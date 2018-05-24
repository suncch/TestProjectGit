package demo;

//饿汉
public class A {
	private A(){
		System.out.println("11");
	}
	private static A a = null;
	public static A getA() {
		if(a==null){
			a=new A();
		}
		return a;
	}
	
}