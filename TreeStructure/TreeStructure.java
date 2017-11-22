package ads.project.TreeStructure;

public class TreeStructure implements Comparable<TreeStructure>{
	public int data;
	public long frequency;
	public TreeStructure left;
	public TreeStructure right;
	public TreeStructure(int data, long frequency, TreeStructure left, TreeStructure right)
	{
		this.data=data;
		this.frequency=frequency;
		this.left=left;
		this.right=right;
	}
	@Override
	public int compareTo(TreeStructure t) {
		// TODO Auto-generated method stub
		if(frequency>t.frequency)
		{
			return 1;
		}
		else if(frequency<t.frequency)
			return -1;
		else
			return 0;
	}
}
