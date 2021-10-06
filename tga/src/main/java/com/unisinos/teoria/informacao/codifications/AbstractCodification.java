package com.unisinos.teoria.informacao.codifications;

import com.unisinos.teoria.informacao.error_correction.CRC;
import com.unisinos.teoria.informacao.error_correction.Hamming;
import htsjdk.samtools.cram.io.BitInputStream;
import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitInputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;
import htsjdk.samtools.util.RuntimeEOFException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractCodification implements Codification {
    @Override
    public byte[] compress(int[] asciiLetters) throws IOException {
        byte[] compressedData = compressData(asciiLetters);
        byte[] header = new byte[8];

        ByteArrayInputStream byteArray = new ByteArrayInputStream(compressedData);
        BitInputStream bits = new DefaultBitInputStream(byteArray);

        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        BitOutputStream outputBits = new DefaultBitOutputStream(outputBytes);

        for (int i = 0; i < header.length; i++) {
            header[i] = bits.readBit() ? (byte) 1 : 0;
            outputBits.write(bits.readBit());
        }

        byte[] crcCode = CRC.encode(header);
        for (byte crcByte : crcCode) {
            outputBits.write(crcByte == 1);
        }

        byte[] hammingData = new byte[4];
        for (int i = 0; ; i++) {
            try {
                if ((i + 1) % 4 == 0) {
                    hammingData[i % 4] = bits.readBit() ? (byte) 1 : 0;
                    byte[] hammingCodeWord = Hamming.encode(hammingData);

                    for (byte hammingByte : hammingCodeWord) {
                        outputBits.write(hammingByte == 1);
                    }
                } else {
                    hammingData[i % 4] = bits.readBit() ? (byte) 1 : 0;
                }
            } catch (RuntimeEOFException exception) {
                break;
            }
        }
        outputBits.close();
        return outputBytes.toByteArray();
    }

    @Override
    public int[] decompress(byte[] bytes) throws IOException {
        byte[] header = new byte[8];
        byte[] crcCode = new byte[8];

        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        BitInputStream bits = new DefaultBitInputStream(byteArray);

        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        BitOutputStream outputBits = new DefaultBitOutputStream(outputBytes);

        for (int i = 0; i < header.length; i++) {
            header[i] = bits.readBit() ? (byte) 1 : 0;
        }

        for (int i = 0; i < crcCode.length; i++) {
            crcCode[i] = bits.readBit() ? (byte) 1 : 0;
        }

        CRC.validateCRC(crcCode, header);

        byte[] hammingCodeWord = new byte[7];
        for (int i = 0; ; i++) {
            try {
                if ((i + 1) % 7 == 0) {
                    hammingCodeWord[i % 7] = bits.readBit() ? (byte) 1 : 0;
                    byte[] decodedData = Hamming.decode(hammingCodeWord);

                    for (byte bit : decodedData) {
                        outputBits.write(bit == 1);
                    }
                } else {
                    hammingCodeWord[i % 7] = bits.readBit() ? (byte) 1 : 0;
                }
            } catch (RuntimeEOFException exception) {
                break;
            }
        }
        outputBits.close();
        return decompressData(outputBytes.toByteArray());
    }

    protected abstract byte[] compressData(int[] ascciLetters) throws IOException;

    protected abstract int[] decompressData(byte[] bytes) throws IOException;

}
