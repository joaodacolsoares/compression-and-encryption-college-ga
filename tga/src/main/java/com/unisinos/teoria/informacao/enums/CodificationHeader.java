package com.unisinos.teoria.informacao.enums;

public enum CodificationHeader {

  GOLOMB("1"),
  FIBONACCI("2"),
  ELIAS_GAMMA("4"),
  UNARY("8"),
  DELTA("16");

  String value;
  private CodificationHeader(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }

  public static CodificationHeader fromValue(int bits) {
    return valueOf(String.valueOf(bits));
  }

}
