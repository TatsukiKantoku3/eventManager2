package fileio;

import java.util.ResourceBundle;

import com.OutputLog;
public class FileController extends OutputLog {

	public static String member(String fileName) throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		MemberFileReader member = new MemberFileReader((String) rb.getString(fileName),
				Integer.parseInt(rb.getString(fileName + "column")));

		code = member.main();
		output();

		return code;
	}

	public static String account(String fileName) throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		AccountFileReader account = new AccountFileReader((String) rb.getString(fileName),
				Integer.parseInt(rb.getString(fileName + "column")));

		code = account.main();
		output();

		return code;
	}

	public static String place(String fileName) throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		PlaceFileReader place = new PlaceFileReader((String) rb.getString(fileName),
				Integer.parseInt(rb.getString(fileName + "column")));

		code = place.main();
		output();

		return code;
	}

	public static String depart(String fileName) throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		DepartFileReader depart = new DepartFileReader((String) rb.getString(fileName),
				Integer.parseInt(rb.getString(fileName + "column")));

		code = depart.main();
		output();

		return code;
	}

}