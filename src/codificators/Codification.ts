export default interface Codification {
  compress : (asciiWord: number) => string
  decompress : (codeWord: string) => string
}