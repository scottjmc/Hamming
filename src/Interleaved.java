import java.util.ArrayList;

public class Interleaved {

	ArrayList<Hamming> table = new ArrayList<Hamming>();

	//==========================APPEND==========================
	/*
	 * Add a Hamming code to the table.
	 * 
	 * @param h - The Hamming code to be added
	 */
	public void append(Hamming h) {

		if(h != null) table.add(h);
	}



	//==========================READ==========================
	/*
	 * Read from a column vertically, from the table.
	 * 
	 * @param column - The column to read from.
	 * @return - the code read vertically from the table.
	 */
	public int[] read(int column) {

		int[] code = new int[table.size()];

		try {

			for (int x = 0; x < table.size(); x++) {

				code[x] = table.get(x).hcode[column];
			}
		} catch(ArrayIndexOutOfBoundsException a) {return null;}

		return code;
	}

	//==========================GET=HEIGHT==========================
	/*
	 * Getter for the height of the table of codes.
	 */
	public int getHeight() {

		return table.size();
	}

}
