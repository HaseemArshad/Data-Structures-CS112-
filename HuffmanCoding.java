package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

	/* Your code goes here */
    sortedCharFreqList = new ArrayList<>();
   StdIn.setFile(fileName);
   int[] frequencies = new int[128];
   int count=0;
   while (StdIn.hasNextChar()){
       char tracker = StdIn.readChar();
       frequencies[tracker]++;
       count++;
   }
   for (char i=(char)0;i<frequencies.length;i++){
       if (frequencies[i]!=0){
           CharFreq charfreq = new CharFreq(i,(double)frequencies[i]/count);
           sortedCharFreqList.add(charfreq);
       }
   }
   if (sortedCharFreqList.size()==1){
       CharFreq add = new CharFreq((char)((sortedCharFreqList.get(0).getCharacter()+1)%128), 0.0);
       sortedCharFreqList.add(add);
   }
   Collections.sort(sortedCharFreqList);
   return;
}



    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

	/* Your code goes here */

 Queue<TreeNode> start = new Queue<>();
 Queue<TreeNode> target = new Queue<>();
 Queue<TreeNode> temporary = new Queue<>();


     for (int i = 0; i < sortedCharFreqList.size(); i++) {


         TreeNode leaf = new TreeNode(sortedCharFreqList.get(i), null, null);
         start.enqueue(leaf);
     }


     while(!start.isEmpty() || !target.isEmpty()) {


         if (temporary.size() == 2) {


             TreeNode a = temporary.dequeue();
             TreeNode b = temporary.dequeue();


             CharFreq data = new CharFreq(null, a.getData().getProbOcc() + b.getData().getProbOcc());


             if (a.getData().getProbOcc() <= b.getData().getProbOcc()) {


                 TreeNode half = new TreeNode(data, a, b);
                 target.enqueue(half);
             } else {


                 TreeNode half = new TreeNode(data, b, a);
                 target.enqueue(half);
             }
         } else if (target.isEmpty()) {
             temporary.enqueue(start.dequeue());
         } else if(start.isEmpty()) {
             temporary.enqueue(target.dequeue());
         } else if (start.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()) {
             temporary.enqueue(start.dequeue());
         } else {
             temporary.enqueue(target.dequeue());
         }
     }


     if (temporary.size() == 2) {


         TreeNode a = temporary.dequeue();
         TreeNode b = temporary.dequeue();


         CharFreq data = new CharFreq(null, a.getData().getProbOcc() + b.getData().getProbOcc());


         if (a.getData().getProbOcc() <= b.getData().getProbOcc()) {


             TreeNode half = new TreeNode(data, a, b);
             huffmanRoot = half;
             return;
         } else {


             TreeNode half = new TreeNode(data, b, a);
             huffmanRoot = half;
             return;
         }
     } else {


         huffmanRoot = temporary.dequeue();
         return;
     }
 }




    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

	/* Your code goes here */
    encodings = new String[128];
   traverse(huffmanRoot, "");
     }
private void traverse(TreeNode root, String a) {




   if (root.getLeft() == null && root.getRight() == null) {
       char data = root.getData().getCharacter();
       int track = (int) data;
       encodings[track] = a;
       return;
   }
   traverse(root.getLeft(), a + "0");
   traverse(root.getRight(), a + "1");
 
}
    

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	/* Your code goes here */
    String a = "";


   while (StdIn.hasNextChar()) {


       int count = (int) StdIn.readChar();
       a = a + encodings[count];
   }


   writeBitString(encodedFile, a);
}

    
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	/* Your code goes here */
    String readBitString = readBitString(encodedFile);
   TreeNode marker = huffmanRoot;
   for (int i = 0; i < readBitString.length(); i++) {
       if (marker.getData().getCharacter() != null) {
           StdOut.print(marker.getData().getCharacter());
           marker = huffmanRoot;
       }
           char temporary = readBitString.charAt(i);
           if (temporary == '0') {
               marker = marker.getLeft();
           } else {
               marker = marker.getRight();
           }
   }
   StdOut.print(marker.getData().getCharacter());
}





    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
