package day_03.dataStructure;



//嵌套类定义结点的抽象数据类型
//内部类访问的特点是：内部类可以直接访问外部类的成员，包括私有；外部类要访问内部类的成员，必须先创建对象。
class Node{

	//参数类型
	int data; //数据域 
	//Node类型
	Node next; //指针域
	
	public Node(int data){
		this.data=data;
		next=null;
	}
}

public class LinkList {
	public Node head;
	public Node current;
	
	//向链表中添加数据
	public void add(int data){
		if(head==null){ //if头结点为空，说明链表未创建，把新的节点赋给头结点
			head=new Node(data);
			current=head;
		}else{
			//创建新节点
			current.next=new Node(data);
			//把链表的当前索引向后移动一位
			current=current.next;
		}
	}
	
	// 遍历单链表
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
	
	//获取单链表中节点个数
	//时间复杂度O(n)
	public int getLength(){
		int length=0;
		Node current=head;
		while(current!=null){
			length++;
			current=current.next;
		}
		
		return length;
	}
	
	//查找单链表中的倒数第k个结点
	
	//1)遍历计算出链表长度，第二遍遍历至指定结点
	public int findLastNode_1(int index){
		if(head==null){
			return -1;
		}
		
		//链表遍历
		int size=0;
		current=head;
		while(current!=null){
			size++;
			current=current.next;
		}
		
		//遍历至倒数
		current=head;
		for(int i=0;i<size-index;i++){
			current=current.next;
		}
		
		return current.data;
	}
	
	//2)只遍历一遍，设置两个指针，两者相差k-1个位置（即链表末端和指定位置的距离）
	public Node findLastNode_2(int index){
		
		//链表不存在或未指定
		if(index==0||head==null){
			return null;
		}
		
		//创建两个结点指针指向head结点
		Node first=head;
		Node second=head;
		
		//first先移动k-1个位置
		for(int i = 0 ; i<index-1;i++){
			first=first.next;
			if(first==null){//指定值大于链表长度
				throw new NullPointerException("链表的长度小于" + index); //我们自己抛出异常，给用户以提示
				
			}
		}
		
		//指针同时开始移动
		while(first.next!=null){
			first=first.next;
			second=second.next;
		}
		
		return second;
		
	}
	
	//中间结点（first为second的二倍，即first走两步 second走一步）
	//合并两个有序的单链表
	//单链表的反转
	//倒序打印链表
	//判断两个单链表相交的第一个交点
	
	
	public static void main(String args[]){
		LinkList list=new LinkList();
		//向LinkList中添加数据
		for(int i=0;i<10;i++){
			list.add(i);
		}
		
		list.print(list.head);
		System.out.println("\ntotally:"+list.getLength());
	
		System.out.println("Last 6th is:"+list.findLastNode_1(6));
		System.out.println("Last 4th is:"+list.findLastNode_2(4).data);
	}
}
