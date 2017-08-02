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
	
	private static Comparable[] aux;//归并所需要的辅助数组
	
	public static void sort(Comparable[] a){
		aux=new Comparable[a.length]; //一次性分配空间
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
		//归并
 		merge(a,lo,mid,hi);
						
	}
	
	
	
	//sortFromLower
	public static void sortFromLower(Comparable[] a){
		int N=a.length;
		aux=new Comparable[N];
		//以x种分组模式(22,44,88...)，分别进行了对应的归并次数
		for(int sz=1;sz<N;sz=sz*2){
			for(int lo=0;lo<N-sz;lo+=sz*2){
				merge(a,lo,lo+sz-1,Math.min(lo+sz+sz-1, N-1)); //未满整除的，直接取末项  1234 567 （4,6）
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
		
		//aux[]归并至a[]
		//k<aux.length始终不变
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

		//1）合并两数组
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
		
		
//		//2）原地归并 
//		//前半段和后半段均sorted
//		//a[lo...mid..hi]->a[low....mid]+a[mid+1...high]
//		String[] a={"a","c","d","e","f","d","f","w","z"};
//		//merge（Comaparable[] a , int lo , int mid , int hi）
//		merge(a,0,4,8);
//		show(a);
//		
//		
		
		//3）自顶向下归并
		//排序
		String[] b={"8","6","2","4","1","5","7","4"};
		sort(b);
		show(b);
	
		//4)自低向上归并
		//遍历先22一归并，再44一归并。。。。
		String[] c={"8","6","2","4","1","5","7","4"};
		sortFromLower(c);
		System.out.println("fromLower");
		show(c);
	}
}
