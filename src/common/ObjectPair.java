package common;

public class ObjectPair <K,V>{
	public K key;
	public V value;
	public ObjectPair(K key,V value) {
		this.key = key;
		this.value = value;
	}
	@Override
	public String toString() {
		return "ObjectPair [key=" + key + ", value=" + value + "]";
	}
	
	
}
