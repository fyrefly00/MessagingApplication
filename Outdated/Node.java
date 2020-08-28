import java.io.Serializable;

public class Node implements Comparable<Node>, Serializable {
		Node leftChild;
		Node rightChild;
		int freq;
		char value;

		public Node(int inFreq, char inValue) {
			freq = inFreq;
			value = inValue;
		}

		public int compareTo(Node inNode) {
			if (inNode.freq > freq) {
				return -1;
			} else if (inNode.freq < freq) {
				return 1;
			} else {
				return 0;
			}
		}

	}
