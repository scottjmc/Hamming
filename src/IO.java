import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class IO {

	static FileReader input = null;
	static FileWriter output = null;

	static BufferedReader reader;
	static BufferedWriter writer;



	//Reads and sanitises a code from the console
	public static String getCode() {

		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		System.out.println("Enter binary string:");

		try {
			line = r.readLine();
			sanitise(line);

		} catch (Exception e) {
			throw new RuntimeException("\nINPUT STREAM EXCEPTION");
		}
		return line;
	}

	//Open a file descriptor for reading
	public static void openInput(String f) {

		try {
			input = new FileReader(f);
			reader = new BufferedReader(input);
		}
		catch (Exception e) {
			if(f == null || f == "") System.err.println("[No file descriptor]");
			throw new RuntimeException("\nBAD FILE STREAM: " + f);
		}

	}

	//Open a file descriptor for writing
	public static void openOutput(String f) {

		try {
			output = new FileWriter(f);
			writer = new BufferedWriter(output);
		}
		catch (Exception e) {
			if(f == null || f == "") System.err.println("[No file descriptor]");
			throw new RuntimeException("/nBAD FILE STREAM: " + f);
		}

	}

	//Read a binary string from the provided file.
	public static int[] read() {

		try {
			return sanitise(reader.readLine());
		} catch (Exception e) {
			return null;
		}
	}

	//Prints a file line.
	public static void println() {

		try {
			System.out.println(reader.readLine());
		} catch (IOException e) {
			System.err.println("BAD LINE");
		}

	}

	public static void write(String s) {

		try {

			s = s.replaceAll("[^0-9]", "");

			writer.write(s + "\n");
			
			writer.flush();

		} catch(Exception e) {
			System.err.println("BAD OUTPUT: " + s);
		}


	}


	//Close the reader/writer.
	public static void close() {

		try {
			
			closeInputReader();
			closeOutputWriter();

			reader.close();
			writer.close();

		} catch(IOException e) {
			System.err.println("Unable to close file.");
		} catch(NullPointerException n) {}
	}

	//sanitise a binary string.
	private static int[] sanitise(String line) {

		try {

			if(line == null || line.isEmpty()) return null;

			Integer.parseInt(line, 2);

		} catch (Exception e) {
			System.err.println("\nBAD INPUT: " + line);
			line = null;
		}

		return toCode(line);
	}

	//sanitise any binary integer code, Hamming or not.
	//Public method DOES NOT give error messages.
	public static int[] sanitise(int[] code) {

		for (int i = 0; i < code.length; i++) {

			if(code[i] != 0 && code[i] != 1) {
				return null;
			}
		}
		return code;
	}

	//Convert a string into an integer array.
	private static int[] toCode(String s)	{

		byte[] sbytes = s.getBytes();
		int[] ibytes = new int[sbytes.length];

		for (int i=0; i < sbytes.length; i++) {

			ibytes[i] =  Integer.parseInt("" + (char) sbytes[i]);
		}

		return ibytes;
	}

	private static void closeInputReader() throws IOException {
		
		reader.close();

	}


	private static void closeOutputWriter() throws IOException {

		writer.flush();
		writer.close();

	}

}
