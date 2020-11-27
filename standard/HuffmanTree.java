import java.io.*;
import java.util.*;

/**
 * Implements a Huffman coding tree for data compression and uncompression. 
 * 
 * This class provides methods for constructing Huffman coding tree based on a
 * given character-frequency array, outputing the generated Huffman tree, 
 * reconstructing Huffman coding tree based on a tree description file and 
 * decoding a Huffman-compressed file.  
 * 
 * @author Nguyen Hoang Nam Anh
 * Time spent: 12 hours
 */
public class HuffmanTree{
    
    private HuffmanNode root;   // reference to the overall root node.

    /**
     * Represents a HuffmanNode in the Huffman coding tree.
     * Leaf nodes store ASCII and pseudo-eof characters. 
     * 
     * HuffmanNodes can be compared based on their frequencies.
     * @author Nguyen Hoang Nam Anh 
     */
    private static class HuffmanNode implements Comparable<HuffmanNode> {

        public static final int NON_VALUE = -1; // default value of a non-leaf
                                                // node.

        public int value; // character ASCII value. 
        public int frequency; // character frequency.
        public HuffmanNode left; // reference to left subtree.
        public HuffmanNode right; // reference to right subtree.


        /** 
         * Constructs a new Huffman leaf node to store a character with its 
         * ASCII value and frequency.
         * Both children are set to null.
         * 
         * Useful when constructing Huffman coding tree from a given character-
         * frequency array.
         * 
         * @param value the value to be stored.
         * @param frequency the frequency to be stored.
         */
        public HuffmanNode(int value, int frequency){
            this.value = value;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }

        /**
         * Constructs a new HuffmanTree non-leaf node. 
         * The value of this node is set to NON_VALUE (-1) by default.
         * 
         * Useful when constructing Huffman coding tree from a given character-
         * frequency array.
         * 
         * @param frequency the frequency to be stored.
         * @param left reference to left subtree.
         * @param right reference to right subtree. 
         */
        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right){
            this.value = NON_VALUE;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        /**
         * Constructs a new HuffmanTree leaf node to store a character with its 
         * ASCII value.
         * 
         * Useful when reconstructing Huffman coding tree based on a given tree
         * description file, where frequency is not important. 
         * 
         * Frequency is set to 0.
         * Both children are set to null.
         * 
         * @param value the ASCII value to be stored.
         */
        public HuffmanNode(int value){
            this.value = value;
            this.frequency = 0;
            this.left = null;
            this.right = null;
        }

        /**
         * Constructs a new HuffmanTree non-leaf node.
         * 
         * Useful when reconstructing a Huffman coding tree based on a tree
         * description file, where frequency is not important. 
         */
        public HuffmanNode(){
            this(NON_VALUE);
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

        // constructs an entire Huffman coding tree from forest
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

        // adds leaf node storing pseudo-eof character
        HuffmanNode eof = new HuffmanNode(count.length, 1);
        forest.add(eof);

        return forest;
    }

    /**
     * Writes the Huffman tree to the supplied output stream.
     * @param output the output stream.
     */
    public void write(PrintStream output){
        write(output, "", this.root);
    }

    /**
     * Helper method for write.
     * 
     * Writes the generated Huffman code corresponding to each leaf node.
     * 
     * @param output the output stream.
     * @param code the being-built code.
     * @param currentNode the current node traversed in the tree.
     */
    private void write(PrintStream output, String code, 
                HuffmanNode currentNode){

        // writes its ASCII value and code once reaching the leaf node
        if (currentNode.left == null && currentNode.right == null){
            output.println(currentNode.value);
            output.println(code);       
            return;
        }

        // adds 0 to the code for one left turn
        write(output, code + "0", currentNode.left);

        // adds 1 to the code for one right turn
        write(output, code + "1", currentNode.right);
    }


    /**
     * Constructs a Huffman coding tree based on a tree description file (
     * tree description-based constructor).
     * 
     * @param input the supplied input stream that stores the tree description.
     */
    public HuffmanTree(Scanner input){
        this.root = new HuffmanNode();
        while (input.hasNextLine()){
            // reads each pair of ASCII value - Huffman code 
            int n = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            // updates the branch from overall root node to the leaf node
            this.root = buildHuffmanTree(this.root, code, n);
        }
    }

    /**
     * Helper method for HuffmanTree (tree-description-based constructor).
     * 
     * Builds a HuffmanTree based on each leaf node.
     * 
     * @param currentNode the current node traversed to.
     * @param code the Huffman code directing to the correspoding leaf node.
     * @param value the ASCII value to be stored in the leaf node.
     * 
     * @return the branch from overall root node to the leaf node.
     */
    private HuffmanNode buildHuffmanTree(HuffmanNode currentNode, String code, 
                                    int value){
        // if leaf node reached: 
        if (code.length() == 0){
            currentNode = new HuffmanNode(value);
        }

        // if non-leaf node reached: 
        else{
            if (currentNode == null){
                currentNode = new HuffmanNode();
            }
            // gets next turn info
            char turn = code.charAt(0);
            // turns left
            if (turn == '0'){
                currentNode.left = buildHuffmanTree(currentNode.left, 
                                                code.substring(1), value);
            }
            // turns right
            else{
                currentNode.right = buildHuffmanTree(currentNode.right, 
                                                code.substring(1), value);
            }
        }
        return currentNode;
    }

    /**
     * Reads the individual bits from the input stream and writes the 
     * corresponding characters to the supplied output stream.
     * 
     * It stops reading when a character with a value equal to the eof 
     * parameter is encountered.
     * 
     * @param input the supplied input stream of compressed Huffman code.
     * @param output the supplied output stream. 
     * @param eof the pseudo-eof character value.
     */
    public void decode(BitInputStream input, PrintStream output, int eof){
        // returns the value of each corresponding character
        int value = decode(input, this.root);

        // stops writing the character value to output stream when eof is 
        // encountered.
        while (value != eof){
            output.write(value);
            value = decode(input, this.root);
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
            return currentNode.value;
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