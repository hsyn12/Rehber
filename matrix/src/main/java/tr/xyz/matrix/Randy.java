package tr.xyz.matrix;


import java.util.random.RandomGenerator;


public class Randy {
	
	private final RandomGenerator gen = RandomGenerator.getDefault();
	
	public int[] randIntArray(int n) {
		
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = gen.nextInt();
		}
		return arr;
	}
	
	public int[] randIntArray(int n, int min, int max) {
		
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = gen.nextInt(min, max);
		}
		return arr;
	}
	
	public double[] randDoubleArray(int n) {
		
		double[] arr = new double[n];
		for (int i = 0; i < n; i++) {
			arr[i] = gen.nextDouble();
		}
		return arr;
	}
	
	public double[] randDoubleArray(int n, double min, double max) {
		
		double[] arr = new double[n];
		for (int i = 0; i < n; i++) {
			arr[i] = gen.nextDouble(min, max);
		}
		return arr;
	}
}
