import java.io.*;
import java.util.*;

public class HuffmanTreeClient{
    public static int[] countCharacterFrequencies() throws IOException {
        FileInputStream inputStream = new FileInputStream("gophers.txt");
        int[] count = new int[256];
        int n = inputStream.read();
        while (n != -1) {
            count[n]++;
            n = inputStream.read();
        }
        inputStream.close();
        return count;
    }
    public static String getCharacter(String n){
        String binaryString = "";
        for (int i = 0; i < 8; i++){
            char bit = n.charAt(i);
            binaryString += bit;
        }
        return binaryString;
    } 


    public static void main(String[] args) throws IOException{
        int[] count = countCharacterFrequencies();
        HuffmanTree test = new HuffmanTree(count);
        System.out.println(test.root);
        System.out.println(test.root.right.right.right.right);
        System.out.println(test.root.frequency);
        System.out.println(test.toBinary32(test.root.frequency));
        // System.out.println(test.writeTree());
        // test.printTree();
        System.out.println(test.root.left.left.binaryValue);
        System.out.println(getCharacter(test.root.left.left.binaryValue));

        // String treeCode = test.writeTree(test.root);
        // treeCode += "0";
        // System.out.println(treeCode);

        // HuffmanTree test2 = new HuffmanTree(treeCode);
        // String treeCode2 = test2.writeTree(test2.root);
        // treeCode2 += "0";
        // System.out.println(treeCode2);

        // System.out.println(treeCode.equals(treeCode2));
        System.out.println();
        String[] testCodes = test.generateCode();

        //System.out.println(Arrays.toString(testCodes));
        for (String code: testCodes){
            if (code != null){
                System.out.println(code);
            }
        }
        


        
    }
}