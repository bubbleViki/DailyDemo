package day_03.dataStructure;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SimpleDemo {
	public static void 	QueueDemo(){
		
		Queue<String> queue = new LinkedList<String>();
		
		//insert(like add())
		queue.offer("JR"); //return true or false
		queue.offer("Aron");
		queue.offer("Ren");
		
		System.out.println(queue);
		
		//remove the head(like remove())
		System.out.println("\ndelete the head:"+queue.poll()); //return the head and deleted it 
															   //if colliection is null , not throw the exception just return null;
		System.out.println(queue);
		
		//return the first element
		System.out.println("\nHead:"+queue.peek()); //queue?null,return null
		System.out.println("Head:"+queue.element()); //queue?null throws exception
		
		System.out.println(queue);
		
		
	}

	public static void StackDemo(){
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(100);//autoboxing
		stack.push(new Integer(200));//Stack stack=new Stack
		stack.push(300);
		stack.push(400);
		
		System.out.println(stack);
		
		stack.pop();
		System.out.println(stack);
		
		System.out.println("the top of the stack:"+stack.peek());
		                    
	}
	
	public static void ArithmeticExpressionsStack(){
		        
	}
	
	
	public static void main(String args[]){
		//QueueDemo();
		//StackDemo();
		//ArithmeticExpressionsStack();
	
	
	} 
}
