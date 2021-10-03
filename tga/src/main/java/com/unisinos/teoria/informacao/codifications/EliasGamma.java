package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;

public class EliasGamma implements Codification {

  public byte[] compress(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);

    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      int n = (int) Math.floor(Math.log10(asciiLetter) / Math.log10(2));
      int remainder = (int) (asciiLetter - Math.pow(n, 2));

      for (int j = 0; j < n; j++) {
        bit.write(false);
      }
      bit.write(true);

      String remainderInBinary = Integer.toBinaryString(remainder);

      for (int j = 0; j < n - remainderInBinary.length(); j++) {
        bit.write(false);
      }  

      for (int j = 0; j < remainderInBinary.length() - 1; j++) {
        bit.write(remainderInBinary.charAt(j) == '1');
      }

    }
  
    bit.close();
    
    return bytes.toByteArray();
  }
  
  public int[] decompress(byte[] bytes) {
    return new int[] {1};
  }
}
