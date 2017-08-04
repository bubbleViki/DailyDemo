package day_03.dataStructure;

import java.util.Arrays;

public class MaxHeap extends Heap{
	public MaxHeap(int[] data){
		super(data);
	}

	@Override
	public Heap buildHeap() {
		// 从最后一个节点的父节点开始构造堆
		int start=getParentIndex(length-1);
		for(;start>=0;start--){
			adjustDownHeap(start);
		}
		return this;
	}


	@Override
	public Heap remove() {
		//将最后一个节点与头节点交换
		swap(0,length-1);
		//重新复制一个数组
		int[] newData=new int[length-1];
		System.arraycopy(data, 0, newData, 0, length-1);
		this.data=newData;
		this.length=length-1;
		//从头开始调整
		//因为忽略头节点，其他位置已经稳定，不需要从低开始遍历，只需要递归调整头节点的子节点
		adjustDownHeap(0);
		return this;      
	}
	
	@Override
	public Heap insert(int value) {
	
		int[] newData=new int[length+1];
		System.arraycopy(data, 0, newData, 0, length);
		
		//插入到数组末端
		newData[length]=value;
		this.data=newData;
		this.length=length+1;
		
		//最后一个节点(插入的数值）开始递归向上进行调整，只关心父节点大小，因为其子节点一直保持稳定状态
		adjustUpHeap(this.length-1);
		return this;
	}
	
	//从node开始，自上而下调整堆
	@Override
	public void adjustDownHeap(int node) {
		int right=getRightChildIndex(node);
		int left=getLeftChildIndex(node);
		int max=node;
		
		if(right<length&&data[right]>data[max]){
			max=right;
		}
		
		if(left<length&&data[left]>data[max]){
			max=left;
		}
		if(max!=node){
			swap(node,max);
			adjustDownHeap(max);
		}
	}	
	
	
	//从node开始，自下而上调整堆
		@Override
		public void adjustUpHeap(int node) {
			int parent=getParentIndex(node);
			if(parent>=0&&data[parent]<data[node]){
				swap(node,parent);
				adjustUpHeap(parent);
			}
		}	
		
	
	public static void main(String args[]){
		int data[]=new int[10];
		for(int i=0;i<data.length;i++){
			data[i]=(int)(Math.random()*100);
		}
		System.out.println(Arrays.toString(data));
	
		//长度为data.length的堆
		Heap heap = new MaxHeap(data);
		heap.buildHeap().print();
		heap.remove().print();
		
		heap.insert(69).print();
	
	}



}
