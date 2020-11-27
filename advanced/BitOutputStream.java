
import java.io.*;

/** Utility class for writing individual bits to a file in a compact form.
  * 
  * The resulting file will always have a file size that is a multiple of 8 
  * (due to byte alignment, data is padded to bring it up to a whole byte).
  * 
  * @author Stuart Reges
  * @author RR
  */
public class BitOutputStream {
    
    private FileOutputStream output; // the output stream
    private int digits;     // a buffer used to build up next set of digits
    private int numDigits;  // how many digits are currently in the buffer

    private static final int BYTE_SIZE = 8;  // digits per byte

    
    /** Constructs a BitOutputStream that sends output to the specified file.
      * 
      * @param file the file to be written to.
      * @throws RuntimeException if the output file cannot be opened.
      */
    public BitOutputStream(String file) {
        try {
            output = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        digits = numDigits = 0;
    }

    
    /** Writes the specified bit to the output.
      * 
      * @param bit bit to be written.
      * @throws IllegalArgumentException if supplied bit is anything but 0 or 1.
      */
    public void writeBit(int bit) {
        if (bit < 0 || bit > 1)
            throw new IllegalArgumentException("Illegal bit: " + bit);
        digits += bit << numDigits;
        numDigits++;
        if (numDigits == BYTE_SIZE)
            flush();
    }

    
    /** Flushes the buffer.
      * 
      * If numDigits < BYTE_SIZE, this will effectively pad the output with 0s,
      * so this should be called only when numDigits == BYTE_SIZE, or when we
      * are closing the output stream.
      * 
      * @throws RuntimeException if the output file cannot be written to.
      */
    private void flush() {
        try {
            output.write(digits);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        digits = 0;
        numDigits = 0;
    }

    
    /** Flushes and closes the output stream.
      * 
      * @throws RuntimeException if the stream cannot be closed.
      */
    public void close() {
        if (numDigits > 0)
            flush();
        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }    
}
