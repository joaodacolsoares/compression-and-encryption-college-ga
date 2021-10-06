package com.unisinos.teoria.informacao.error_correction;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CRCTest {
    @Test
    public void shouldCorrectlyGenerateHammingCodeword() throws IOException {
        byte[] data = {0, 1, 0, 1, 0, 1, 1, 1};
        byte[] expectedCRCCode = {1, 0, 1, 0, 0, 0, 1, 0};
        byte[] crcCode = CRC.encode(data);

        for (int i = 0; i < expectedCRCCode.length; i++) {
            assertEquals(expectedCRCCode[i], crcCode[i]);
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenDataIsCorrupted() throws IOException {
        byte[] invalidData = {1, 1, 0, 1, 0, 1, 1, 1};
        byte[] validData = {0, 1, 0, 1, 0, 1, 1, 1};
        byte[] crcCode = CRC.encode(validData);
        CRC.validateCRC(crcCode, invalidData);
    }
}
