// import java.util.Arrays;

// public class Test {

	
// //==========================SMOKE=TEST==========================
// 	/*
// 	 * Tests typically destructive inputs to see if the program tolerates them.
// 	 * Tests (1 & 2) - a null or empty input created a BadCodeException as expected.
// 	 */
// 	@org.junit.Test(expected=BadCodeException.class)
// 	public void smokeTest() {
		
// 		int[] nullcode = null;
		
// 		Hamming h = new Hamming(nullcode);
		
// 		int[] emptycode = {};
		
// 		h = new Hamming(emptycode);
// 	}
	
	
// //==========================TYPE=TEST==========================
// 	/*
// 	 * Tests the IO classes sanitser function.
// 	 * Tests (1) to ensure a null replaces any incoming bad code.
// 	 */
// 	@org.junit.Test
// 	public void typeTest() {
		
// 		int[] test = {1,((int)'F'),9,49};
// 		test = IO.sanitise(test);
// 		assert(test == null);
// 	}
	
	
// //==========================HAMMING=ENCODE=TEST==========================
// 	/*
// 	 * Test the Hamming encoder for functionality.
// 	 * Tests (1) If a raw code is converted into the expected Hamming code.
// 	 * Tests (2) IF a longer code is also encoded correctly.
// 	 */
// 	@org.junit.Test
// 	public void hammingEncodeTest() {
		
// 		//TEST #1
// 		int[] test = {1,0,1,1};
// 		int[] expected = {0,1,1,0,0,1,1};
// 		Hamming h = new Hamming(test);
// 		assert(Arrays.equals(h.hcode, expected));
		
		
// 		//TEST #2
// 		int[] test2 = {1,0,0,1,1,0,1,0};
// 		int[] expected2 = {0,1,1,1,0,0,1,0,1,0,1,0};
		
// 		Hamming h2 = new Hamming(test2);
		
// 		assert(Arrays.equals(h2.hcode, expected2));	
// 	}
	
// //==========================HAMMING=DECODE=TEST==========================
// 	/*
// 	 * 
// 	 * 
// 	 */
// 	@org.junit.Test
// 	public void hammingDecodeTest() {
		
// 		//TEST#1
// 		int[] data = {0,1,1,1};
		
// 		Hamming h = new Hamming(data);
		
// 		int[] decoded = h.decode();
		
// 		assert(Arrays.equals(data, decoded));
		
		
// 		//TEST#2
// 		int[] data2 = {1,0,1,1}; //Input data
// 		int i = 4; //Index for noise.
		
// 		Hamming h2 = new Hamming(data2);
		
// 		h2.hcode[i] = (h2.hcode[i] == 1) ? 0 : 1; //Flip the bit at address i.
// 		int[] decoded2 = h2.decode();
		
// 		assert(Arrays.equals(data2, decoded2));
		
		
// 		//TEST #2
// 		int[] data3 = {1,1,0,0}; //Input data
		
// 		i = 0; //Index for noise.
		
// 		Hamming h3 = new Hamming(data3);
		
// 		h3.hcode[i] = (h3.hcode[i] == 1) ? 0 : 1; //Flip the bit at address i.
// 		int[] decoded3 = h3.decode();
		
// 		assert(Arrays.equals(data3, decoded3));
		
		
		
		
// 	}
	
// //==========================TO=HAMMING=TEST==========================
// 	/*
// 	 * Tests to make sure Hamming objects are made from int arrays.
// 	 * Tests (1) an external int[] equals the hcode of the new Hamming.
// 	 */
// 	@org.junit.Test
// 	public void toHammingTest() {
		
// 		int[] hcode = {0,1,1,0,0,1,1};
		
// 		Hamming h = Hamming.toHamming(hcode);
		
// 		assert(Arrays.equals(hcode, h.hcode));
// 	}
	
// //==========================NOISE=TEST(GOOD)==========================
// 	/*
// 	 * Test the channel when it's in a reliable or good state.
// 	 * Tests (1) whether the channel gets set to a good state.
// 	 * Tests (2) whether a code remains unchanged when the channel is good.
// 	 */
// 	@org.junit.Test
// 	public void noiseTestGood() {
		
// 		//Set the channel to be in a reliable state.
// 		Noise.setState(true);
		
// 		//Set pgb, pbg and pfip (probabilities).
// 		Noise.set(0, 0, 1);
// 		/*
// 		 * Channel has 0% chance of going to bad state.
// 		 * Channel cannot recover from bad state (0% chance).
// 		 * Channel will flip 100% of bits in bad state.
// 		 */
		
// 		int[] input = {1,0,1,1,1,1,1};
// 		int[] inputClone = input.clone();
		
		
// 		//This should have no effect, as channel shouldn't ever go to bad state.
// 		Noise.affect(input);
		
// 		assert(Noise.getState());
		
// 		//Assert the channel does not change anything in good state.
// 		assert(Arrays.equals(input, inputClone));
// 	}
	
	
// //==========================NOISE=TEST=(BAD)==========================
// 	/*
// 	 * Test the channel when it's in an unreliable or bad state.
// 	 * Tests (1) whether the channel gets set to a bad state.
// 	 * Tests (2) whether noise is actually introduced into a code.
// 	 */
// 	@org.junit.Test
// 	public void noiseTestBad() {
		
// 		//Set the channel to be in an unreliable state.
// 		Noise.setState(false);
		
// 		//Set the probabilities to maintain bad.
// 		Noise.set(1, 0, 1);
// 		/*
// 		 * Channel has a 100% chance to go immediately from good to bad.
// 		 * Channel has a 0% chance of going back to good.
// 		 * Channel has a 100% chance to flip any bit.
// 		 */
		
// 		int[] input = {1,1,1,0};
		
// 		input = Noise.affect(input);
		
// 		int[] expected = {0,0,0,1};
		
// 		//Channel should now be in an unreliable state.
// 		assert(!Noise.getState()); 

// 		//Channel should flip EVERY bit to its inverse.
// 		assert(Arrays.equals(input, expected)); 
// 	}
	

// }
