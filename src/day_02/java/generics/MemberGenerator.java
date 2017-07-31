package day_02.java.generics;

import java.util.Random;

public class MemberGenerator implements Generator<String> {

	private String[] members=new String[]{"ARON","JR","BAEKHO","MINHYU","REN"};
	
	@Override
	public String next() {
		Random r = new Random();
		return members[r.nextInt(5)];
	}

}
