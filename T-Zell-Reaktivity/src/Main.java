import org.shogun.*;
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary("modshogun");
		modshogun.init_shogun_with_defaults();
		System.out.println("Now working? Really?");
	}

}
