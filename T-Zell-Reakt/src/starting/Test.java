package starting;

import org.shogun.modshogun;

import aaEncoding.AAEncoder;
import aaEncoding.AAEncodingDatabase;
import aaEncoding.AAEncodingFileReader;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// initialisieren
		System.loadLibrary("modshogun");
		modshogun.init_shogun_with_defaults();
		
		// Lese die EncodingDB ein
		AAEncodingFileReader aa = new AAEncodingFileReader();
		
		AAEncodingDatabase db = aa.readAAEncodings("/home/johannesheidrich/AAEncodings.txt");
		System.out.println("Database size: " + db.getEncodingDatabase().size());
		
		// Teste die Transcodierung
		AAEncoder encoder = new AAEncoder();
		//ARNDCQEGHILKMFPSTWYV
		Double[] encValues = encoder.getEncodedString(db.getEncodingDatabase().get(0), "KMFPSTWYV");
		for (int i = 0; i < 9; i++)
		{
			System.out.print(encValues[i] + " ");
		}
	}

}
