
import java.io.*;
import java.util.*;

/** 
  * Program that uncompresses a specified file, assuming it was compressed using
  * the Huff program.
  * 
  * @author RR
  * @author Nguyen Hoang Nam Anh
  */
public class Unhuff {
    
    public static final int CHAR_MAX = 256;  // max char value to be encoded
    public static final int BYTE_SIZE = 8; // digits per byte
    public static final int HIGHEST_POW_32_BIT = 31; // the highest power of 2 
                            // to compute when converting 32-bit binary number
                            // to a decimal number.

    /**
     * Reads the first 32-bit sequence in the preamble of the compressed input
     * file to get the number of characters that need decoding.
     * @param input the compressed file.
     * @return the decimal number of characters that need decoding. 
     */
    private static int toDecimal (BitInputStream input){
        String binary32 = getBinary32(input);
        return toDecimal(binary32);
    }

    /**
     * Helper method for toDecimal. 
     * 
     * Extracts the 32-bit sequence storing the number of decoded characters 
     * from the preamble. 
     * 
     * @param input the compressed file.
     * @return the 32-bit binary string. 
     */
    private static String getBinary32 (BitInputStream input){
        String binary32 = "";
        for (int i = 0; i < 4*BYTE_SIZE; i++){
            binary32 += input.readBit();
        }
        return binary32;
    }

    /**
     * Helper method for toDecimal. 
     * Turns the 32-bit binary string into decimal number. 
     * 
     * @param binary32 the 32-bit binary string
     * @return the decimal number of decoded characters. 
     */
    private static int toDecimal (String binary32){
        int decimal = 0;
        for (int i = HIGHEST_POW_32_BIT; i >= 0; i--){
            int digit = binary32.charAt(HIGHEST_POW_32_BIT - i) - '0';
            decimal += digit * (int) Math.pow(2, i);
        }
        return decimal;
    }
    
    /** Uncompresses the contents of the user specified .huff file.
      * 
      * The user supplies the file to be uncompressed via the console. The 
      * uncompressed contents are written to a file with the same name as
      * the original, but with the .unhuff extension.
      * 
      * @param args ignored.
      * @throws IOException if the user supplied file cannot be opened.
      */
    public static void main(String[] args) throws IOException {
        
        // Get file name from the user
        System.out.print("Please enter name of file to be unhuffed "
                             + "(file name must end with .huff): ");
        Scanner console = new Scanner(System.in);                
        
        // Fetch the name of the file to be unhuffed
        String inputFileName = console.next();
        console.close();
        Scanner tokenizer = new Scanner(inputFileName);
        tokenizer.useDelimiter("\\.");
        String fileStem = tokenizer.next();
        tokenizer.close();
        
        // Open output, decode
        BitInputStream input = new BitInputStream(inputFileName);
        PrintStream output = new PrintStream(new File(fileStem + ".unhuff"));
        int eof = toDecimal(input);
        HuffmanTree tree = new HuffmanTree(input);
        tree.decode(input, output, eof);
        input.close();
        output.close();        
    }
    
}

