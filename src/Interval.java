public class Interval {
	private long time;
	
	public Interval(long t) {
		time = t;
	}
	public void sleep() {
		try {
			Thread.sleep((long) (time* 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
