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

public class Unary extends AbstractCodification implements Codification {

  public byte[] compressData(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);

    Codification.writeHeader(bit, CodificationHeader.UNARY);
  
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

  public int[] decompressData(byte[] bytes) {
    ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
    BitInputStream bits = new DefaultBitInputStream(byteArray);

    bits.readBits(BYTE_SIZE);
    //CRC

    List<Boolean> allBits = readAllBits(bits, bytes.length);
    List<Integer> asciiLetters = new ArrayList<>();
    Integer counter = 0;
    for (int i = 0; i < allBits.size(); i++) {
      boolean currentBit = allBits.get(i);
      if (!currentBit) {
        counter++;
      }
      if (currentBit) {
        asciiLetters.add(counter);
        counter = 0;
      }
    }
    return asciiLetters.stream().mapToInt(Integer::intValue).toArray();
  }

  private List<Boolean> readAllBits(BitInputStream bits, int numberOfBytes) {
    int numberOfBits = numberOfBytes * 8;
    List<Boolean> allBits = new ArrayList<>();
    for (int i = 0; i < numberOfBits; i++) {
      allBits.add(bits.readBit());
    }
    return allBits;
  }

}
