export default class Hamming {
    hammingParityGeneratorArray: string[] = ["101", "110", "111", "011"]

    generateCodeWord(asciiWord: string): string {
        const bits: string[] = asciiWord.split('');
        var hammingParity: string;
        for (var i: number = 0; i < bits.length; i++) {
            if (bits[i]) {
                if (hammingParity) {
                    var currentParity: string = "";
                    for (var j: number = 0; j < hammingParity.length; j++) {
                        currentParity += hammingParity.charCodeAt(j) ^ this.hammingParityGeneratorArray[i].charCodeAt(j);
                    }
                    hammingParity = currentParity;
                } else {
                    hammingParity = this.hammingParityGeneratorArray[i];
                }
            }
        }

        if (!hammingParity) {
            hammingParity = "000";
        }
        return asciiWord + hammingParity;
    }
}