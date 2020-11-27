import java.io.*;
import java.util.*;

/**
 * Implements a Huffman coding tree for data compression and uncompression. 
 * 
 * This class provides methods for constructing a Huffman coding tree based on 
 * a given character-frequency array, generating Huffman codes from the Huffman
 * coding tree, reconstructing a Huffman coding tree based on a bit-based 
 * tree description preamble to a Huffman-compressed file and decoding 
 * the Huffman-compressed file. 
 * 
 * @author Nguyen Hoang Nam Anh
 * 
 * Time spent: 10 hours
 * 
 */
public class HuffmanTree{

    public static final int CHAR_MAX = 256; // max char value to be encoded.
    public static final int BYTE_SIZE = 8; // // digits per byte.
    public static final int HIGHEST_POW_8_BIT = 7; // the highest power of 2 
                            // to compute when converting 8-bit binary number
                            // to a decimal number.

    private HuffmanNode root; // reference to the overall root node.


    /**
     * Represents a HuffmanNode in the Huffman coding tree.
     * Leaf nodes store ASCII characters. 
     * 
     * HuffmanNodes can be compared based on their frequencies.
     * 
     * @author Nguyen Hoang Nam Anh 
     */
    private static class HuffmanNode implements Comparable<HuffmanNode>{

        public static final int NON_DECIMAL = -1; // default decimal value of 
                                                  // a non-leaf node.
        public static final String NON_BINARY = ""; // default binary value of 
                                                    // a non-leaf node.


        public int decimalValue; // decimal value of character [0-255]. 
        public String binaryValue; // binary code of character (8-bit).
        public int frequency; // character frequency.
        public HuffmanNode left; // reference to left subtree.
        public HuffmanNode right; // reference to right subtree.


        /**
         * Converts a decimal value [0-255] to 8-bit binary string.
         * 
         * @param decimalValue the decimal value.
         * 
         * @return 8-bit binary string version.
         */
        private String toBinary8(int decimalValue){
            // computes the binary number
            String binary = Integer.toBinaryString(decimalValue);

            // computes the number of 0s to add to the front of this binary
            // number to make it an 8-bit binary number
            int digits = BYTE_SIZE - binary.length(); 
            for (int i = 0; i < digits; i++){
                binary = "0" + binary;
            }

            return binary;
        }


        /**
         * Converts an 8-bit binary string to its corresponding decimal value.
         * 
         * @param binaryValue the 8-bit binary string.
         * 
         * @return the decimal number version.
         */
        private int toDecimal(String binaryValue){
            int decimal = 0; 

            // computes the decimal from 8 bits in binaryValue
            for (int i = HIGHEST_POW_8_BIT; i >= 0; i--){
                int digit = binaryValue.charAt(HIGHEST_POW_8_BIT - i) - '0';
                decimal += digit * (int) Math.pow(2, i); 
            }

            return decimal;
        }


        /**
         * Constructs a new Huffman leaf node to store a character with its 
         * ASCII decimal - binary value and frequency.
         * 
         * Both children are set to null.
         * 
         * Useful when constructing Huffman coding tree from a given character-
         * frequency array.
         * @param decimalValue the decimal value to be stored [0-255].
         * @param frequency the frequency to be stored.
         */
        public HuffmanNode(int decimalValue, int frequency){
            this.decimalValue = decimalValue;
            this.binaryValue = toBinary8(decimalValue);
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }


        /**
         * Constructs a new HuffmanTree non-leaf node. 
         * 
         * The decimal value of this node is set to NON_DECIMAL (-1) by default.
         * The binary value of this node is set to NON_BINARY ("") by default.
         * 
         * Useful when constructing Huffman coding tree from a given character-
         * frequency array.
         * 
         * @param frequency the frequency to be stored.
         * @param left reference to left subtree.
         * @param right reference to right subtree.
         */
        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right){
            this.decimalValue = NON_DECIMAL;
            this.binaryValue = NON_BINARY;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }


        /**
         * Constructs a new HuffmanTree leaf node to store a character with its 
         * ASCII decimal & binary values.
         * 
         * Useful when reconstructing a Huffman coding tree based on a given 
         * bit-based Huffman tree description, where frequency is not important. 
         * 
         * Frequency is set to 0.
         * Both children are set to null.
         *
         * @param binaryValue the 8-bit binary string of the character.
         */
        public HuffmanNode(String binaryValue){
            this.decimalValue = toDecimal(binaryValue);
            this.binaryValue = binaryValue;
            this.frequency = 0;
            this.left = null;
            this.right = null;
        }


        /**
         * Constructs a new HuffmanTree non-leaf node.
         * 
         * Useful when reconstructing a Huffman coding tree based on a given 
         * bit-based Huffman tree description, where frequency is not important. 
         * 
         * The decimal value of this node is set to NON_DECIMAL (-1) by default.
         * The binary value of this node is set to NON_BINARY ("") by default.
         * Frequency is set to 0.
         * 
         * @param left reference to left subtree.
         * @param right reference to right subtree.
         */
        public HuffmanNode(HuffmanNode left, HuffmanNode right){
            this.decimalValue = NON_DECIMAL;
            this.binaryValue = NON_BINARY;
            this.frequency = 0;
            this.left = left;
            this.right = right;
        }


        /**
         * Returns whether other is less than, equal to, or greater than this 
         * HuffmanNode.
         * 
         * The natural ordering is determined by the frequency.
         * 
         * Useful when constructing Huffman coding tree from a given character-
         * frequency array.
         * 
         * @return the frequency difference between 2 nodes.
         */
        public int compareTo(HuffmanNode other){
            return this.frequency - other.frequency;
        }   
    }



    /**
     * Constructs a Huffman coding tree from the given character-frequency 
     * array (character-frequency based constructor).
     * 
     * @param count the character-frequency array.
     */
    public HuffmanTree(int[] count){
        buildHuffmanTree(count);
    }


    /**
     * Helper method for HuffmanTree (character-frequency based constructor).
     * Constructs a Huffman coding tree from forest of HuffmanTree leaf nodes.
     * 
     * @param count the character-frequency array.
     * @param forest the initial forest of HuffmanTree leaf nodes.
     * @return reference to overall root node of the HuffmanTree.
     */
    private void buildHuffmanTree(int[] count){
        // builds initial forest containing Huffman leaf nodes 
        Queue<HuffmanNode> forest = buildForest(count);

        // constructs a complete Huffman coding tree from forest
        while (forest.size() > 1){
            // removes 2 smallest children
            HuffmanNode childSmall = forest.remove();
            HuffmanNode childBig = forest.remove();
            // adds their parent back to forest
            HuffmanNode parent = new HuffmanNode(
                childSmall.frequency + childBig.frequency, 
                childSmall, childBig);
            forest.add(parent);
        }

        // assigns the last remaining HuffmanNode in forest 
        // to HuffmanTree overall root node
        this.root = forest.remove();
    }

    
    /**
     * Helper method for buildHuffmanTree.
     * 
     * Constructs an inital forest that stores HuffmanTree leaf nodes whose
     * frequency is larger than 0.
     * 
     * @param count the character-frequency array.
     * @return forest the initial forest containing HuffmanTree leaf nodes. 
     */
    private Queue<HuffmanNode> buildForest(int[] count){
        // creates an intial empty forest
        Queue<HuffmanNode> forest = new PriorityQueue<HuffmanNode>();

        // adds ASCII leaf nodes whose frequency is larger than 0 
        for (int value = 0; value < count.length; value++){
            if (count[value] > 0){
                forest.add(new HuffmanNode(value, count[value]));
            }
        }
        return forest;
    }


    /**
     * Generates Huffman codes for the characters stored in the tree (useful
     * when encoding characters from an input file to Huffman codes).
     * 
     * @return a 256-sized String array containing the Huffman codes.
     */
    public String[] generateCode(){
        String[] codes = new String[CHAR_MAX];
        generateCode(codes, "", this.root);
        return codes;
    }


    /**
     * Helper method for generateCode. 
     * 
     * Generates each code for each character stored in the tree.
     * 
     * @param codes the String array containing the codes.
     * @param code the the code being built for a character.
     * @param currentNode the current node being visited in the tree.
     */
    private void generateCode(String[] codes, String code, 
                        HuffmanNode currentNode){
        // once a leaf node visited, save the code to array
        if (currentNode.left == null && currentNode.right == null){
            codes[currentNode.decimalValue] = code;
            return;
        }

        // if a non-leaf node visited:
        // adds 0 to code for a left turn
        generateCode(codes, code + "0", currentNode.left);
        // adds 1 to code for a right turn
        generateCode(codes, code + "1", currentNode.right);
    }


    /**
     * Writes bit-based tree description constructed to the preamble of the 
     * compressed file. 
     * 
     * The tree description is derived from the frequency-based Huffman coding
     * tree. 
     * 
     * @param output the bit-based output stream.
     */
    public void write (BitOutputStream output){
        // writes the 32-bit binary number that stores the number of 
        // characters in the original input file. This is used to decide 
        // where to stop when decoding the compressed file.
        writeNumCharacters(output);

        // writes the bit-based tree description.
        writeTree(output);
    }


    /**
     * Helper method for write.
     * 
     * Writes the 32-bit binary number that stores the number of 
     * characters in the original input file. This is used to decide 
     * where to stop when decoding the compressed file.
     * 
     * @param output the bit-based output stream.
     */
    private void writeNumCharacters (BitOutputStream output){
        // gets the total number of characters
        int n = this.root.frequency; 

        // converts the number to 32-bit string version
        String binary32 = toBinary32(n);

        // writes to bit-based output
        for (int i = 0; i < binary32.length(); i++){
            int bit = binary32.charAt(i) - '0';
            output.writeBit(bit);
        }
    }


    /**
     * Converts the number of character (decimal) to 32-bit binary version.
     * 
     * @param n the number of characters.
     * 
     * @return the 32-bit binary version.
     */
    private String toBinary32 (int n){
        // converts n to binary number
        String binary = Integer.toBinaryString(n);
        
        // makes this binary number a complete 32-bit bin
        int digits =  4*BYTE_SIZE - binary.length(); 
        for (int i = 0; i < digits; i++){
                binary = "0" + binary;
        }

        return binary;
    }



    /**
     * Helper method for write.
     * 
     * Writes bit-based tree description to the output stream.
     * After the tree description is completed, use an extra 0 to mark end-of-
     * tree-description (end of preamble).
     * 
     * @param output the output stream. 
     */
    private void writeTree(BitOutputStream output){
        // constructs the tree description (String)
        String tree = writeTree(this.root);

        // writes the description bit by bit to output
        for (int i = 0; i < tree.length(); i++){
            int bit = tree.charAt(i) - '0';
            output.writeBit(bit);
        }

        // marks end-of-description
        output.writeBit(0); 
    }


    /**
     * Helper method for writeTree.
     * 
     * Writes the bit-based tree description. 
     * The tree nodes are traversed in post-order. 
     * 
     * Tree description format: 
     * A leaf node is represented by 1 followed by its corresponding binary 
     * value. 
     * A non-leaf node is represented by 0. 
     * @param currentNode the current node being visited.
     * 
     * @return string sequence (0s & 1s) of tree description.
     */
    private String writeTree(HuffmanNode currentNode){
        if (currentNode.left == null && currentNode.right == null){
            return "1" + currentNode.binaryValue;
        }
        else{
            String tree = writeTree(currentNode.left);
            tree += writeTree(currentNode.right);
            tree += "0";
            return tree;
        }
    }


    /**
     * Reconstructs a Huffman coding tree based on the preamble of a bit-based
     * compressed file (tree description-based constructor).
     * 
     * @param input the compressed binary input stream.
     */
    public HuffmanTree(BitInputStream input){
        buildHuffmanTree(input);
    }


    /**
     * Helper method for HuffmanTree (tree-description-based constructor).
     * 
     * @param input the compressed binary input stream.
     */
    private void buildHuffmanTree(BitInputStream input){
        // entry point: the bit-based tree description sequence

        // creates a stack to store the nodes building the HuffmanTree
        Stack<HuffmanNode> forest = new Stack<HuffmanNode>();
        
        boolean complete = false; 

        // reads the description bit by bit

        // if bit 1 encountered -> leaf node detected -> creates a new leaf 
        // node storing the corresponding character of the following 8-bit bin 

        // if bit 0 encountered --> parent of 2 nodes detected --> removes 2 
        // children node in the stack, creates a new non-leaf node parent 
        // refering to the most recent popped child to the right and the second
        // most recent child to the left (post-order traversal) --> pushes 
        // parent node back to the stack.

        // if bit 0 encountered but there is only 1 node left in the tree --> 
        // end-of-description marker detected --> take the complete tree out 
        // of the stack.

        while (!complete){
            int bit = input.readBit();
            if (bit == 0 && forest.size() < 2){
                complete = true;
            }
            else if (bit == 0 && forest.size() >= 2){
                HuffmanNode childRight = forest.pop();
                HuffmanNode childLeft = forest.pop();

                HuffmanNode parent = new HuffmanNode(childLeft, childRight);
                forest.push(parent);

            }
            else if (bit == 1){
                HuffmanNode leafNode = new HuffmanNode(getBinaryValue(input));
                forest.push(leafNode);
            }
        }

        this.root = forest.pop();
    }


    /**
     * Helper method for building a leaf node in buildHuffmanTree. 
     * 
     * When bit 1 is encountered (outside this method), this methods reads 
     * for the next 8 bit and returns an 8-bit binary string representing 
     * an ASCII character.
     * 
     * @param input the compressed binary input stream.
     * 
     * @return 8-bit binary value of the corresponding leaf node. 
     */
    private String getBinaryValue(BitInputStream input){
        String binaryString = "";
        for (int i = 0; i < BYTE_SIZE; i++){
            int bit = input.readBit();
            binaryString += bit;
        }
        return binaryString;
    }


    /**
     * Reads the individual bits from the input stream and writes the 
     * corresponding characters to the supplied output stream.
     * 
     * It stops reading when a character with the number of decoded characters 
     * equals to the total number of characters in the original input file 
     * (before compression). 
     * 
     * @param input the supplied input stream of compressed Huffman code.
     * @param output the supplied output stream. 
     * @param eof the pseudo-eof value (the total number of chars in the
     * original input file.)
     */
    public void decode (BitInputStream input, PrintStream output, int eof){
        int numdDecoded = 0; 
        while (numdDecoded < eof){
            output.write(decode(input, this.root));
            numdDecoded++;
        }
    }

    
    /**
     * Helper method for decode.
     * 
     * Decodes an original character from a sequence of bits in input stream.
     * 
     * @param input the supplied input stream of compressed Huffman code.
     * @param currentNode the current node traversed to.
     * @return the value of the leaf node corresponding to the next sequence
     * of bits from input.
     */
    private int decode (BitInputStream input, HuffmanNode currentNode){
        // if leaf node reached, output its ASCII value
        if (currentNode.left == null && currentNode.right == null){
            return currentNode.decimalValue;
        }

        // if non-leaf node reached, read next bit to get next turn info
        // if next bit is 0, turn left
        if (input.readBit() == 0){
            return decode(input, currentNode.left);
        } 
        // if next bit is 1, turn right
        else{
            return decode(input, currentNode.right);
        }
    }  
}

