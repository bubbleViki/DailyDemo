package day_02.java.generics;

public class Generics {
	
	
	//���ͷ���
	//��������
	public static <T> void out(T t){
		System.out.println(t);
	}
	
	//ƥ��n��ʵ��
	//�ɱ䳤����
	//���βε�����...��������
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
		
		
		//������ -�������Ƶ�&�Զ����
		out("NU'EST");
		out(2012+"\n");
		
		out2("NU'EST",2012,"5members");
		
		
	
	}
}
