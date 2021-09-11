import fs from 'fs'
import Codification from './codificators/Codification'

class App {
  
  async start(inputPath : string, outputPath : string, codification : Codification | null) {
    const asciiWords = await this.readFile(inputPath);

    const output = asciiWords
      .map(word => this.fromAsciiToBinary(word))
      // .map(word => codification.compress(word))
      .map(word => this.fromBinaryToAscii(word))

    var buf = Buffer.from(output);

    const wstream = fs.createWriteStream(outputPath);
    wstream.write(buf);
  }

  private readFile(inputPath: string) : Promise<number[]> {
    return new Promise<number[]>((resolve, reject) => {
      fs.readFile(inputPath, (err, data) => {
        if (err) {
          console.error(err)
          reject();
        }

        const asciiWords : number[] = []
        data.forEach(word => asciiWords.push(word));
        resolve(asciiWords)
      });
    })
  }

  private fromAsciiToBinary(decimalNumber: number) {
    return decimalNumber.toString(2);
  }

  private fromBinaryToAscii(binaryWord: string) {
    return parseInt(binaryWord, 2);
  }

}

const app = new App();
app.start(process.argv[2], process.argv[3], null);
