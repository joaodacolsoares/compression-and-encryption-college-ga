package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;

public class Fibonacci implements Codification {

  public byte[] compress(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);

    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      int[] fibonacciNumbers = this.calculateFibonacciUntilNumber(asciiLetter, new int[] {1});
      this.calculateCodeWord(asciiLetter, fibonacciNumbers, bit);
    }
  
    bit.close();
    
    return bytes.toByteArray();
  }
  
  public int[] decompress(byte[] bytes) {
    return new int[] {1};
  }

  private int[] calculateFibonacciUntilNumber(int objective, int[] current) {
    int index = current.length();

    if(current[index - 1] >= objective) {
      current.pop()
      return current
    }

    if(index == 1) {
      current.push(2)
    } else {
      current.push(current[index - 1] + current[index - 2])
    }

    return this.calculateFibonacciUntilNumber(objective, current);
  }

  private int[] calculateCodeWord(int objective, int[] fibonacciNumbers, BitOutputStream bit) {
    int objectiveTotal = objective;

    // só fazer isso aqui em java com o ArrayUtils.reverse(int[] array)

    return fibonacciNumbers.slice().reverse().map(number => {
      if(objectiveTotal >= number) {
        bit.write(true)
      }
      bit.write(false)
    }).reverse().join('')
  }
}
