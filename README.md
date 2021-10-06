# Teoria da Informação - Encode e Decode

## Descrição
Esse projeto é um trabalho da faculdade em que consiste na criação de um Encoder/Decoder que suporte os seguintes tipos de codificações: Unário, Elias Gamma, Fibonacci, Delta e Golomb. 

## Tecnologias
Nós começamos a desenvolver o projeto em TypeScript(nodejs), mas depois de uma breve validação resolvemos optar por utilizar **Java** pois era mais fácil o acesso ao nível de bits. 

## Instalação
Requisitos para iniciar: 
 - Java 8 
 - Maven

### Clonando o repositório
Para clonar o repositório deve rodar o seguinte comando:
`git clone https://github.com/joaodacolsoares/compression-and-encryption-college-ga.git`

### Como rodar
Para rodar a aplicação, tem que utilizar esse comando na home do projeto onde se deve substituir os valores de 
 `<input_file>`, `<action>`, `<codification>` e `<golomb_k>` com os valores respectivos no path:
`mvn exec:java -Dexec.mainClass=com.unisinos.teoria.informacao.App -Dexec.args="<input_file> <output_file> <action> <codification> <golomb_k>"`

Valor para `<input_file>`:
 - O path do seu arquivo em relação a pasta principal do projeto

Valores para `<action>`:
-   "ENCODE"
-   "DECODE"

Valores para `<codification>`:
-   "ELIAS_GAMMA"
-   "GOLOMB"
-   "FIBONACCI"
-   "UNARY"
-   "DELTA"

O valor de `<golomb_k>` pode ser trocado por um inteiro que serve como o k.
