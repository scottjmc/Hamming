import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	final static String RAW = "resources/raw.txt";//Raw data
	final static String ENC = "resources/encoded.txt";//Encoded data
	final static String DEC = "resources/decoded.txt";//Decoded data
	final static String INTR = "resources/interleaved.txt";//Interleaved data
	final static String NOISE = "resources/noise.txt";//Noisy encoded data.

	//==========================MAIN==========================
	/*
	 * Main method for entire program.
	 * Keeps track of total process time elapsed.
	 */
	public static void main(String[] args) {

		long start = System.nanoTime();
		//======================================================
		try { //Add try/catch for any runtime exception.

			Noise.set(0.1, 0.9, 0.1);



			randomFile(RAW,4,10);
			encodeFile(RAW, ENC);
			interleave(7, ENC, INTR);
			addNoise(INTR,NOISE);
			interleave(7, NOISE, INTR);
			decodeFile(INTR, DEC);



		} catch(RuntimeException e) {
			long end = (System.nanoTime() - start)/1000000;
			System.err.println("\tRun unsuccessful. (" + end + "ms)");
			e.printStackTrace();
		}
		//======================================================
		long end = (System.nanoTime() - start)/1000000;
		int errors = countErrors(RAW, DEC);
		System.out.println("\tRun successful. (" + end + "ms)");
		System.out.println("\tNumber of errors: " + errors);
	}


	//==========================INTERLEAVE==========================
	/*
	 * An abstraction to generate Interleaved tables.
	 * 
	 * @param length - The length of an interleaved message.
	 * @param in - The file to be read from.
	 * @param out - The file to write to.
	 */
	public static void interleave(int length, String in, String out) {

		Interleaved inter;

		IO.openInput(in);
		IO.openOutput(out);

		int[] code = IO.read();
		int[] leaved = new int[length];

		while(code != null) {

			inter = new Interleaved();

			for (int i = 0; i < length; i++) {

				inter.append(Hamming.toHamming(code));
				code = IO.read();
				if(code == null) break;
			}

			for (int i = 0; i < length; i++) {

				leaved = inter.read(i);
				IO.write(Hamming.toString(leaved));
			}
		}

		IO.close();
	}


	//==========================DECODEFILE==========================
	/*
	 * An abstraction to decode every Hamming code in a file.
	 * Outputs to a new file of decoded Hamming codes.
	 * 	 
	 * @param in - Input file
	 * @param out - Output file
	 */
	public static void decodeFile(String in, String out) {

		IO.openInput(in);
		IO.openOutput(out);

		int[] input = IO.read();
		Hamming h;


		while(input != null && input.length > 1) {

			h = Hamming.toHamming(input);

			IO.write(h.toString(h.decode()));

			input = IO.read();
		}

		IO.close();
	}

	//==========================ENCODE=FILE==========================
	/*
	 * An abstraction to encode an entire file of raw data into Hamming codes.
	 * Outputs to a new file for encoded Hamming files.
	 * 
	 * @param in - Input file
	 * @param out - Output file
	 */
	public static void encodeFile(String in, String out) {

		IO.openInput(in);
		IO.openOutput(out);

		int[] code = IO.read();

		while(code != null) {

			Hamming h = new Hamming(code);

			IO.write(h.toString());

			code = IO.read();
		}

		IO.close();
	}

	//==========================ADD=NOISE==========================
	/*
	 * Takes a file, adds noise to every code in that file.
	 * Noise adjustable by vars:
	 * 
	 * @param pgb - Probability of channel going from good to bad.
	 * @param pbg - Probability of channel going from bad to good.
	 * @param pflip - Probability of a bit being flipped when channel is bad.
	 * @param in - Input file.
	 * @param out - Output file.
	 */
	public static void addNoise(String in, String out) {

		IO.openInput(in);
		IO.openOutput(out);

		int[] code = IO.read();


		while(code != null) {

			code = Noise.affect(code);

			IO.write(Hamming.toString(code));

			code = IO.read();

		}

		IO.close();
	}



	//==========================RANDOM=CODE==========================
	/*
	 * An abstraction to encode an entire file of raw data into Hamming codes.
	 * Outputs to a new file for encoded Hamming files.
	 */
	public static int[] randomCode(int codelength) {

		int[] code = new int[codelength];


		for (int i = 0; i < codelength; i++) {
			code[i] = (int) (Math.random() *2);

		}
		return code;
	}




	//==========================RANDOM=FILE==========================
	/*
	 * Fill a file with random codes.
	 * 
	 * @param out - file to write to.
	 * @param codelength - length of each code.
	 * @param filelength - length of the file.
	 */
	public static void randomFile(String out, int codelength, int filelength) {

		IO.openOutput(out);

		for (int i = 0; i < filelength; i++) {

			String s = Hamming.toString(randomCode(codelength));
			IO.write(s);
		}

		IO.close();
	}

	//==========================SET=ARGS==========================
	/*
	 * Takes command line args and sets them to channel noise args.
	 * 
	 * @param args - Command line args.
	 */
	public void setArgs(String[] args) {

		if(args == null) return;

		if(args[0] == null || args[1] == null || args[2] == null) return;

		Noise.setPGB(Double.parseDouble(args[0]));;
		Noise.setPBG(Double.parseDouble(args[1]));
		Noise.setPFLIP(Double.parseDouble(args[2]));
	}



	public static int countErrors(String pre, String post) {

		int errors = 0;

		int buffersize = 100;

		ArrayList<int[]> buffer = new ArrayList<int[]>();

		IO.openInput(pre);

		for (int i = 0; i < buffersize; i++) {

			int[] code = IO.read();
			if(code == null) break;

			buffer.add(code);
		}

		IO.close();
		IO.openInput(post);

		for (int i = 0; i < buffersize; i++) {

			int[] code = IO.read();
			if(code == null) break;

			if(!(Arrays.equals(buffer.get(i), code))) errors ++;
		}

		IO.close();
		return errors;
	}

}
