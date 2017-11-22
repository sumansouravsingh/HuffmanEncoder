package ads.project.FourWayHeap;

import java.util.ArrayList;

import ads.project.PriorityQueue.PriorityQueueStruct;
import ads.project.TreeStructure.TreeStructure;

public class FourWayHeap extends PriorityQueueStruct<Comparable<TreeStructure>>
{   
	ArrayList<TreeStructure> list;
	public FourWayHeap()
	{
		super();
		list = new ArrayList<>();
		list.add(null);
		list.add(null);
		list.add(null);
	}

	public int size()
	{
		return list.size();
	}
	public void insert(TreeStructure node)
	{
		list.add(node);
		heapify(true);
	}

	public TreeStructure extractMin()
	{
		if(list.size()<4)
		{
			return null;
		}
		TreeStructure returnNode = list.get(3);
		TreeStructure node = list.remove(list.size() - 1);
		if(list.size()>3)
		{
			list.set(3,node);
			heapify(false);
		}
		return returnNode;
	}

	public TreeStructure peek()
	{
		if(list.isEmpty())
		{
			return null;
		}
		return list.get(3);
	}

	private void heapify(boolean moveUp)
	{
		int index;
		if(moveUp)
		{
			index = list.size() - 1;
			while(index > 3)
			{
				if(list.get(index).frequency < list.get((index + 8)/ 4).frequency)
				{
					TreeStructure temp = list.get(index);
					list.set(index, list.get((index + 8)/ 4));
					list.set((index + 8)/ 4, temp);
					index = (index + 8)/ 4;
				}
				else
				{
					break;
				}
			}
		}
		else
		{
			index = 3;
			while(index >= 3 && index < list.size())
			{
				TreeStructure min = list.get(index);
				int j = -1;
				for(int i=1;i<=4;i++)
				{
					int childIndex = 4 * index - (i + 4);
					if(childIndex < list.size())
					{
						if(list.get(childIndex).frequency < min.frequency)
						{
							min = list.get(childIndex);
							j = childIndex;
						}
					}
				}
				if(j != -1)
				{
					list.set(j, list.get(index));
					list.set(index, min);
					index = j;
				}
				else break;
			}
		}
	}
}