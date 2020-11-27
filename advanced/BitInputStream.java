import java.io.*;

/** Utility class for reading individual bits from a file.
  * 
  * @author Stuart Reges
  * @author RR
  */
public class BitInputStream {
    private FileInputStream input; // the input stream
    private int digits;     // next set of digits (buffer)
    private int numDigits;  // how many digits from buffer have been used

    private static final int BYTE_SIZE = 8;  // digits per byte
    private static final int EOF = -1; // end-of-file marker
    
    
    /** Constructs a BitInputStream that reads input from the specified file.
      * 
      * @param file the file to be read from.
      * @throws RuntimeException if the input file cannot be opened.
      */
    public BitInputStream(String file) {
        try {
            input = new FileInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        nextByte();
    }

    
    /** Returns the next bit read from the input stream.
      * 
      * A special value of -1 indicates the end of file.
      * 
      * @return the next bit read from the input stream.
      */
    public int readBit() {
        if (digits == EOF)
            return EOF;
        int result = digits % 2;
        digits /= 2;
        numDigits++;
        if (numDigits == BYTE_SIZE)
            nextByte();
        return result;
    }


    /** Refreshes the internal buffer with the next BYTE_SIZE bits.
      * 
      * @throws RuntimeException if the input stream cannot be read.
      */
    private void nextByte() {
        try {
            digits = input.read();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        numDigits = 0;
    }

    
    /** Closes the input stream.
      * 
      * @throws RuntimeException if the input stream cannot be closed.
      */
    public void close() {
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }    
}
