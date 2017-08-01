package day_04.sort.algorithm;

public class SimpleSort {
	public static void selectSort(Comparable[] a){
		int N=a.length;
		
		for(int i=0;i<N;i++){
			//设置当前位置为min
			int min=i;
			
			//剩下数组中寻找min
			for(int j=i+1;j<N;j++){
				if(less(a[j],a[min])) min=j; 
			}
			
			//当前位置如果>剩下元素中找到的min元素交换位置
			exch(a,i,min);
		}
	}
	
	public static void insertSort(Comparable[] a){
		int N=a.length;
		for(int i=1;i<N;i++){
			
			for(int j=i;j>0&&less(a[j],a[j-1]);j--){ //当前索引位置小于左边元素，且当前元素不为首元素
				exch(a,j,j-1);
			}
		}
	}
	
	
	public static void shellSort(Comparable[] a){
		int N=a.length;
		//设置初始增量值
		int h=a.length/3;
		while(h>=1){
			for(int i=h;i<N;i++){
				
				//插入排序
				for(int j=i;j>=h&&less(a[j],a[j-h]);j-=h){ //当右边（当前索引）小于左边，且右边不为首元素
					exch(a,j,j-1);
				}
			}
				
			h=h/3;
		}
		
		
	}
	
	//元素交换
	public static void exch(Comparable[] a,int i,int j){
		Comparable t=a[i];
		a[i]=a[j];
		a[j]=t;
	}
	
	
	//compare two elements
	public static boolean less(Comparable v , Comparable w){
		return v.compareTo(w)<0; // if(v<w) return true ; 
	}
	
	
	public static boolean isSorted(Comparable[] a){
		
		for(int i=1;i<a.length;i++){
			if(less(a[i],a[i-1])) //如果后一个小于前一个返回false
				return false;
		}
		return true;
	}
	
	
	public static void show(Comparable[] a){
		for(int i=0;i<a.length;i++){
			System.out.println(a[i]);
		}
	}
	
	public static void main(String[] args){
		String[] a = {"s","d","a","h","p","n","e","u","t","k","z","x","c","f","o","w"};
		//selectSort(a);
		//insertSort(a);
		shellSort(a);
		assert isSorted(a);
		show(a);
		
	}
}
