import java.util.ArrayList;
import java.util.Arrays;

public class Hamming {

	private int length; //Total code length.
	private int dbits; //Number of data bits.
	private int pbits; //Number of parity bits.

	public int[] hcode; //The Hamming code.

	static ArrayList<Integer> powers = new ArrayList<Integer>();

	//==========================CONSTRUCTOR==========================
	/*
	 * Constructor for Hamming object.
	 * Initialises class-wide variables.
	 * Encodes input data as public variable hcode.
	 * 
	 * @exception BadCodeException - for null or empty codes.
	 * @param data - raw input code in binary as integer array.
	 */
	public Hamming(int[] data) throws BadCodeException	{

		if(data == null || data.length == 0) 
			throw new BadCodeException(); 

		dbits = data.length;


		init();

		encode(data);
	}

	//==========================CONSTRUCTOR==========================
	/*
	 * Constructor for Hamming object.
	 * Private, for use only by toHamming method.
	 * Purely used to instantiate a hamming object without encoding.
	 */	
	private Hamming() {

		setBits();
	}


	//==========================INITIALISE==========================
	/*
	 * Initialises private variables as helper method for constructor.
	 * Sets the number of parity bits, data bits and total bits for this Hamming.
	 * Also initialises a new array to store the Hamming code that will be generated.
	 */
	//Calculate the distribution of parity bits and data bits and the minimum length.
	private void init() {

		pbits = 1; //Counts the minimum number of parity bits needed to store our data.

		while(true) {

			//If the all the data bits can be contained by 2^n - n, we only need n parity bits.
			if((dbits+1) <= (Math.pow(2, pbits) - pbits)) break;
			else pbits++; //Otherwise we need more parity bits.
		}


		length = pbits + dbits; //Set our length equal to the sum of parity and data.

		if(pbits > powers.size()) {

			for (int i = powers.size(); i <= pbits; i++) {

				powers.add(new Integer((int)Math.pow(2, i)));

			}
		}
		hcode = new int[length]; //Initialse our Hamming code to this size.
	}


	//==========================ENCODE==========================
	/*
	 * Encodes a binary integer array into a Hamming code.
	 * 
	 * @param data - raw input code in binary as integer array.
	 * @return hcode - a copy of this class's Hamming code just generated.
	 */
	private int[] encode(int[] data) {

		//Stores current power of 2 for parity addresses.
		int pow = 0;

		//Iterate through the entire Hamming code.
		//Fill in the values of the data bits.
		for (int i = 0, pos = 0; i < length; i++) {

			//If we encounter a parity's address, skip it.
			if(i == Math.pow(2, pow) - 1) {
				pow++; //Next parity will be the next power of 2.
				continue; //Skip this address.
			}

			//Otherwise, data bit address.
			hcode[i] = data[pos]; //Fill in with next data bit from code.
			pos++; //Continue through the input data array.
		}


		pow = 0; //Reset pow to traverse array again.

		//Iterate through the entire Hamming code again.
		//This time, calculate the value of the parity bits.
		for (int i = 0; i < length; i++) {

			//Calculate the next power of 2 to watch out for.
			//Used both by addressing and parity calculations.
			int nextp = (int) Math.pow(2, pow);

			if(nextp > powers.get(powers.size()-1).intValue()) powers.add(new Integer(nextp));

			//Find the parity bits addresses.
			if(i == nextp -1) {

				int j = i; //Holds location in array currently, for reference.

				int ones = 0; //Count the number of '1' bits seen.

				while(j < length) { //While inside the whole array:

					//Establish check/skip loop.
					for (int read = 0; (read < nextp)&&(j<length); read++, j++) {

						//If the checked bit is a 1, add it to our counter.
						if(hcode[j] == 1) ones++;
					}

					j += nextp; //Append the power of two to skip to the next check bit.
				}

				//The 'ones' counter currently contains an odd or even number of '1's.
				hcode[i] = ones % 2; //Perform modulus to get a single bit representing odd/even.
				pow++; //Increase the power counter ready for our next power of 2.
			}
		}

		return hcode;
	}



	//==========================DECODE==========================
	/*
	 * Checks an incoming Hamming code for errors and strips the data bits.
	 * Calls external method to correct any errors found.
	 * 
	 * @param h - the Hamming code to be decoded.
	 * @return data - the decoded and corrected data.
	 */

	//Decode an encoded Hamming code and potentially call error correction.
	public int[] decode() {
		
		//Ensure we have the right quantities of parity and data bits stored.
		if(dbits == 0 || pbits == 0) setBits();

		//Call correct on the code.
		//This will fix up to 1 error per code word.
		//This will only return error = true if it hasn't resolved the error.
		boolean error = correct();
		
		//If there was a 2-bit error (aka unresolved):
		if(error) {System.err.println("\n[UNRESOLVED] - MULTIPLE BIT ERROR");}


		int[] data = new int[dbits];

		int pow = 0; //Stores current power of 2;
		int pos = 0; //Position in data array.

		for (int i = 0; i < hcode.length; i++) {

			if(i+1 == powers.get(pow)) { //If it's a parity bit.

				pow++; //Increase power for next parity bit.
				continue; //Skip this location for now.
			}

			else data[pos++] = hcode[i]; //Otherwise store that data bit.
		}

		/*
		 * Attempt to recover data by removing parity bits.
		 * This only works if the data bits were unaffected.
		 * In Hamming 7,4 this has a 3/7 chance of success.
		 */
		if(error) System.err.println("ATTEMPTED RECOVERY: " + toString(data) + "\n");

		return data;
	}

	//==========================CORRECT==========================

	private boolean correct() {

		int pow = 0; //Set pow to hold exponent of 2.
		int bad = 0; //Address of bad bit.

		boolean error = false;

		//Iterate through the entire Hamming code.
		//Re-calculate the value of the parity bits, and check them.
		for (int i = 0; i < length; i++) {

			//Calculate the next power of 2 to watch out for.
			//Used both by addressing and parity calculations.
			int nextp = (int) Math.pow(2, pow);

			//Find the parity bits addresses.
			if(i == nextp -1) {

				int j = i; //Holds location in array currently, for reference.

				int ones = 0; //Count the number of '1' bits seen.

				while(j < length) { //While inside the whole array:

					//Establish check/skip loop.
					for (int read = 0; (read < nextp)&&(j<length); read++, j++) {

						if(powers.contains(new Integer(j+1))) continue;

						//If the checked bit is a 1, add it to our counter.
						if(hcode[j] == 1) ones++;
					}

					j += nextp;
				}

				//The 'ones' counter currently contains an odd or even number of '1's.
				int expected = ones % 2; //Perform modulus to get a single bit (odd/even).


				if(hcode[i] != expected) {

					bad += (i+1);
					error = true;
				}
				pow++; //Increase the power counter ready for our next power of 2.
			}
		}

		if(error) {

			bad--;
			System.out.println("[RESOLVED] BAD BIT AT: " + bad);

			if(bad < length) hcode[bad] = (hcode[bad] == 0) ? 1 : 0;
		}

		return error;
	}

	//==========================TO=STRING==========================
	/*
	 * 
	 * 
	 */
	private void setBits() {

		if(hcode == null || hcode.length == 0) return;

		while(true) {

			if(Math.pow(2, pbits) -1 < length) pbits++;
			else break;
		}

		dbits = length - pbits;


		if(pbits > powers.size()) {

			for (int i = powers.size(); i <= pbits; i++) {
				powers.add(new Integer((int)Math.pow(2, i)));
			}
		}
	}


	//==========================TO=STRING==========================
	/*
	 * Two methods to convert this Hamming code - or any int array to a string.
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		
		return Arrays.toString(this.hcode);
	}

	public static String toString(int[] code) {

		return Arrays.toString(code);
	}


	//==========================TO=HAMMING==========================
	/*
	 * Turn a Hamming code into an actual Hamming object.
	 * 
	 * @param hcode - The Hamming's hcode field.
	 * @return - The Hamming code created.
	 */
	public static Hamming toHamming(int[] hcode) {

		if(hcode == null) throw new BadCodeException();

		Hamming h = new Hamming();

		h.hcode = hcode;
		h.length = hcode.length;
		return h;
	}
}
