package day_02.java.generics;

//泛型容器
//参数K,V类型未定死
//运行时根据真正的类型来构造和分配内存
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
