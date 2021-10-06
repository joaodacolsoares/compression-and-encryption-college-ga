package com.unisinos.teoria.informacao.codifications;

public class CodificationFactory {

  public Codification create(String codification, String ...k) {
    if ("ELIAS_GAMMA".equals(codification)) {
      return new EliasGamma();
    } else if ("FIBONACCI".equals(codification)) {
      return new Fibonacci();
    } else if ("GOLOMB".equals(codification)) {
      return new Golomb(Integer.parseInt(k[0]));
    } else if ("UNARY".equals(codification)) {
      return new Unary();
    } else if ("DELTA".equals(codification)) {
      return new Delta();
    }
    return null;
  }
  
}
