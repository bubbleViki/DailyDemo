package day_04.sort.algorithm;

public class ArrayMerge {
	
	public static void mergeList(Comparable[] a,Comparable b[],Comparable c[]){
		
		int x,y,z;
		x=y=z=0;
		
		while(x<a.length&&y<b.length){
			if(less(a[x],b[y])){ //if a<b
				c[z]=a[x];
				z++;
				x++;
			}
			else c[z++]=b[y++];
		}	
		
		//if a[] not end
		while(x<a.length){
			c[z++]=a[x++];
		}
		
		while(y<b.length){
			c[z++]=b[y++];
		}
		
	}	
	
	private static Comparable[] aux;//�鲢����Ҫ�ĸ�������
	
	public static void sort(Comparable[] a){
		aux=new Comparable[a.length]; //һ���Է���ռ�
		sort(a,0,a.length-1);
	}
	
	//sort
	private static void sort(Comparable[] a,int lo,int hi){
		if(hi<=lo) return;
		//mid
		int mid=lo+(hi-lo)/2;
		//left	
		sort(a,lo,mid);
		//right
		sort(a,mid+1,hi);
		//�鲢
 		merge(a,lo,mid,hi);
						
	}
	
	
	
	//sortFromLower
	public static void sortFromLower(Comparable[] a){
		int N=a.length;
		aux=new Comparable[N];
		//��x�ַ���ģʽ(22,44,88...)���ֱ�����˶�Ӧ�Ĺ鲢����
		for(int sz=1;sz<N;sz=sz*2){
			for(int lo=0;lo<N-sz;lo+=sz*2){
				merge(a,lo,lo+sz-1,Math.min(lo+sz+sz-1, N-1)); //δ�������ģ�ֱ��ȡĩ��  1234 567 ��4,6��
			}
				
		}
	}
	
	//low mid high
	//a[lo...mid..hi]->a[low....mid]+a[mid+1...high]
	//sorted
	public static void merge(Comparable[] a,int lo,int mid,int hi){
		int i=lo;
		int j=mid+1;
		//Comparable[] aux = new Comparable[a.length];
		
		//copy a[]->aux[]
		for(int k=lo;k<=hi;k++){
			aux[k]=a[k];
		}
		
		//aux[]�鲢��a[]
		//k<aux.lengthʼ�ղ���
		for(int k=lo;k<=hi;k++){
			//left end
			if(i>mid) a[k]=aux[j++];
			else if(j>hi) a[k]=aux[i++];
			
  			else if(less(aux[i],aux[j])) //lest right elements compare
				a[k]=aux[i++];
			else a[k]=aux[j++];
		}
	}
	
	
	
	//check isSorted
	public static boolean isSorted(Comparable a[]){
		for(int i=1;i<a.length;i++){
			if(less(a[i],a[i-1]))
				return false;
		}
		
		return true;
	}
	
	
	public static boolean less(Comparable x,Comparable y){
		return x.compareTo(y)<0; //x<y 
	}
	
	public static void show(Comparable[] a){
		for(int i=0;i<a.length;i++){
			System.out.println(a[i]);
		}
	}
	public static void main(String args[]){

		//1���ϲ�������
//		String[] a1={"a","c","d","e","f"};
//		String[] b1={"d","f","w","z"};
		
		//remeber to init the Sring[]
//		String[] c1=new String[a1.length+b1.length];
//		if(isSorted(a1)&&isSorted(b1)){
//			mergeList(a1,b1,c1);
//			show(c1);
//		}
//		
//		else System.out.println("a/b not sorted");
		
		
//		//2��ԭ�ع鲢 
//		//ǰ��κͺ��ξ�sorted
//		//a[lo...mid..hi]->a[low....mid]+a[mid+1...high]
//		String[] a={"a","c","d","e","f","d","f","w","z"};
//		//merge��Comaparable[] a , int lo , int mid , int hi��
//		merge(a,0,4,8);
//		show(a);
//		
//		
		
		//3���Զ����¹鲢
		//����
		String[] b={"8","6","2","4","1","5","7","4"};
		sort(b);
		show(b);
	
		//4)�Ե����Ϲ鲢
		//������22һ�鲢����44һ�鲢��������
		String[] c={"8","6","2","4","1","5","7","4"};
		sortFromLower(c);
		System.out.println("fromLower");
		show(c);
	}
}
