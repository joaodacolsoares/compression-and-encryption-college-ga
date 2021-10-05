package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import htsjdk.samtools.cram.io.BitInputStream;
import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitInputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;
import htsjdk.samtools.util.RuntimeEOFException;

public class Golomb implements Codification {

  private static final boolean STOP_BIT = Boolean.TRUE;

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

      for (int j = 0; j < remainderInBinary.length(); j++) {
        bit.write(remainderInBinary.charAt(j) == '1');
      }
      
    }

    bit.close();
    
    return bytes.toByteArray();
  }

  public int[] decompress(byte[] bytes) {
    ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
    BitInputStream bits = new DefaultBitInputStream(byteArray);
    
    int countZero = 0;

    List<Integer> asciiLetters = new ArrayList<>();
    
    while (true) {
      try {
        boolean currentBit = bits.readBit();
        if (!currentBit) {
          countZero++;
        }
        if (STOP_BIT == currentBit) {
          int remainder = bits.readBits((int) (Math.log10(k) / Math.log10(2)));
          asciiLetters.add((countZero * k) + remainder);
          countZero = 0;
        }
      } catch (RuntimeEOFException e) {break;}
    }
    return asciiLetters.stream().mapToInt(Integer::intValue).toArray();

  }
   
}
