package day_03.dataStructure;

import java.util.Arrays;

public abstract class Heap{
	
	protected int[] data;
	protected int length =0;
	
	public Heap(int[] data){
		this.data=data;
		this.length=data.length;
	}
	
	/**
	 * ������
	 */
	public abstract Heap buildHeap();
	
	/**
	 * ɾ���ڵ㣨���ڵ㣩
	 * 
	 * @return
	 */
	public abstract Heap remove();
	
	/**
	 * ����Ԫ��
	 * 
	 * @param value
	 */
	public abstract Heap insert(int value);
	
	
	
	
	/**
	 * ��node��ʼ���϶��µ�����
	 * 
	 * @param node
	 */
	public abstract void adjustDownHeap(int node);

	
	/**
	 * ��node��ʼ���¶��ϵ�����
	 * 
	 * @param node
	 */
	public abstract void adjustUpHeap(int node);

	
	
	
	/**
	 * ����Ԫ��
	 * 
	 * @param n1
	 * @param n2
	 */
	public void swap(int n1,int n2){
		int temp=data[n1];
		data[n1]=data[n2];
		data[n2]=temp;
	}
	
	
	
	/**
	 * ��ȡnode�ĸ��ڵ������
	 * 
	 * @param node
	 * @return
	 */
	protected int getParentIndex(int node){
		return (node-1)/2 ;
	}
	
	
	/**
	 * ��ȡnode�����ӵ�����
	 * 
	 * @param node
	 * @return
	 */
	protected int getLeftChildIndex(int node){
		return node*2+1 ;
	}
	
	
	/**
	 * ��ȡnode���Һ��ӵ�����
	 * 
	 * @param node
	 * @return
	 */
	protected int getRightChildIndex(int node){
		return node*2+2;
	}
	
	/**
	 * ��ӡ��
	 */
	
	protected void print(){
		System.out.println(Arrays.toString(data));
	}
	
}

