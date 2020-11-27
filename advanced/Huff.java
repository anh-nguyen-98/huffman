
import java.io.*;
import java.util.*;

/**
  * Program that uses Huffman encoding to compress the contents of a supplied 
  * input file.
  * 
  * @author RR
  * @author Nguyen Hoang Nam Anh 
  */
public class Huff {
    
    // Class constants
    private static final int CHAR_MAX = 256;
    private static final int EOF = -1;
    
    // Fields for various file names
    private static String inputFileName;                                  
    private static String outputFileName;  
    
    /** Sets the various global filenames.
      * 
      * In particular, the supplied filename is stemmed to produce the huffed
      * file name.
      * 
      * @param fileName the name of the file to be compressed.          
      */
    private static void setFileNames(String fileName) {                        
        inputFileName = fileName;
        Scanner tokenizer = new Scanner(inputFileName);
        tokenizer.useDelimiter("\\.");
        String fileStem = tokenizer.next(); 
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
    private static int[] countCharacterFrequencies(FileInputStream inputStream) 
                                              throws IOException {
        int[] count = new int[CHAR_MAX];
        int n = inputStream.read(); 
        while (n != EOF) {
            count[n]++;
            n = inputStream.read();
        }
        inputStream.close();
        return count;
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
      * @param outputStream the bit-based output stream which has been opened. 
      * @throws IOException if the input or output files cannot be opened.
      */
    private static void encode(String[] codes, BitOutputStream outputStream) 
                        throws IOException {
        // reopens the inputStream that has been closed after counting 
        // character frequencies to produce codes.                   
        FileInputStream inputStream = new FileInputStream(inputFileName);
        
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
        FileInputStream inputStream = new FileInputStream(inputFileName);

        int[] count = countCharacterFrequencies(inputStream);
        
        // Build Huffman tree
        HuffmanTree tree = new HuffmanTree(count);
        
        // Open output file and write tree description
        BitOutputStream outputStream = new BitOutputStream(outputFileName);
        tree.write(outputStream); 
        
        // Record codes
        String[] codes = tree.generateCode();
        
        // Encode the input file
        encode(codes, outputStream);
    }
    
}

