package day_02.java.generics;

public class Generics {
	
	
	//泛型方法
	//参数泛化
	public static <T> void out(T t){
		System.out.println(t);
	}
	
	//匹配n个实参
	//可变长参数
	//（形参的类型...参数名）
	@SafeVarargs
	public static <T> void out2(T...args){
		for(T t:args){
			System.out.println(t);
		}
	}
	
	
	
	public static void main(String args[]){
		Container<String,String> c1 = new Container<String,String>("name","NU'EST");
		Container<String,Integer> c2 = new Container<String,Integer>("member",5);
		System.out.println(c1.getKey()+":"+c1.getValue());
		System.out.println(c2.getKey()+":"+c2.getValue()+"\n");
		
		
		
		
		MemberGenerator member = new MemberGenerator();
		System.out.println(member.next()+"\n");
		
		
		//编译器 -》类型推导&自动打包
		out("NU'EST");
		out(2012+"\n");
		
		out2("NU'EST",2012,"5members");
		
		
	
	}
}
