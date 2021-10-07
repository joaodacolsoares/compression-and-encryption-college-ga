package com.unisinos.teoria.informacao.codifications;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class FibonacciTest {

  @Test
  public void shouldCompress() throws IOException {
    Codification fibonacci = new Fibonacci();
    byte[] result = fibonacci.compress(new int[] {40});
    assertEquals(2, result[0]);
  }

  @Test
  public void shouldDecompress() throws IOException {
    Codification fibonacci = new Fibonacci();
    int[] result = fibonacci.decompress(fibonacci.compress(new int[] {40, 31}));
    assertEquals(40, result[0]);
  }

}
