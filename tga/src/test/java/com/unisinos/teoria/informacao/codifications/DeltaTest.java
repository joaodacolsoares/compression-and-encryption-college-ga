package com.unisinos.teoria.informacao.codifications;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class DeltaTest {
  
  @Test
  public void shouldCompress() throws IOException {
    Codification delta = new Delta();
    byte[] result = delta.compress(new int[] {12, 13, 14, 15});
    assertEquals(16, result[0]);
  }

}
