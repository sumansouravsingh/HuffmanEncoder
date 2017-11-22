package ads.project.pairingheap;
import java.util.ArrayList;

import ads.project.TreeStructure.TreeStructure;

class PairNode {
	TreeStructure data;
	PairNode leftChild;
	PairNode nextSibling;
	PairNode prev;
	public PairNode(TreeStructure x) {
		data = x;
		leftChild = null;
		nextSibling = null;
		prev = null;
	}
}

public class PairingHeap {
	private PairNode root;
	private PairNode[] treeArray = new PairNode[5];
	public ArrayList<PairNode> arr = new ArrayList<PairNode>();
	public PairingHeap() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}
	public boolean isLastNode() {
		return root.leftChild == null;
	}
	public TreeStructure peek(){
		return root.data;
	}
	public void makeEmpty() {
		root = null;
	}
	public PairNode insert(TreeStructure x) {
		PairNode newNode = new PairNode(x);
		if (root == null)
			root = newNode;
		else
			root = compareAndLink(root, newNode);
		return newNode;
	}

	private PairNode compareAndLink(PairNode first, PairNode second) {
		if (second == null)
			return first;

		if (second.data.frequency < first.data.frequency) {
			second.prev = first.prev;
			first.prev = second;
			first.nextSibling = second.leftChild;
			if (first.nextSibling != null)
				first.nextSibling.prev = first;
			second.leftChild = first;
			return second;
		} else {
			second.prev = first;
			first.nextSibling = second.nextSibling;
			if (first.nextSibling != null)
				first.nextSibling.prev = first;
			second.nextSibling = first.leftChild;
			if (second.nextSibling != null)
				second.nextSibling.prev = second;
			first.leftChild = second;
			return first;
		}
	}

	private PairNode combineSiblings(PairNode firstSibling) {
		if (firstSibling.nextSibling == null)
			return firstSibling;
		int numSiblings = 0;
		for (; firstSibling != null; numSiblings++) {
			treeArray = doubleIfFull(treeArray, numSiblings);
			treeArray[numSiblings] = firstSibling;
			//arr.add(numSiblings, firstSibling);
			firstSibling.prev.nextSibling = null;
			firstSibling = firstSibling.nextSibling;
		}
		treeArray = doubleIfFull(treeArray, numSiblings);
		treeArray[numSiblings] = null;
		//arr.add(numSiblings, null);
		int i=0;
		for (; i + 1 < numSiblings; i += 2)
			treeArray[i] = compareAndLink(treeArray[i], treeArray[i + 1]);
			//arr.set(i,compareAndLink(arr.get(i), arr.get(i + 1)));
		int j=i-2;
		if (j==numSiblings-3)
			treeArray[j] = compareAndLink(treeArray[j], treeArray[j + 2]);
			//arr.set(j,compareAndLink(arr.get(j), arr.get(j + 2)));
		for (;j>=2;j-= 2)
			treeArray[j - 2] = compareAndLink(treeArray[j - 2], treeArray[j]);
			//arr.set(j-2,compareAndLink(arr.get(j-2), arr.get(j)));
		return treeArray[0];
		//return arr.get(0);
	}

	private PairNode[] doubleIfFull(PairNode[] array, int index) {
		if (index == array.length) {
			PairNode[] oldArray = array;
			array = new PairNode[index * 2];
			for (int i = 0; i < index; i++)
				array[i] = oldArray[i];
		}
		return array;
	}

	public TreeStructure extractMin() {
		if (isEmpty())
			return null;
		TreeStructure x = root.data;
		if (root.leftChild == null)
			root = null;
		else
			root = combineSiblings(root.leftChild);
		return x;
	}
}