package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

public class DecyzjaPomocnik {
	public static double sum(double[] arr) {
		double s = 0;
		
		for (double d : arr) {
			s += d;
		}
		
		return s;
	}
	
	public static double avg(double[] arr) {
		return sum(arr) / arr.length;
	}
}
