package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;

public class Golomb implements Codification {

  private int k;

  public Golomb(int k) {
    this.k = k;
  }

  public byte[] compress(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);
  
    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      int remainder  = asciiLetter % k;
      double quotient = Math.floor(asciiLetter / k);
  
      for (int j = 0; j < quotient; j++) {
        bit.write(false);      
      }

      bit.write(true);

      int length = (int) (Math.log10(k) / Math.log10(2));
      String remainderInBinary = Integer.toBinaryString(remainder);
      
      for (int j = 0; j < length - remainderInBinary.length(); j++) {
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
