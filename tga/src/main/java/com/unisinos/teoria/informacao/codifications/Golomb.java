package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.unisinos.teoria.informacao.enums.CodificationHeader;

import htsjdk.samtools.cram.io.BitInputStream;
import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitInputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;
import htsjdk.samtools.util.RuntimeEOFException;

public class Golomb extends AbstractCodification implements Codification {

  private static final boolean STOP_BIT = Boolean.TRUE;

  private int k;

  public Golomb(int k) {
    this.k = k;
  }

  public byte[] compressData(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bits = new DefaultBitOutputStream(bytes);

    writeHeader(bits);
  
    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      int remainder  = asciiLetter % k;
      double quotient = Math.floor(asciiLetter / k);
  
      for (int j = 0; j < quotient; j++) {
        bits.write(false);      
      }

      bits.write(true);

      int length = (int) (Math.log10(k) / Math.log10(2));
      String remainderInBinary = Integer.toBinaryString(remainder);
      
      for (int j = 0; j < length - remainderInBinary.length(); j++) {
        bits.write(false);
      }  

      for (int j = 0; j < remainderInBinary.length(); j++) {
        bits.write(remainderInBinary.charAt(j) == '1');
      }
      
    }

    bits.close();
    
    return bytes.toByteArray();
  }

  public int[] decompressData(byte[] bytes) {
    ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
    BitInputStream bits = new DefaultBitInputStream(byteArray);
    
    int countZero = 0;

    List<Integer> asciiLetters = new ArrayList<>();

    bits.readBits(BYTE_SIZE);
    int k = bits.readBits(BYTE_SIZE);
    //CRC
    
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

  private void writeHeader(BitOutputStream bits) {
    String header = CodificationHeader.GOLOMB.getValue();

    for (int i = 0; i < BYTE_SIZE - header.length(); i++) {
      bits.write(false);
    }

    for (int i = 0; i < header.length(); i++) {
      bits.write(header.charAt(i) == '1');
    }

    String binaryK = Integer.toBinaryString(k);

    for (int j = 0; j < BYTE_SIZE - binaryK.length(); j++) {
      bits.write(false);
    }  

    for (int i = 0; i < binaryK.length(); i++) {
      bits.write(binaryK.charAt(i) == '1');
    }
  }
   
}
