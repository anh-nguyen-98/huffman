public class HuffmanNodeClient{

    public static String toBinary8 (int n){
        String binary = toBinary(n);
        int digits = 8 - binary.length(); // digits: the number of 0s to add to the beginning of the string binary to become full 8-bit binary string.
        for (int i = 0; i < digits; i++){
            binary = "0" + binary;
        }
        return binary;
    }

    private static String toBinary (int n){
        if (n == 0){
            return "0";
        }
        if (n == 1){
            return "1";
        }
        return toBinary(n/2) + n%2;   
    } 

    public static int toDecimal (String binary){
        int decimal = 0; 
        for (int i = 7; i >= 0; i--){
            int digit = binary.charAt(7-i) - '0';
            decimal += digit * (int) Math.pow(2, i); 
        }
        return decimal;
    }   
    public static void main(String[] args){   
        System.out.println(toBinary8(103));
        System.out.println(toDecimal(toBinary8(103)));
        // int x = '1' - '0';
        // System.out.println(x);
        HuffmanNode g = new HuffmanNode(111, 1);
        //System.out.print(g);
        HuffmanNode o = new HuffmanNode("01101111");
        HuffmanNode sum = new HuffmanNode(1, g, o);
        System.out.println(sum);

    }
}