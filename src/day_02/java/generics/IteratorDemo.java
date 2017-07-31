package day_02.java.generics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteratorDemo {
	
	public static void main(String args[]){
		List<String> member = new ArrayList<>();
		member.add("JR");
		member.add("REN");
		member.add("MINHYUN");
		member.add("ARON");
		member.add("BEAHKO");
		
		
		//way-1
		LinkedList<String> member2= new LinkedList<>();
		member2.add("JR");
		member2.add("REN");
		member2.add("MINHYUN");
		member2.add("ARON");
		member2.add("BEAHKO");
		
		//way-2
		List<String> member3=new LinkedList<>();
		member3.add("ARON");
		member3.add("BEAHKO");
		member3.add("JR");
		member3.add("REN");
		member3.add("MINHYUN");
		
		
		
		//peek at the head of the list
		System.out.println("head of the list:"+((LinkedList<String>) member3).peek());
		
		//foreach
		
		//
		Iterator<String> iter = member.iterator(); //获取ArrayList的迭代器
		
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
	}
}
