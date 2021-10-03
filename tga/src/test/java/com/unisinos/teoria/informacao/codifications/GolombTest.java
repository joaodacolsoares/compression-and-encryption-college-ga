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
    assertEquals(24, result[0]);
  }

  //01110000
  @Test
  public void shouldCompressRemainderWith3Positions() throws IOException {
    Codification golomb = new Golomb(8);
    byte[] result = golomb.compress(new int[] {14});
    assertEquals(112, result[0]);
  }

}
