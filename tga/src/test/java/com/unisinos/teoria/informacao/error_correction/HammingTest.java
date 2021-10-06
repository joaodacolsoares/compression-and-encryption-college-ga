package com.unisinos.teoria.informacao.error_correction;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class HammingTest {

    @Test
    public void shouldCorrectlyGenerateHammingCodeword() throws IOException {
        byte[] data = {1, 0, 1, 1};
        byte[] expectedHammingCodeword = {1, 0, 1, 1, 0, 0, 1};

        byte[] hammingCodeword = Hamming.encode(data);

        for (int i = 0; i < expectedHammingCodeword.length; i++) {
            assertEquals(expectedHammingCodeword[i], hammingCodeword[i]);
        }
    }

    @Test
    public void shouldCorrectErrorsFor0000000() throws IOException {
        byte[] expectedData = {0, 0, 0, 0, 0, 0, 0};
        byte[] corruptData = {0, 0, 0, 0, 0, 0, 0};


        for (int i = 0; i < corruptData.length; i++) {
            corruptData[i] ^= 1;
            Hamming.decode(corruptData);

            for (int j = 0; j < corruptData.length; j++) {
                assertEquals(corruptData[i], expectedData[i]);
            }
        }
    }

    @Test
    public void shouldCorrectErrorsFor0001011() throws IOException {
        byte[] expectedData = {0, 0, 0, 1, 0, 1, 1};
        byte[] corruptData = {0, 0, 0, 1, 0, 1, 1};


        for (int i = 0; i < corruptData.length; i++) {
            corruptData[i] ^= 1;
            Hamming.decode(corruptData);

            for (int j = 0; j < corruptData.length; j++) {
                assertEquals(corruptData[i], expectedData[i]);
            }
        }
    }

    @Test
    public void shouldCorrectErrorsFor0010111() throws IOException {
        byte[] expectedData = {0, 0, 1, 0, 1, 1, 1};
        byte[] corruptData = {0, 0, 1, 0, 1, 1, 1};


        for (int i = 0; i < corruptData.length; i++) {
            corruptData[i] ^= 1;
            Hamming.decode(corruptData);

            for (int j = 0; j < corruptData.length; j++) {
                assertEquals(corruptData[i], expectedData[i]);
            }
        }
    }

    @Test
    public void shouldCorrectErrorsFor0011100() throws IOException {
        byte[] expectedData = {0, 0, 1, 1, 1, 0, 0};
        byte[] corruptData = {0, 0, 1, 1, 1, 0, 0};


        for (int i = 0; i < corruptData.length; i++) {
            corruptData[i] ^= 1;
            Hamming.decode(corruptData);

            for (int j = 0; j < corruptData.length; j++) {
                assertEquals(corruptData[i], expectedData[i]);
            }
        }
    }
}
