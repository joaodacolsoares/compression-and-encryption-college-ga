# Trabalho Teoria da Informação - Criptografia e Compressão

class App

CodificationFactory

FibonacciCodification

interface Codification
 - compress
 - decompress

input -> ascii -> codeWord -> buffer

### How to run
In order to run you need to execute the following command.
```
mvn exec:java -Dexec.mainClass=com.unisinos.teoria.informacao.App -Dexec.args="<input_file> <output_file> <action> <codification> <golomb_k>"
```
You need to replace the values for the <input_file> and <output_file> with the path for your files.
The values for the action can be:
 - "ENCODE"
 - "DECODE"

The values for codification:
 - "ELIAS_GAMMA"
 - "GOLOMB"
 - "FIBONACCI"
 - "UNARY"
 - "DELTA"

The <golomb_k> can be replaced with any value you want to use in the golomb compression.
