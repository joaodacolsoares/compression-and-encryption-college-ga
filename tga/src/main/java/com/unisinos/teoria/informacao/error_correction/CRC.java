package com.unisinos.teoria.informacao.error_correction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import htsjdk.samtools.cram.io.BitInputStream;
import htsjdk.samtools.cram.io.DefaultBitInputStream;

public class CRC {
    private static final String CORRUPTED_CRC_CODE_MESSAGE = "Received crc code is corrupted";


    public static byte[] encode(byte[] data) throws IOException {
        byte[] polynomial = {1, 0, 0, 0, 0, 0, 1, 1, 1};
        int[] encodedData = new int[data.length + polynomial.length - 1];

        for (int i = 0; i < data.length; i++) {
            encodedData[i] = data[i];
        }

        for (int i = 1, j = data.length; i <= polynomial.length - 1; i++, j++) {
            encodedData[j] = 0;
        }

        for (int i = 0; i <= encodedData.length - polynomial.length; ) {
            for (int j = 0; j < polynomial.length; j++) {
                int bit = encodedData[i + j] ^ polynomial[j];
                encodedData[i + j] = bit;
            }
            while (i < encodedData.length && encodedData[i] != 1) {
                i++;
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(bytes);
        for (int i = encodedData.length - polynomial.length + 1; i < encodedData.length; i++) {
            dataOutputStream.write(encodedData[i]);
        }
        dataOutputStream.flush();
        return bytes.toByteArray();
    }

    public static void validateCRC(byte[] crcCode, byte[] data) throws IllegalArgumentException, IOException {
        ByteArrayInputStream outputBytes = new ByteArrayInputStream(encode(data));
        BitInputStream outputBits = new DefaultBitInputStream(outputBytes);

        for (int i = 0; i < crcCode.length; i++) {
            boolean bit = outputBits.readBit();
            if ((bit ? 1 : 0) != crcCode[i]) {
                throw new IllegalArgumentException(CORRUPTED_CRC_CODE_MESSAGE);
            }
        }
        outputBits.close();
    }
}
