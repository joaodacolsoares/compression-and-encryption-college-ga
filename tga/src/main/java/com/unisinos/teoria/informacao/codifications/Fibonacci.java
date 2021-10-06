package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.unisinos.teoria.informacao.enums.CodificationHeader;

import htsjdk.samtools.cram.io.BitInputStream;
import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitInputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;
import htsjdk.samtools.util.RuntimeEOFException;

public class Fibonacci extends AbstractCodification implements Codification {

  private static final boolean STOP_BIT = Boolean.TRUE;

  public byte[] compressData(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);

    Codification.writeHeader(bit, CodificationHeader.FIBONACCI);

    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      List<Integer> fibonacciNumbers = this.calculateFibonacciUntilNumber(asciiLetter, new ArrayList<Integer>(Arrays.asList(1, 2)));
      List<Boolean> occurences = this.calculateOccurences(asciiLetter, fibonacciNumbers);
      occurences.stream().forEach((isOccurence) -> bit.write(isOccurence));
      bit.write(true);
    }
    
    bit.close();
    
    return bytes.toByteArray();
  }
  
  public int[] decompressData(byte[] bytes) throws IOException {
    ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
    BitInputStream bits = new DefaultBitInputStream(byteArray);
  
    bits.readBits(BYTE_SIZE);
    //CRC

    List<Integer> asciiLetters = new ArrayList<>();

    List<Boolean> mappedNumber = new ArrayList<>();
    boolean lastMappedNumber = false;
    while (true) {
      try {
        boolean currentBit = bits.readBit();
        if (currentBit == STOP_BIT && currentBit == lastMappedNumber) {
          asciiLetters.add(calculateFibonacciForBooleanList(mappedNumber));
          mappedNumber.clear();
          lastMappedNumber = false;
        } else {
          lastMappedNumber = currentBit;
          mappedNumber.add(currentBit);
        }
      } catch (RuntimeEOFException e) {break;}
    }

    bits.close();
    return asciiLetters.stream().mapToInt(Integer::intValue).toArray();
  }

  private int calculateFibonacciForBooleanList(List<Boolean> values) {
    List<Integer> fibonacci = new ArrayList<Integer>(Arrays.asList(1, 2));
    for (int i = 2; i < values.size(); i++) {
      fibonacci.add(fibonacci.get(i - 1) + fibonacci.get(i - 2));
    }
    int count = 0;
    for (int i = 0; i < values.size(); i++) {
      if (values.get(i)) {
        count += fibonacci.get(i);
      }
    }
    return count;
  }

  private List<Integer> calculateFibonacciUntilNumber(int objective, List<Integer> current) {
    int index = current.size() - 1;
    int nextFibonacciNumber = current.get(index) + current.get(index - 1);

    if (nextFibonacciNumber >= objective) {
      return current;
    }
  
    current.add(nextFibonacciNumber);
    return this.calculateFibonacciUntilNumber(objective, current);
  }

  private List<Boolean> calculateOccurences(int objective, List<Integer> fibonacciNumbers) {
    Collections.reverse(fibonacciNumbers);
    List<Boolean> fibonacciOccurences = new ArrayList<>();
    int currentObjective = objective;
    for (int i = 0; i < fibonacciNumbers.size(); i++) {
      int fibonacciNumber = fibonacciNumbers.get(i);
      boolean isOccurence = this.toOcurrence(fibonacciNumber, currentObjective);
      fibonacciOccurences.add(isOccurence);
      if (isOccurence) {
        currentObjective = currentObjective - fibonacciNumber;
      }
    }

    Collections.reverse(fibonacciOccurences);

    return fibonacciOccurences;
  }

  private boolean toOcurrence(int fibonacciNumber, int objective) {
    if (objective >= fibonacciNumber) {
      objective = objective - fibonacciNumber;
      return true;
    }
    return false;
  }

}
