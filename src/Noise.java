
public abstract class Noise {

	private static boolean reliable = true; //Condition of the channel.

	private static double pgb; //Probability of a channel going from Good->Bad 
	private static double pbg; //Probability of a channel going from Bad -> Good
	private static double pflip; //Probability of a bit flipping when in a bad state.



	public static int[] affect(int[] code) {
		

		for (int i = 0; i < code.length; i++) {

			//If we're reliable, roll reliability and continue
			if(reliable) reliable = (roll(pgb)) ? false : true;

			if(!reliable) { //If unreliable, chance of bit-flip

				if(pflip != 0 && (pflip == 1 || !roll(pflip))) { //If we roll on the probability of a flip
					
					int x = code[i]; //Take our current bit
					x = (x==0) ? 1 : 0; //Flip it to its inverse
					code[i] = x; //Reassign it to the array
				}
				
				reliable = (roll(pbg)) ? true : false; 
			}
		}
		return code;
	}
	

	private static boolean roll(double probability) {

		if(probability == 0) return false;
		if(probability == 1) return true;
		return (Math.random() < probability) ? false : true;
	}


	public static boolean getState() {

		return reliable;
	}
	
	public static void setState(boolean state) {
		
		reliable = state;
	}

	public static void setNoiseRandom() {

		pgb = Math.random();
		pbg = Math.random();
		pflip = Math.random();


	}

	public static void setPGB(double pgb) {

		if(pgb < 0 || pgb > 1) {
			System.err.println("Probability of Good->Bad should be 0<P<1");
			return;
		}
		
		Noise.pgb = pgb;
	}

	public static void setPBG(double pbg) {

		if(pbg < 0 || pbg > 1) {
			System.err.println("Probability of Bad->Good should be 0<P<1");
			return;
		}
		
		Noise.pbg = pbg;
	}

	public static void setPFLIP(double pflip) {

		if(pflip < 0 || pflip > 1) {
			System.err.println("Probability of flipping bit should be 0<P<1");
			return;
		}

		Noise.pflip = pflip;
	}

	public static void set(double pgb, double pbg, double pflip) {

		setPGB(pgb);
		setPBG(pbg);
		setPFLIP(pflip);
	}
	
	public static void setRandom() {
		
		setPGB(Math.random());
		setPBG(Math.random());
		setPFLIP(Math.random());
		
	}
	
	public static void printProbabilities() {
		
		System.out.println("Probability Good->Bad: " + pgb);
		System.out.println("Probability Bad->Good: " + pbg);
		System.out.println("Probability of bit-flip: " + pflip);
		
	}

}
