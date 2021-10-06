package com.unisinos.teoria.informacao.error_correction;

import java.io.*;
import java.util.*;

public class Hamming {
    private static final String ERROR_MESSAGE = "Error detected in hamming code word %s at index %d";

    public static byte[] encode(byte[] asciiWord) throws IOException {
        int[] hammingCodeWord = new int[7];
        for (int i = 0; i < asciiWord.length; i++) {
            hammingCodeWord[i] = asciiWord[i];
        }

        hammingCodeWord[4] = hammingCodeWord[0] ^ hammingCodeWord[1] ^ hammingCodeWord[2];
        hammingCodeWord[5] = hammingCodeWord[1] ^ hammingCodeWord[2] ^ hammingCodeWord[3];
        hammingCodeWord[6] = hammingCodeWord[0] ^ hammingCodeWord[2] ^ hammingCodeWord[3];

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        for (int value : hammingCodeWord) {
            data.write(value);
        }
        data.flush();

        return bytes.toByteArray();
    }

    public static byte[] decode(byte[] asciiWord) throws IOException {
        Set<Integer> validBitPositions = new HashSet<>();
        Set<Integer> incorrectParities = new HashSet<>();

        StringBuilder asciiWordString = new StringBuilder();
        for (byte bit : asciiWord) {
            asciiWordString.append(bit);
        }

        Map<Integer, List<Integer>> parityToPositionsMap = getParityToPositionsMap();
        byte[] hammingCodeWord = getHammingWordToCompare(asciiWord);
        for (int i = 4; i < hammingCodeWord.length; i++) {
            if (hammingCodeWord[i] == asciiWord[i]) {
                validBitPositions.addAll(parityToPositionsMap.get(i));
            } else {
                incorrectParities.add(i);
            }
        }

        if (incorrectParities.isEmpty()) {
            return asciiWord;
        }

        int incorrectBitPosition = getIncorrectBitPosition(validBitPositions, incorrectParities);
        if (incorrectBitPosition != -1) {
            System.out.println(String.format(ERROR_MESSAGE, asciiWordString, incorrectBitPosition));
            asciiWord[incorrectBitPosition] ^= 1;
        } else if (!incorrectParities.isEmpty()) {
            for (int incorrectParity : incorrectParities) {
                System.out.println(String.format(ERROR_MESSAGE, asciiWordString, incorrectParity));
                asciiWord[incorrectParity] ^= 1;
            }
        }
        return Arrays.copyOfRange(asciiWord, 0, 4);
    }

    private static int getIncorrectBitPosition(Set<Integer> validBitPositions, Set<Integer> incorrectParities) {
        final Map<Integer, Integer> possibleInvalidBitsToOccurrences = findPossibleInvalidBitPositions(validBitPositions, incorrectParities);
        return getIncorrectBitPosition(possibleInvalidBitsToOccurrences);
    }

    private static Map<Integer, Integer> findPossibleInvalidBitPositions(Set<Integer> validBitPositions, Set<Integer> incorrectParities) {
        final Map<Integer, Integer> possibleInvalidBitsToOccurrences = new HashMap<>();
        for (int invalidParity : incorrectParities) {
            for (int bitPosition : getParityToPositionsMap().get(invalidParity)) {
                if (!validBitPositions.contains(bitPosition)) {
                    possibleInvalidBitsToOccurrences.put(bitPosition,
                            possibleInvalidBitsToOccurrences.getOrDefault(bitPosition, 0) + 1);
                }
            }
        }
        return possibleInvalidBitsToOccurrences;
    }

    private static int getIncorrectBitPosition(Map<Integer, Integer> possibleInvalidBitsToOccurrences) {
        int incorrectBitPosition = -1;
        for (int key : possibleInvalidBitsToOccurrences.keySet()) {
            if (possibleInvalidBitsToOccurrences.getOrDefault(incorrectBitPosition, 0)
                    < possibleInvalidBitsToOccurrences.get(key)) {
                incorrectBitPosition = key;
            }
        }
        return incorrectBitPosition;
    }

    private static byte[] getHammingWordToCompare(byte[] asciiWord) throws IOException {
        byte[] hammingCodeWord = new byte[7];
        for (int i = 0; i < 4; i++) {
            hammingCodeWord[i] = asciiWord[i];
        }
        hammingCodeWord = encode(hammingCodeWord);
        return hammingCodeWord;
    }

    private static Map<Integer, List<Integer>> getParityToPositionsMap() {
        Map<Integer, List<Integer>> parityToPositionsMap = new HashMap<>();
        parityToPositionsMap.put(4, Arrays.asList(0, 1, 2));
        parityToPositionsMap.put(5, Arrays.asList(1, 2, 3));
        parityToPositionsMap.put(6, Arrays.asList(0, 2, 3));
        return parityToPositionsMap;
    }
}
