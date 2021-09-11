import BinaryOperation from "../BinaryOperation";
import Codification from "./Codification";

export default class Golomb implements Codification {
  k : number = 4

  compress(asciiWord: number) : string {
    const remainder  = asciiWord % 4;
    const quotient = Math.floor(asciiWord / 4);

    const prefix = '0'.repeat(quotient);
    const stopBit = '1';
    
    const suffix = BinaryOperation.decToBin(remainder, 2)

    return prefix + stopBit + suffix;
  }

  decompress(codeWord: string) : string {
    return ''
  }
}