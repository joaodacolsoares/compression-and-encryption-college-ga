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

public class EliasGamma implements Codification {

  private static final boolean STOP_BIT = Boolean.TRUE;

  public byte[] compress(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);

    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      int n = (int) Math.floor(Math.log10(asciiLetter) / Math.log10(2));
      int remainder = (int) (asciiLetter - Math.pow(2, n));

      for (int j = 0; j < n; j++) {
        bit.write(false);
      }
      bit.write(true);

      String remainderInBinary = Integer.toBinaryString(remainder);

      for (int j = 0; j < n - remainderInBinary.length(); j++) {
        bit.write(false);
      }  

      for (int j = 0; j < remainderInBinary.length(); j++) {
        bit.write(remainderInBinary.charAt(j) == '1');
      }
    }
  
    bit.close();
    
    return bytes.toByteArray();
  }
  
  public int[] decompress(byte[] bytes) throws IOException {
    ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
    BitInputStream bits = new DefaultBitInputStream(byteArray);
  
    List<Integer> asciiLetters = new ArrayList<>();

    int countZero = 0;
    while (true) {
      try {
        boolean currentBit = bits.readBit();
        if (!currentBit) {
          countZero++;
        }
  
        if (STOP_BIT == currentBit) {
          int remainder = bits.readBits(countZero);
          asciiLetters.add((int) Math.pow(2, countZero) + remainder);
          countZero = 0;
        }
      } catch (RuntimeEOFException e) {break;}
    }

    bits.close();
    return asciiLetters.stream().mapToInt(Integer::intValue).toArray();
  }

}
