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
        byte[] header = new byte[16];

        ByteArrayInputStream byteArray = new ByteArrayInputStream(compressedData);
        BitInputStream bits = new DefaultBitInputStream(byteArray);

        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        BitOutputStream outputBits = new DefaultBitOutputStream(outputBytes);

        for (int i = 0; i < header.length; i++) {
            boolean bitValue = bits.readBit();
            header[i] = bitValue ? (byte) 1 : 0;
            outputBits.write(bitValue);
        }

        byte[] crcCode = CRC.encode(header);

        ByteArrayInputStream crcBytes = new ByteArrayInputStream(crcCode);
        BitInputStream crcBits = new DefaultBitInputStream(crcBytes);

        for (int i = 0; i < BYTE_SIZE; i++) {
            boolean bit = crcBits.readBit();
            System.out.print(bit ? "1" : "0");
            outputBits.write(bit);
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
        crcBits.close();
        bits.close();
        outputBits.close();
        return outputBytes.toByteArray();
    }

    @Override
    public int[] decompress(byte[] bytes) throws IOException {
        byte[] header = new byte[16];
        byte[] crcCode = new byte[8];

        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        BitInputStream bits = new DefaultBitInputStream(byteArray);

        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        BitOutputStream outputBits = new DefaultBitOutputStream(outputBytes);

        for (int i = 0; i < header.length; i++) {
            boolean bitValue = bits.readBit();
            header[i] = bitValue ? (byte) 1 : 0;
            outputBits.write(bitValue);
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
