package com.unisinos.teoria.informacao.codifications;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class UnaryTest {

  @Test
  public void shouldCompressWith7ZerosAnd1StopBit() throws IOException {
    Codification unary = new Unary();
    byte[] result = unary.compress(new int[] {7});
    assertEquals(1, result[0]);
  }

  @Test
  public void shouldDecompress() throws IOException {
    Codification unary = new Unary();
    int[] result = unary.decompress(unary.compress(new int[] {1100}));
    assertEquals(1100, result[0]);
  }

}
