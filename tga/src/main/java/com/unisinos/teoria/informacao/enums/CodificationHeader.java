package com.unisinos.teoria.informacao.enums;

import java.util.Arrays;

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
    return Arrays.asList(values()).stream().filter((header) ->  header.getValue().equals(String.valueOf(bits))).findFirst().get();
  }

}
