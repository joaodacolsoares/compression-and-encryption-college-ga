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

    detectErrors(asciiWord: string): string {
        const generatedParity: string = this.generateCodeWord(asciiWord.substring(0, 4));
        var parityPositionsMap: Map<number, Array<number>> = this.getParityPositionMap();

        var validIndexes: Set<number> = new Set<number>();
        var invalidIndexes: Set<number> = new Set<number>()
        var invalidParity: Set<number> = new Set<number>();

        for (var i = 0; i < asciiWord.length - 3; i++) {
            if (asciiWord.charAt(i) != generatedParity.charAt(i)) {
                invalidIndexes.add(i);
            }
        }

        for (var i = 4; i < asciiWord.length; i++) {
            if (asciiWord.charAt(i) != generatedParity.charAt(i)) {
                var parityPositions: Array<number> = parityPositionsMap.get(i);
                parityPositions.filter(parityPosition => !validIndexes.has(parityPosition))
                    .forEach(invalidIndexes.add, invalidIndexes);
                invalidParity.add(i);
            } else {
                parityPositionsMap.get(i).forEach(validIndexes.add, validIndexes);
                parityPositionsMap.get(i).forEach(invalidIndexes.delete, invalidIndexes);
            }
        }

        return this.correctInvalidBits(asciiWord, invalidIndexes, invalidParity);
    }

    private correctInvalidBits(asciiWord: string, invalidIndexes: Set<number>, invalidParity: Set<number>): string {
        var asciiWordCorrected: string[] = asciiWord.split('');
        if (invalidParity.size == 3 && invalidIndexes.size == 4) {
            asciiWordCorrected[2] = this.getOppositeCharacter(asciiWordCorrected[2]);
        } else if (invalidIndexes.size == 0 && invalidParity.size > 0) {
            invalidParity.forEach(indexToFix => {
                asciiWordCorrected[indexToFix] = this.getOppositeCharacter(asciiWordCorrected[indexToFix]);
            });
        } else if (invalidIndexes.size > 0) {
            invalidParity.forEach(indexToFix => {
                asciiWordCorrected[indexToFix] = this.getOppositeCharacter(asciiWordCorrected[indexToFix]);
            });
        }
        return asciiWordCorrected.toString();
    }

    private getParityPositionMap(): Map<number, Array<number>> {
        var parityPositionsMap = new Map<number, Array<number>>();
        parityPositionsMap.set(4, [0, 1, 2]);
        parityPositionsMap.set(5, [1, 2, 3]);
        parityPositionsMap.set(6, [0, 2, 3]);
        return parityPositionsMap;
    }

    private getOppositeCharacter(value: string): string {
        return value == '1' ? '0' : '1';
    }
}