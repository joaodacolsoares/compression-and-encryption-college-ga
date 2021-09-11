import Codification from "./Codification";

export default class Fibonacci implements Codification {
  k : number = 4

  compress(asciiWord: number) : string {
    const fibonacciNumbers = this.calculateFibonacciUntilNumber(asciiWord)
    return this.calculateCodeWord(asciiWord, fibonacciNumbers);
  }

  private calculateFibonacciUntilNumber(objective: number, current = [1]) : number[] {
    const index = current.length;

    if(current[index - 1] >= objective) {
      current.pop()
      return current
    }    

    if(index == 1) {
      current.push(2)
    } else {
      current.push(current[index - 1] + current[index - 2])
    }


    return this.calculateFibonacciUntilNumber(objective, current)
  }

  private calculateCodeWord(objective: number, fibonacciNumbers: number[]) : string {
    let objectiveTotal : number = objective;
    return fibonacciNumbers.slice().reverse().map(number => {
      console.log(objectiveTotal, number, objectiveTotal >= number)
      if(objectiveTotal >= number) {
        objectiveTotal -= number
        return '1'
      }
      return '0'
    }).reverse().join('')
  }

  decompress(codeWord: string) : string {
    return ''
  }
}