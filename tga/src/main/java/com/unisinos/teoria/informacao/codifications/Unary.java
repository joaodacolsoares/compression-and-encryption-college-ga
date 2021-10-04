package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;

public class Unary implements Codification {

  public byte[] compress(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);
  
    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];

      for (int j = 0; j < asciiLetter; j++) {
        bit.write(false);      
      }
      bit.write(true);
    }

    bit.close();
    
    return bytes.toByteArray();
  }

  public int[] decompress(byte[] bytes) {
    return new int[] {1};
  }
   
}
