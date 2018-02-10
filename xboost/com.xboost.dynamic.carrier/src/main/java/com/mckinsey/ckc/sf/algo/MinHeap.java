package com.mckinsey.ckc.sf.algo;

import java.util.List;

import com.mckinsey.ckc.sf.data.Opportunity;

public class MinHeap {
	// 堆的存储结构 - 数组
	private List<Opportunity> data;
	private int currentSize;

	// 将一个数组传入构造方法，并转换成一个小根堆
	public MinHeap(List<Opportunity> data) {
		this.data = data;
		buildHeap();
	}

	// 将数组转换成最小堆
	private void buildHeap(){
		for (int i = data.size()/ 2 - 1; i >= 0; i--) {
			heapify(i);
		}
	}

	private void heapify(int i) {
		// 获取左右结点的数组下标
		int l = left(i);
		int r = right(i);

		// 这是一个临时变量，表示 跟结点、左结点、右结点中最小的值的结点的下标
		int smallest = i;

		// 存在左结点，且左结点的值小于根结点的值
		if (l < data.size() && data.get(l).getCurrentDistance()< data.get(i).getCurrentDistance())
			smallest = l;

		// 存在右结点，且右结点的值小于以上比较的较小值
		if (r < data.size() && data.get(r).getCurrentDistance() < data.get(smallest).getCurrentDistance())
			smallest = r;

		// 左右结点的值都大于根节点，直接return，不做任何操作
		if (i == smallest)
			return;

		// 交换根节点和左右结点中最小的那个值，把根节点的值替换下去
		swap(i, smallest);

		// 由于替换后左右子树会被影响，所以要对受影响的子树再进行heapify
		heapify(smallest);
	}

	// 获取右结点的数组下标
	private int right(int i) {
		return (i + 1) << 1;
	}

	// 获取左结点的数组下标
	private int left(int i) {
		return ((i + 1) << 1) - 1;
	}

	// 交换元素位置
	private void swap(int i, int j) {
		Opportunity tmp = data.get(i);
		data.set(i, data.get(j));
		data.set(j, tmp);
	}

	// 获取对中的最小的元素，根元素
	public Opportunity getRoot() {
		return data.get(0);
	}

	// 替换根元素，并重新heapify
	public void setRoot(Opportunity root) {
		data.set(0, root);
		heapify(0);
	}
	
	
	public void insertElement(Opportunity x) {  
        int hole = ++currentSize;  
        while (hole > 1 && x.getCurrentDistance()< data.get(hole/2).getCurrentDistance()){  
        	data.set(hole, data.get(hole/2));
            hole /= 2;  
        }  
        data.set(hole, x) ;
    }
}
