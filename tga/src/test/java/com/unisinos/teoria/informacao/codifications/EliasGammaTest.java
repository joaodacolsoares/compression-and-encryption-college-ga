package com.unisinos.teoria.informacao.codifications;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class EliasGammaTest {

  @Test
  public void shouldCompressWith2PositionsOnEachSide() throws IOException {
    Codification eliasGamma = new EliasGamma();
    byte[] result = eliasGamma.compress(new int[] {7});

    assertEquals(56, result[0]);
  }

  @Test
  public void shouldDecompress() throws IOException {
    Codification eliasGamma = new EliasGamma();
    int[] result = eliasGamma.decompress(eliasGamma.compress(new int[] {67, 51, 98}));

    assertEquals(67, result[0]);
    assertEquals(51, result[1]);
    assertEquals(98, result[2]);
  }
  
}
