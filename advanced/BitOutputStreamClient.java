import java.io.*;
import java.util.*;

public class BitOutputStreamClient{
    private static int[] countCharacterFrequencies(FileInputStream inputStream) throws IOException {
        int[] count = new int[256];
        int n = inputStream.read(); // inputStream read one character in file and convert to the corresponding ASCII value integer n
        while (n != -1) {
            count[n]++;
            n = inputStream.read();
        }
        return count;
    }
    public static final int BYTE_SIZE = 8;

    public static int toDecimal (BitInputStream input){
        String binary32 = getBinary32(input);
        return toDecimal(binary32);
    }

    private static String getBinary32 (BitInputStream input){
        String binary32 = "";
        for (int i = 0; i < 4*BYTE_SIZE; i++){
            binary32 += input.readBit();
        }
        return binary32;
    }

    private static int toDecimal (String binary32){
        int decimal = 0;
        for (int i = 31; i >= 0; i--){
            int digit = binary32.charAt(31 - i) - '0';
            decimal += digit * (int) Math.pow(2, i);
        }
        return decimal;
    }
    public static void main (String[] args) throws IOException{
        FileInputStream inputStream = new FileInputStream("gophers.txt");
        BitOutputStream outputStream = new BitOutputStream("gophers.huff");
        int[] count = countCharacterFrequencies(inputStream);
        HuffmanTree tree = new HuffmanTree(count);
        tree.write(outputStream); 
        
        BitInputStream test = new BitInputStream("gophers.huff");
        //System.out.println(toDecimal(test));
        // for (int i = 0; i < 1000; i++){
        //     System.out.print(test.readBit());
        // }
        String binary = getBinary32(test);
        System.out.println(binary);
        System.out.println(binary.length());
        System.out.print(toDecimal(binary));
        



    }
}