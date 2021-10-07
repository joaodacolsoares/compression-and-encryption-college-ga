package com.unisinos.teoria.informacao.codifications;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class GolombTest {

  //11000
  @Test
  public void shouldCompressRemainderWith2Positions() throws IOException {
    Codification golomb = new Golomb(4);
    byte[] result = golomb.compress(new int[] {14});
    assertEquals(1, result[0]);
  }

  //01110000
  @Test
  public void shouldCompressRemainderWith3Positions() throws IOException {
    Codification golomb = new Golomb(8);
    byte[] result = golomb.compress(new int[] {14});
    assertEquals(1, result[0]);
  }

  @Test
  public void shouldDecompressRemainderWith3Positions() throws IOException {
    Codification golomb = new Golomb(8);
    int[] result = golomb.decompress(golomb.compress(new int[] {14}));
    assertEquals(14, result[0]);
  }

  @Test
  public void shouldDecompressRemainderWith2Positions() throws IOException {
    Codification golomb = new Golomb(4);
    int[] result = golomb.decompress(golomb.compress(new int[] {14}));
    assertEquals(14, result[0]);
  }

  @Test
  public void shouldDecompressRemainderWith2Letters() throws IOException {
    Codification golomb = new Golomb(4);
    int[] result = golomb.decompress(golomb.compress(new int[] {14, 18}));
    assertEquals(14, result[0]);
    assertEquals(18, result[1]);
  }
}
