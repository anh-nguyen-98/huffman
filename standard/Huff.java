
import java.io.*;
import java.util.*;

/** Program that uses Huffman encoding to compress the contents of a supplied 
  * input file.
  * 
  * @author RR
  */
public class Huff {
    
    // Class constants
    private static final int CHAR_MAX = 256;
    private static final int EOF = -1;
    
    // Fields for various file names
    private static String inputFileName;
    private static String codeFileName;
    private static String outputFileName;
    
    
    /** Sets the various global filenames.
      * 
      * In particular, the supplied filename is stemmed to produce the huffed
      * file name and the code file name.
      * 
      * @param fileName the name of the file to be compressed.
      */
    private static void setFileNames(String fileName) {
        inputFileName = fileName;
        Scanner tokenizer = new Scanner(inputFileName);
        tokenizer.useDelimiter("\\.");
        String fileStem = tokenizer.next();
        codeFileName = fileStem + ".code";
        outputFileName = fileStem + ".huff";
        tokenizer.close();
    }
    
    
    /** Returns the frequency if each character in the input file.
      * 
      * Input file name is obtained from the global variable inputFileName.
      * 
      * @throws IOException if supplied file cannot be opened.
      * @return an int array containing the frequencies of various ASCII
      *         characters.
      */
    private static int[] countCharacterFrequencies() throws IOException {
        FileInputStream inputStream = new FileInputStream(inputFileName);
        int[] count = new int[CHAR_MAX];
        int n = inputStream.read();
        while (n != EOF) {
            count[n]++;
            n = inputStream.read();
        }
        inputStream.close();
        return count;
    }
    
    
    /** Returns the codes defined in the code file.
      * 
      * The code file name is obtained from the global variable codeFileName.
      * 
      * @throws IOException if the code file cannot be opened.
      * @return a String array containing the codes read from the file.
      */
    private static String[] readCodes() throws IOException {
        
        String[] codes = new String[CHAR_MAX + 1];
        Scanner codeInput = new Scanner(new File(codeFileName));
        while (codeInput.hasNextLine()) {
            int n = Integer.parseInt(codeInput.nextLine());
            codes[n] = codeInput.nextLine();
        }
        
        return codes;
    }
    
    
    /** Writes the supplied code to the supplied stream.
      * 
      * The supplied stream is assumed to be already open, and the string s
      * is assumed to be composed of only 0s and 1s.
      * 
      * @param s the 0-1 code to be written.
      * @param output the output stream to which the data is to be written.
      */
    private static void writeString(String s, BitOutputStream output) {
        for (int i = 0; i < s.length(); i++)
            output.writeBit(s.charAt(i) - '0');
    }
    
    
    /** Compresses the data from the input file using codes and writes the
      * compressed data to the output file.
      * 
      * The input file is specified via the global variable inputFileName and
      * the output file is specified via the global varaible outputFileName.
      * 
      * @param codes the Huffman codes to use for compressing the input data.
      * @throws IOException if the input or output files cannot be opened.
      */
    private static void encode(String[] codes) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputFileName);
        BitOutputStream outputStream = new BitOutputStream(outputFileName);
        
        // Encode the input file, one byte at a time
        int n = inputStream.read();
        while (n != EOF) {
            if (codes[n] == null) {
                System.out.println("Your code file has no code for " + n +
                                   " (the character '" + (char)n + "')");
                System.out.println("Exiting...");
                System.exit(-1);
            }
            writeString(codes[n], outputStream);
            n = inputStream.read();
        }
        
        // Write the code for the pseudo-EOF
        writeString(codes[CHAR_MAX], outputStream);
        
        // Clean up
        inputStream.close();
        outputStream.close();        
    }
    
    
    /** Main driver method. */
    public static void main(String[] args) throws IOException {
        
        // Prompt for user input
        System.out.print("Please enter the name of the file to be huffed: ");
        Scanner console = new Scanner(System.in);
        
        // Set the various file names using the command line parameter        
        setFileNames(console.next());
        console.close();
        
        // Open input file and count character frequencies
        int[] count = countCharacterFrequencies();
        
        // Build Huffman tree
        HuffmanTree tree = new HuffmanTree(count);
        
        // Write code file
        PrintStream codeStream = new PrintStream(new File(codeFileName));
        tree.write(codeStream);
        codeStream.close();
        
        // Open code file and record codes
        String[] codes = readCodes();
        
        // Encode the input file
        encode(codes);
    }
    
}

