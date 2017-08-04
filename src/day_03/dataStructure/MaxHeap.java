package day_03.dataStructure;

import java.util.Arrays;

public class MaxHeap extends Heap{
	public MaxHeap(int[] data){
		super(data);
	}

	@Override
	public Heap buildHeap() {
		// �����һ���ڵ�ĸ��ڵ㿪ʼ�����
		int start=getParentIndex(length-1);
		for(;start>=0;start--){
			adjustDownHeap(start);
		}
		return this;
	}


	@Override
	public Heap remove() {
		//�����һ���ڵ���ͷ�ڵ㽻��
		swap(0,length-1);
		//���¸���һ������
		int[] newData=new int[length-1];
		System.arraycopy(data, 0, newData, 0, length-1);
		this.data=newData;
		this.length=length-1;
		//��ͷ��ʼ����
		//��Ϊ����ͷ�ڵ㣬����λ���Ѿ��ȶ�������Ҫ�ӵͿ�ʼ������ֻ��Ҫ�ݹ����ͷ�ڵ���ӽڵ�
		adjustDownHeap(0);
		return this;      
	}
	
	@Override
	public Heap insert(int value) {
	
		int[] newData=new int[length+1];
		System.arraycopy(data, 0, newData, 0, length);
		
		//���뵽����ĩ��
		newData[length]=value;
		this.data=newData;
		this.length=length+1;
		
		//���һ���ڵ�(�������ֵ����ʼ�ݹ����Ͻ��е�����ֻ���ĸ��ڵ��С����Ϊ���ӽڵ�һֱ�����ȶ�״̬
		adjustUpHeap(this.length-1);
		return this;
	}
	
	//��node��ʼ�����϶��µ�����
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
	
	
	//��node��ʼ�����¶��ϵ�����
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
	
		//����Ϊdata.length�Ķ�
		Heap heap = new MaxHeap(data);
		heap.buildHeap().print();
		heap.remove().print();
		
		heap.insert(69).print();
	
	}



}
