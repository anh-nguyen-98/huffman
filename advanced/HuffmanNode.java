/**
 * Represents HuffmanNode in the Huffman coding tree. 
 * 
 * @author Nguyen Hoang Nam Anh 
 */
public class HuffmanNode implements Comparable<HuffmanNode>{

    public int decimalValue;
    public String binaryValue;
    public int frequency;
    public HuffmanNode left;
    public HuffmanNode right;

    public static final int NON_DECIMAL = -1;
    public static final String NON_BINARY = "";

    /**
     * Convert from ASCII decimal value to 8-bit binary string.
     * @param n decimal ASCII value (0 <= n < 256)
     * 
     * @return String 8-bit binary value 
     */
    private String toBinary8 (int decimalValue){
        String binary = toBinary(decimalValue);
        int digits = 8 - binary.length(); // digits: the number of 0s to add to the beginning of the string binary to become full 8-bit binary string.
        for (int i = 0; i < digits; i++){
            binary = "0" + binary;
        }
        return binary;
    }

    private String toBinary (int decimalValue){
        if (decimalValue == 0){
            return "0";
        }
        if (decimalValue == 1){
            return "1";
        }
        return toBinary(decimalValue/2) + decimalValue%2;   
    }

    /**
     * Converts from 8-bit binary to ASCII decimal value
     * @param binary 8-bit binary String
     * @return decimal ASCII number
     */
    private int toDecimal (String binaryValue){
        int decimal = 0; 
        for (int i = 7; i >= 0; i--){
            int digit = binaryValue.charAt(7 - i) - '0';
            decimal += digit * (int) Math.pow(2, i); 
        }
        return decimal;
    }
    
    /**
     * Constructs ASCII leaf-node for HuffmanTree constructor 1 (frequency-based constructor)
     */
    public HuffmanNode(int decimalValue, int frequency){
        this.decimalValue = decimalValue;
        this.binaryValue = toBinary8(decimalValue);
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    /**
     * Constructs non-leaf node for HuffmanTree constructor 1 (frequency-based constructor)
     */
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right){
        this.decimalValue = NON_DECIMAL;
        this.binaryValue = NON_BINARY;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    /**
     * Constructs ASCII Huffman leaf node for HuffmanTree constructor 2 (bit-based constructor)
     * (Post-traversal bit-based tree description)
     * 
     * Frequency does not matter in this construction.
     */
    public HuffmanNode(String binaryValue){
        this.decimalValue = toDecimal(binaryValue);
        this.binaryValue = binaryValue;
        this.frequency = 0;
        this.left = null;
        this.right = null;
    }

    /**
     * Constructs Huffman non-leaf node for HuffmanTree constructor 2 (bit-based constructor)
     * (Post-traversal bit-based tree description)
     */
    public HuffmanNode(HuffmanNode left, HuffmanNode right){
        this.decimalValue = NON_DECIMAL;
        this.binaryValue = NON_BINARY;
        this.frequency = 0;
        this.left = left;
        this.right = right;
    }

    /**
     * Compares 2 HuffmanNodes based on their frequency. 
     * Used for HuffmanTree constructor 1. 
     */
    public int compareTo (HuffmanNode other){
        return this.frequency - other.frequency;
    }

    public String toString(){
        return "decimal: " + this.decimalValue + " - binary: " + this.binaryValue + " - frequency: " + this.frequency;
    }


}