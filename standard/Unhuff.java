
import java.io.*;
import java.util.*;

/** Program that uncompresses a specified file, assuming it was compressed using
  * the Huff program.
  * 
  * @author RR
  */
public class Unhuff {
    
    public static final int CHAR_MAX = 256;  // max char value to be encoded
    
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
        
        // open code file and construct tree
        Scanner codeInput = new Scanner(new File(fileStem + ".code"));
        HuffmanTree tree = new HuffmanTree(codeInput);
        
        // open encoded file, open output, decode
        BitInputStream input = new BitInputStream(inputFileName);
        PrintStream output = new PrintStream(new File(fileStem + ".unhuff"));
        tree.decode(input, output, CHAR_MAX);
        input.close();
        output.close();        
    }
    
}

