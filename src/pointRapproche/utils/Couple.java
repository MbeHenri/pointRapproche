package pointRapproche.utils;

public class Couple<T>{
	private T e1;
	private T e2;
	public Couple(T e1, T e2) {
		this.e1=e1;
		this.e2=e2;
	}
	public T getE1() {
		return e1;
	}
	public void setE1(T e1) {
		this.e1 = e1;
	}
	public T getE2() {
		return e2;
	}
	public void setE2(T e2) {
		this.e2 = e2;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{"+e1.toString()+", "+e2.toString()+"}";
	}
}
