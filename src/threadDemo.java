
public class threadDemo extends Thread{
	public void run(){
		try {
			System.out.println("Executed first");
			sleep(10000);
			System.out.println("Executed second");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		threadDemo t=new threadDemo();
		t.start();
	}

}
