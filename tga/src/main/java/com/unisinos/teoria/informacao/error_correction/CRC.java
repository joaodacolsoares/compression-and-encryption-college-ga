package com.unisinos.teoria.informacao.error_correction;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CRC {
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
}
