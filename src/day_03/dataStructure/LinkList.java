package day_03.dataStructure;



//Ƕ���ඨ����ĳ�����������
//�ڲ�����ʵ��ص��ǣ��ڲ������ֱ�ӷ����ⲿ��ĳ�Ա������˽�У��ⲿ��Ҫ�����ڲ���ĳ�Ա�������ȴ�������
class Node{

	//��������
	int data; //������ 
	//Node����
	Node next; //ָ����
	
	public Node(int data){
		this.data=data;
		next=null;
	}
}

public class LinkList {
	public Node head;
	public Node current;
	
	//���������������
	public void add(int data){
		if(head==null){ //ifͷ���Ϊ�գ�˵������δ���������µĽڵ㸳��ͷ���
			head=new Node(data);
			current=head;
		}else{
			//�����½ڵ�
			current.next=new Node(data);
			//������ĵ�ǰ��������ƶ�һλ
			current=current.next;
		}
	}
	
	// ����������
	public void print(Node node){
		if(node==null){
			return;
		}
		
		current=node;
		while(current!=null){
			System.out.print(current.data+" ");
			current=current.next;
		}
	}
	
	//��ȡ�������нڵ����
	//ʱ�临�Ӷ�O(n)
	public int getLength(){
		int length=0;
		Node current=head;
		while(current!=null){
			length++;
			current=current.next;
		}
		
		return length;
	}
	
	//���ҵ������еĵ�����k�����
	
	//1)��������������ȣ��ڶ��������ָ�����
	public int findLastNode_1(int index){
		if(head==null){
			return -1;
		}
		
		//�������
		int size=0;
		current=head;
		while(current!=null){
			size++;
			current=current.next;
		}
		
		//����������
		current=head;
		for(int i=0;i<size-index;i++){
			current=current.next;
		}
		
		return current.data;
	}
	
	//2)ֻ����һ�飬��������ָ�룬�������k-1��λ�ã�������ĩ�˺�ָ��λ�õľ��룩
	public Node findLastNode_2(int index){
		
		//�������ڻ�δָ��
		if(index==0||head==null){
			return null;
		}
		
		//�����������ָ��ָ��head���
		Node first=head;
		Node second=head;
		
		//first���ƶ�k-1��λ��
		for(int i = 0 ; i<index-1;i++){
			first=first.next;
			if(first==null){//ָ��ֵ����������
				throw new NullPointerException("����ĳ���С��" + index); //�����Լ��׳��쳣�����û�����ʾ
				
			}
		}
		
		//ָ��ͬʱ��ʼ�ƶ�
		while(first.next!=null){
			first=first.next;
			second=second.next;
		}
		
		return second;
		
	}
	
	//�м��㣨firstΪsecond�Ķ�������first������ second��һ����
	//�ϲ���������ĵ�����
	//������ķ�ת
	//�����ӡ����
	//�ж������������ཻ�ĵ�һ������
	
	
	public static void main(String args[]){
		LinkList list=new LinkList();
		//��LinkList���������
		for(int i=0;i<10;i++){
			list.add(i);
		}
		
		list.print(list.head);
		System.out.println("\ntotally:"+list.getLength());
	
		System.out.println("Last 6th is:"+list.findLastNode_1(6));
		System.out.println("Last 4th is:"+list.findLastNode_2(4).data);
	}
}
