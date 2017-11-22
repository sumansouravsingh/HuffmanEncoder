package ads.project.HuffmanDecoding;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import ads.project.TreeStructure.TreeStructure;
public class Decoding {
	
	public static String getEncodedString(String fileName) throws IOException, FileNotFoundException{
		byte[] bArr;
		StringBuilder outputStr=new StringBuilder();
		bArr=Files.readAllBytes(new File(fileName).toPath());
		for(byte b: bArr){
			outputStr.append(Integer.toBinaryString(b & 255 | 256).substring(1));
		}
		return outputStr.toString();
	}
	
	public void createTree(TreeStructure r,String x, String val)
	{
		int len=x.length();
		TreeStructure t=r;
		int i=0;
		while(i<len-1)
		{
			char temp=x.charAt(i++);
			if(temp=='0')
			{
				if(t.left==null)
				{
					TreeStructure tree = new TreeStructure(Integer.MIN_VALUE,0,null,null);
					t.left=tree;
				}
				t=t.left;
				
			}
			else if(temp=='1')
			{
				if(t.right==null)
				{
					TreeStructure tree = new TreeStructure(Integer.MIN_VALUE,0,null,null);
					t.right=tree;
				}
				t=t.right;
			}
		}
		TreeStructure tree = new TreeStructure(Integer.parseInt(val),0,null,null);
		if(x.charAt(i)=='1')
		{
			t.right=tree;
		}
		else
		{
			t.left=tree;
		}
	}

	public void createDecodedText(TreeStructure root,String encodedString, HashMap<String, String> map)
	{
		try{
			TreeStructure t=root;
			BufferedWriter decodeFile = new BufferedWriter(new FileWriter(new File("decoded.txt")));
			int i=0;
			StringBuilder str=new StringBuilder();
			while(i<encodedString.length())
			{
				if(t.data!=Integer.MIN_VALUE)
				{
					decodeFile.write(t.data+"\n");
					t=root;
					str.setLength(0);
					continue;
				}
				else
					if(encodedString.charAt(i)=='0')
					{
						str.append("0");
						t=t.left;
					}
					else if(encodedString.charAt(i)=='1')
					{
						str.append("1");
						t=t.right;
					}
				i++;
			}
			if(map.containsKey(str))
			{
				decodeFile.write(map.get(str.toString())+"\n");
			}
			else{
				while(!map.containsKey(str.toString()))
				{
					str.append("0");
					i++;
				}
				decodeFile.write(map.get(str.toString())+"\n");
			}
			System.out.println("Decoder File generated");
			decodeFile.close();
		}
		catch(Exception e){
			System.out.println("Something went wrong!");
			return;
		}
	}
	
	public static void main(String[] args) {
		TreeStructure root = new TreeStructure(Integer.MIN_VALUE, 0, null, null);
		Decoding d=new Decoding(); 
		HashMap<String, String> map = new HashMap<String, String>();
		String encodedString="";
		try{
			encodedString=Decoding.getEncodedString("encoded.bin");
		}
		catch(Exception e){
			System.out.println("Something went wrong!");
			return;
		}

		try {
			File tableFile = new File("code_table.txt");
			Scanner input = new Scanner(tableFile);
			while(input.hasNextLine())
			{
				String temp=input.nextLine();
				if(!temp.isEmpty())
				{
					String tmparr[]=temp.split("==>");
					map.put(tmparr[1], map.getOrDefault(tmparr[1], tmparr[0]));
					d.createTree(root,tmparr[1],tmparr[0]);
				}
			}
			input.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found! Enter a valid file name and path");
			return;
		}
		d.createDecodedText(root, encodedString, map);
	}
}
