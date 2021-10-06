package com.unisinos.teoria.informacao.codifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.unisinos.teoria.informacao.enums.CodificationHeader;

import htsjdk.samtools.cram.io.BitOutputStream;
import htsjdk.samtools.cram.io.DefaultBitOutputStream;

public class Delta extends AbstractCodification implements Codification {

    public byte[] compressData(int[] asciiLetters) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        BitOutputStream bit = new DefaultBitOutputStream(bytes);

        Codification.writeHeader(bit, CodificationHeader.DELTA);

        List<Integer> encoded = new ArrayList<>();
        int last = 0;
        for (int value : asciiLetters) {
            if (encoded.isEmpty()) {
                encoded.add(value);
            } else {
                encoded.add(value - last);
            }
            last = value;
        }
        encoded.stream().map(Integer::toBinaryString).forEach((differenceInBinary) -> {
            for (int i = 0; i < differenceInBinary.length(); i++) {
                System.out.print(differenceInBinary.charAt(i) == '1' ? "1" : "0");
                bit.write(differenceInBinary.charAt(i) == '1');
            }
        });

        bit.close();
        return bytes.toByteArray();
    }

    public static int[] deltaEncode(int[] buffer) {
        int original, delta = 0;
        for (int i = 0; i < buffer.length; ++i) {
            original = buffer[i];
            buffer[i] -= delta;
            delta = original;
        }
        return buffer;
    }

    public int[] decompressData(byte[] bytes) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
