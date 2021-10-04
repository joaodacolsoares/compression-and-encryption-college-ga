package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;

public class Fibonacci implements Codification {

  public byte[] compress(int[] asciiLetters) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BitOutputStream bit = new DefaultBitOutputStream(bytes);

    for (int i = 0; i < asciiLetters.length; i++) {
      int asciiLetter = asciiLetters[i];
      List<Integer> fibonacciNumbers = this.calculateFibonacciUntilNumber(asciiLetter, new ArrayList<Integer>(Arrays.asList(1, 2)));
      List<Boolean> occurences = this.calculateOccurences(asciiLetter, fibonacciNumbers);
      occurences.stream().forEach((isOccurence) -> bit.write(isOccurence));
    }
    
    bit.close();
    
    return bytes.toByteArray();
  }
  
  public int[] decompress(byte[] bytes) {
    return new int[] {1};
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
