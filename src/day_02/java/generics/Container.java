package day_02.java.generics;

//��������
//����K,V����δ����
//����ʱ��������������������ͷ����ڴ�
public class Container<K,V> {
	private K key;
	private V value;
	
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Container(K k,V v){
		key=k;
		value=v;
	}
	
	
	
}
