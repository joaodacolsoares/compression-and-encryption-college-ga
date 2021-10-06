package com.unisinos.teoria.informacao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.unisinos.teoria.informacao.codifications.Codification;
import com.unisinos.teoria.informacao.codifications.CodificationFactory;
import com.unisinos.teoria.informacao.enums.CodificationHeader;

import org.apache.commons.io.FileUtils;

import htsjdk.samtools.cram.io.BitInputStream;
import htsjdk.samtools.cram.io.DefaultBitInputStream;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        String originFile = args[0];
        String encodeOrDecode = args[1];
        CodificationFactory codificationFactory = new CodificationFactory();
        byte[] file = Files.readAllBytes(Paths.get(originFile));
        
        ByteArrayInputStream byteArray = new ByteArrayInputStream(file);
        BitInputStream bits = new DefaultBitInputStream(byteArray);
        
        
        if ("ENCODE".equals(encodeOrDecode)) {
            Codification codification = codificationFactory.create(args[2], args.length >= 4 ? args[3] : "");
            byte[] result = codification.compress(new String(file).chars().toArray());
            FileUtils.writeByteArrayToFile(new File(originFile + ".ecc"), result);
        } else {
            CodificationHeader codificationHeader = CodificationHeader.fromValue(bits.readBits(Codification.BYTE_SIZE));
            Codification codification = codificationFactory.create(codificationHeader.toString(), Integer.toString(bits.readBits(Codification.BYTE_SIZE)));
            // Codification codification = codificationFactory.create("UNARY");
            int[] result = codification.decompress(file);
            String buffer = new String();
            for (int i = 0; i < result.length; i++) {
                buffer += (char) result[i];
            }
            FileUtils.write(new File("./output"), buffer, StandardCharsets.UTF_8);
        }
        
    }
}
