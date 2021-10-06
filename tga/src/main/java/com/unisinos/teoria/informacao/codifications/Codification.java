package com.unisinos.teoria.informacao.codifications;

import java.io.IOException;

import com.unisinos.teoria.informacao.enums.CodificationHeader;

import htsjdk.samtools.cram.io.BitOutputStream;

public interface Codification {

  public static final int BYTE_SIZE = 8;
 
  public byte[] compress(int[] asciiLetters) throws IOException;
  
  public int[] decompress(byte[] bytes) throws IOException;

  static void writeHeader(BitOutputStream bits, CodificationHeader headerType) {
    String header = headerType.getValue();
    for (int i = 0; i < header.length(); i++) {
      bits.write(header.charAt(i) == '1');
    }
    
    for (int i = 0; i < BYTE_SIZE; i++) {
      bits.write(false);
    }
  }
  
}
