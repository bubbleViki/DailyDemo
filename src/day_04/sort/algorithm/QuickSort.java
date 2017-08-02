package day_04.sort.algorithm;

public class QuickSort {
	
	private static int partition(Comparable[] a,int lo,int hi){
		int i =lo;
		int j=hi+1;
		
		//ָ����ʼ���е�
		Comparable v= a[lo];
		
		while(true){
			//���ӦС��v�����ҵ�����v��ֵʱstop������
			while(less(a[++i],v)){
				if(i==hi) break;
			}
			//�ұ�Ӧ����v,��С��vʱstop������
			while(less(v,a[--j])){
				if(j==lo) break;
			}
			
			if(i>=j) break;
			
			exch(a,i,j);
			
		}
		
		exch(a,lo,j);
		
		return j;
	}
	
	public static void sort(Comparable[] a){
		sort(a,0,a.length-1);
	}
	
	private static void sort(Comparable[] a,int lo,int hi){
		
		//�ݹ��˳�����
		if(lo>=hi) return;
		
		//�зֵ�
		int j=partition(a,lo,hi);
		//�������
		sort(a,lo,j-1);
		//�ұ�����
		sort(a,j+1,hi);
		
	}
	
	public static boolean less(Comparable x,Comparable y){
		return x.compareTo(y)<0;
	}
	
	public static void exch(Comparable[] a,int x,int y){
		Comparable  temp=a[x];
		a[x]=a[y];
		a[y]=temp;
	}
	
	public static void show(Comparable[] a){
		for(int i=0;i<a.length;i++)
			System.out.println(a[i]);
	}
	
	public static void main(String[] args){
		String[] a={"K","R","A","T","E","L","E","P","U","I","M","Q","C","X","O","S"};
		sort(a);
		show(a);
	}
}
