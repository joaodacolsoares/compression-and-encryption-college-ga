export default class BinaryOperation {
  static decToBin(dec : number, length : number) : string {
    var out = "";
    while(length--) {
      console.log(dec, length)
      out += (dec >> length) & 1;
    }
    return out;
  }
}