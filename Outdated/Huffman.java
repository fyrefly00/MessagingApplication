
/**************************
 * Huffman.java	Author: Robert Walker
 * 
 * Purpose: Huffman Code 
 ****************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;


public class Huffman {
	public static int[] freq; // Stores frequencies
	public static String[] huffCodes; // Code table
	public static PriorityQueue<Node> queue;

	public static void main(String[] args) throws Exception {
		freq = new int[256];
		huffCodes = new String[256]; 
		String message =readFile(); // Read file && count frequencies
		buildQueue(); // Make the queue
		buildCodeTable(queue.peek(), ""); // Populate code table
		String hold = encode();
		System.out.println();
        System.out.println();

        DatagramSocket ds = new DatagramSocket(); 
        DatagramSocket ds2 = new DatagramSocket();
        byte[] receive = new byte[65535]; 
        InetAddress address = InetAddress.getByName("10.10.2.15");
        HuffmanPacket packet = new HuffmanPacket(hold, queue, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(packet);
        oos.flush();
        byte[] buf = bos.toByteArray();
        DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, address, 1234);
        ds.send(messagePacket);


        HuffmanPacket packet2 = new HuffmanPacket(hold, null, false);
        oos.writeObject(packet2);
        oos.flush();
        buf = bos.toByteArray();
        DatagramPacket messagePacket2 = new DatagramPacket(buf, buf.length, address, 1234);
        ds.send(messagePacket2);

        HuffmanPacket packet3 = new HuffmanPacket("aaaa", null, false);
        oos.writeObject(packet3);
        oos.flush();
        buf = bos.toByteArray();
        DatagramPacket messagePacket3 = new DatagramPacket(buf, buf.length, address, 1234);
        ds2.send(messagePacket3);
    //     HuffmanPacket unencodedPacket = new HuffmanPacket("hello", null, false);
    //     ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
    //     ObjectOutputStream oos2 = new ObjectOutputStream(bos);
    //    oos2.writeObject(unencodedPacket);
    //    oos2.flush();
    //    byte[] buf2 = bos2.toByteArray();
    //    DatagramPacket messagePacket2 = new DatagramPacket(buf2, buf2.length, address, 1234);
    //    ds.send(messagePacket2);

        // decode(hold);
        
	}

	// Build the queue given the frequencies
	public static void buildQueue() {
		queue = new PriorityQueue<Node>();
		for (int i = 0; i < freq.length; i++) { // Populate the queue
			if (freq[i] != 0) {
				queue.add(new Node(freq[i], (char) i));
			}
		}
		while (queue.size() > 1) { // Poll and stick to make one big queue
			Node holder1 = queue.poll();
			Node holder2 = queue.poll();
			char c = '~';
			Node parent = new Node(holder1.freq + holder2.freq, c);
			parent.leftChild = holder1;
			parent.rightChild = holder2;
			queue.add(parent);
		}
	}

	// Recursively build the Code Table
	public static void buildCodeTable(Node inNode, String inString) {
		Node temp = inNode;
		String str = inString;
		if (temp.leftChild == null && temp.rightChild == null) { // If leaf
			huffCodes[(int) temp.value] = str;
			str = "";
			return;
		}
		if (temp.leftChild != null) {
			buildCodeTable(temp.leftChild, str + "0");
		}
		if (temp.rightChild != null) {
			buildCodeTable(temp.rightChild, str + "1");
		}
	}

	private static void inOrder(Node inNode) {
		Node temp = inNode;

		if (temp == null) { // Base Case
			System.out.print("");
		}
		if (temp.leftChild != null) { // Recursive statement #1
			inOrder(temp.leftChild);
		}
		System.out.print(temp.value + " " + temp.freq + " ");
		if (temp.rightChild != null) { // Recursive statement #2
			inOrder(temp.rightChild);
		}
	}

	public static String readFile() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("huffin.txt"));
		String str = "";
		while (scan.hasNext()) {
			String hold = scan.nextLine();
			str = str + hold + "\n";

		}
		System.out.println(str);
		for (int i = 0; i < str.length(); i++) { // Count the frequencies
			char c = str.charAt(i);
			freq[(int) c]++;

        }
        return str;
	}

	// public static class Node implements Comparable<Node> {
	// 	Node leftChild;
	// 	Node rightChild;
	// 	int freq;
	// 	char value;

	// 	public Node(int inFreq, char inValue) {
	// 		freq = inFreq;
	// 		value = inValue;
	// 	}

	// 	public int compareTo(Node inNode) {
	// 		if (inNode.freq > freq) {
	// 			return -1;
	// 		} else if (inNode.freq < freq) {
	// 			return 1;
	// 		} else {
	// 			return 0;
	// 		}
	// 	}

	// }

	// Encode given file
	public static String encode() throws FileNotFoundException {
		int currLength = 0;
		Scanner scan = new Scanner(new File("huffin.txt"));
		String str = "";
		String finalResult = "";
		while (scan.hasNext()) { // Scan the input file, taking new lines into account
			String hold = scan.nextLine();
			str = str + hold + "\n";
		}
		for (int i = 0; i < str.length(); i++) { // Encode using code table
			char c = str.charAt(i);
			finalResult = finalResult + huffCodes[(int) c];

		}

		for (int i = 0; i < finalResult.length(); i++) { // Limit the output to 40 chars per line
			if (currLength < 40) {
				System.out.print(finalResult.substring(i, i + 1));
				currLength++;
			} else {
				System.out.println();
				currLength = 0;
			}

		}
		return finalResult;
	}

	// Decode given encoded string
	public static void decode(String inString) {
		Node temp = queue.peek();
		String finalString = "";
		for (int i = 0; i < inString.length(); i++) {
			if (temp.leftChild == null && temp.rightChild == null) { // If leaf
				finalString = finalString + temp.value;
				temp = queue.peek();
			}
			if (inString.charAt(i) == '0') {
				temp = temp.leftChild;
			}
			if (inString.charAt(i) == '1') {
				temp = temp.rightChild;
			}

		}

		System.out.println(finalString);
	}

}
