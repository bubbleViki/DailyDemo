package day_04.sort.algorithm;

public class SimpleSort {
	public static void selectSort(Comparable[] a){
		int N=a.length;
		
		for(int i=0;i<N;i++){
			//���õ�ǰλ��Ϊmin
			int min=i;
			
			//ʣ��������Ѱ��min
			for(int j=i+1;j<N;j++){
				if(less(a[j],a[min])) min=j; 
			}
			
			//��ǰλ�����>ʣ��Ԫ�����ҵ���minԪ�ؽ���λ��
			exch(a,i,min);
		}
	}
	
	public static void insertSort(Comparable[] a){
		int N=a.length;
		for(int i=1;i<N;i++){
			
			for(int j=i;j>0&&less(a[j],a[j-1]);j--){ //��ǰ����λ��С�����Ԫ�أ��ҵ�ǰԪ�ز�Ϊ��Ԫ��
				exch(a,j,j-1);
			}
		}
	}
	
	
	public static void shellSort(Comparable[] a){
		int N=a.length;
		//���ó�ʼ����ֵ
		int h=a.length/3;
		while(h>=1){
			for(int i=h;i<N;i++){
				
				//��������
				for(int j=i;j>=h&&less(a[j],a[j-h]);j-=h){ //���ұߣ���ǰ������С����ߣ����ұ߲�Ϊ��Ԫ��
					exch(a,j,j-1);
				}
			}
				
			h=h/3;
		}
		
		
	}
	
	//Ԫ�ؽ���
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
			if(less(a[i],a[i-1])) //�����һ��С��ǰһ������false
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
