package ads.project.HuffmanCoding;
import ads.project.FourWayHeap.FourWayHeap;
import ads.project.PriorityQueue.PriorityQueueStruct;
import ads.project.TreeStructure.*;
import ads.project.pairingheap.PairingHeap;

import java.io.*;
import java.util.*;
public class Solution {
	static HashMap<Integer, String> map = new HashMap<Integer, String>();
	private static Scanner sc;

	public HashMap<Integer, Integer> readFromFile(String fileName, HashMap<Integer, Integer> m, ArrayList<Integer> iList) {
		File inputFile = new File(fileName);
		Scanner input=null;
		try {
			input = new Scanner(inputFile);
			while(input.hasNextLine())
			{
				String temp=input.nextLine();
				if(!temp.isEmpty())
				{
					int x= Integer.parseInt(temp);
					iList.add(x);
					m.put(x, m.getOrDefault(x, 0)+1);
				}
			}
			input.close();
			System.out.println("Read from file Complete!!!");
			return m;
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found! Enter a valid file name and path");
			return null;
		}
	}

	public void printCodes(TreeStructure root, String s)
	{
		if(root.left==null && root.right==null)
		{
			map.put(root.data, s);
			return;
		}
		printCodes(root.left, s + "0");
		printCodes(root.right, s + "1");
	}

	public void createHuffmanTree(HashMap<Integer, Integer> map)
	{
		TreeStructure left,right;
		PriorityQueueStruct<TreeStructure> q = new PriorityQueueStruct<TreeStructure>();
		for(Map.Entry<Integer, Integer> m: map.entrySet())
		{
			q.insert(new TreeStructure(m.getKey(),m.getValue(),null,null));
		}
		while(q.size()!=1)
		{
			left=q.extractMin();
			right=q.extractMin();
			if(left==null||right==null)
			{
				System.out.println("Empty List");
				return;
			}
			TreeStructure top=new TreeStructure(Integer.MIN_VALUE,left.frequency+right.frequency,left,right);
			q.insert(top);
		}
		if(q.peek()==null)
			System.out.println("List is Empty");
		else printCodes(q.peek(),"");
	}
	
	public void createHuffmanTreePairingHeap(HashMap<Integer, Integer> map)
	{
		TreeStructure left,right;
		PairingHeap q = new PairingHeap();
		for(Map.Entry<Integer, Integer> m: map.entrySet())
		{
			TreeStructure top=new TreeStructure(m.getKey(),m.getValue(),null,null);
			q.insert(top);
		}
		while(!q.isLastNode())
		{
			left=q.extractMin();
			right=q.extractMin();
			TreeStructure top=new TreeStructure(Integer.MIN_VALUE,left.frequency+right.frequency,left,right);
			q.insert(top);
		}
		printCodes(q.peek(),"");
	}
	
	public void createHuffmanTreeFourWay(HashMap<Integer, Integer> map)
	{
		TreeStructure left,right;
		FourWayHeap q = new FourWayHeap();
		for(Map.Entry<Integer, Integer> m: map.entrySet())
		{
			TreeStructure top=new TreeStructure(m.getKey(),m.getValue(),null,null);
			q.insert(top);
		}
		while(q.size()!=4)
		{
			left=q.extractMin();
			right=q.extractMin();
			TreeStructure top=new TreeStructure(Integer.MIN_VALUE,left.frequency+right.frequency,left,right);
			q.insert(top);
		}
		printCodes(q.peek(),"");
	}
	
	private void writeBinaryFile(ArrayList<Integer> list) throws IOException {
		try{
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream("encoded.bin"));
			ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			StringBuilder number = new StringBuilder();
			int i=0;
			while (i<list.size()) {
				int index = list.get(i++);
				if (map.containsKey(index)) {
					String code = map.get(index);
					number.append(code);
					if(number.length()>=8){
						while(number.length()>=8){
							byte[] byteArray=getByteByString(number.substring(0, 8).toString());
							outputStream.write(byteArray);
							number=number.delete(0, 8);
						}
					}
				} else {
						System.out.println("not found in table");
				}
			}
			if (number.length() != 0) {
				byte[] byteArray = getByteByString(number.substring(0, 8).toString());
				outputStream.write(byteArray);
			}
			byte[] byteArray = outputStream.toByteArray();
			bos.write(byteArray);
			bos.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static byte[] getByteByString(String binaryString) {
		int splitSize = 8;
		if (binaryString.length() % splitSize == 0) {
			int index = 0;
			int position = 0;
			byte[] resultByteArray = new byte[binaryString.length() / splitSize];
			StringBuilder text = new StringBuilder(binaryString);
			while (index < text.length()) {
				String binaryStringChunk = text.substring(index, Math.min(index+splitSize, text.length()));
				Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
				resultByteArray[position] = byteAsInt.byteValue();
				index += splitSize;
				position++;
			}
			return resultByteArray;
		} else {
			System.out.println("Invalid string length");
			return null;
		}
	}

	private void createEncodedFile(ArrayList<Integer> iList, String path) {
		try {
			writeBinaryFile(iList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createCodeTable() {
		try{
			BufferedWriter codeTableWrite = new BufferedWriter(new FileWriter(new File("code_table.txt")));
			for(Map.Entry<Integer, String> codemap: map.entrySet())
			{
				codeTableWrite.write(codemap.getKey()+"==>"+codemap.getValue()+"\n");
			}
			System.out.println("Code Table file generated!");
			codeTableWrite.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in Writing.");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args){
		ArrayList<Integer> iList = new ArrayList<Integer>();
		sc= new Scanner(System.in);
		System.out.println("Enter File Name to Read From: ");
		String fileName = sc.nextLine();
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		Solution s = new Solution();
		m=s.readFromFile(fileName, m, iList);
		if(m==null)
			return;
		long time[]=new long[3];
		long startBinaryHeap=0;
		long endBinaryHeap=0;
		startBinaryHeap = System.currentTimeMillis();
		for(int cnt=0;cnt<10;cnt++){
			s.createHuffmanTree(m);
		}
		endBinaryHeap = System.currentTimeMillis();
		time[0]=endBinaryHeap-startBinaryHeap;
		
		startBinaryHeap = System.currentTimeMillis();
		for(int cnt=0;cnt<10;cnt++){
			s.createHuffmanTreePairingHeap(m);
		}
		endBinaryHeap = System.currentTimeMillis();
		time[1]=endBinaryHeap-startBinaryHeap;
		
		startBinaryHeap = System.currentTimeMillis();
		for(int cnt=0;cnt<10;cnt++){
			s.createHuffmanTreeFourWay(m);
		}
		endBinaryHeap = System.currentTimeMillis();
		time[2]=endBinaryHeap-startBinaryHeap;
		
		System.out.println("Times: Binary= "+(time[0])+", Four way= "+(time[2])+", Pairing= "+(time[1]));
		s.createEncodedFile(iList,fileName);
		s.createCodeTable();
	}
}