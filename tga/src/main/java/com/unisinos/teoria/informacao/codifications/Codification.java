package com.unisinos.teoria.informacao.codifications;

import java.io.IOException;

public interface Codification {
 
  public byte[] compress(int[] asciiLetters) throws IOException;
  
  public int[] decompress(byte[] bytes) throws IOException;
  
}
