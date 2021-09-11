import BinaryOperation from "../BinaryOperation";
import Codification from "./Codification";

export default class EliasGamma implements Codification {
  k : number = 4

  compress(asciiWord: number) : string {
    const n  = Math.floor(Math.log2(asciiWord));
    const remainder = asciiWord - Math.pow(n, 2)

    const prefix = '0'.repeat(n);
    const stopBit = '1';
    const suffix = BinaryOperation.decToBin(remainder, n)

    return prefix + stopBit +  suffix;
  }

  decompress(codeWord: string) : string {
    return ''
  }
}