package fileio;

import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.DataValid;

import dao.DaoFactory;
import dao.MembersDao;
import domain.Members;

public class MemberFileReader extends EventMgFileIO {
	private String fileName;
	static String errorCode="100";
	static final String className = new Object(){}.getClass().getEnclosingClass().getName();


//	public static void main(String args[]) {
//		int valid_data_quantity = 9;
//		try {
//			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\Member_20180601.csv",
//					valid_data_quantity);
//
//			/*
//			 *Junitを使うまではこれで接続します
//			 */
//			try {
//				JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
//			} catch (NamingException e) {
//				e.printStackTrace();
//			}
//
//			String result = MembersFileReader.main();
//			System.out.print(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * ファイル名と列数をセットします
	 * @param	fileName	パスを含めたファイル名
	 * @param	cols	データタイプコード列を含めた列数
	 *
	 * **/
	MemberFileReader(String filename, int columns) {
		super(filename, columns);
		this.fileName=filename;
	}
	/**
	 * このクラスのメイン処理です
	 * @return String 結果コードを返却します
	 * @throws NoSuchFileException
	 */
	public String main() throws NoSuchFileException {

		String result = null; //結果

		List<String[]> fileRead = new ArrayList<String[]>();

			fileRead = enableFile();//ファイル有効性チェック

		result = getResult(); //結果セット
		if (!result.equals(SUCCESS)) {//異常であれば終了
			return result;
		}

		// ドメインリスト
		List<Members> MembersList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {

				// ドメインにセット
				Members domain = new Members();
				Date birthday =null;
				Date hired = null;

				// 誕生日の型をDate型に変換
				try {
					DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
					birthday = df.parse(columns[4]);

				// 入社日をDate型に変換
					String fileNames[] = fileName.split("_",0);
					String year = fileNames[2].substring(0, 4) + "年";
					DateFormat df2 = new SimpleDateFormat("yyyy年M月d日");
					hired = df2.parse(year + columns[7]);
				} catch (ParseException p) {
					result="201";

					 logger.error(className+p.getMessage());
					return result;
				}

				// memberのインスタンスに値を格納
				domain.setMember_id(columns[1]);
				domain.setName(columns[2]);
				domain.setKana(columns[3]);
				domain.setBirthday(birthday);
				domain.setAddress(columns[5]);
				domain.setTel(columns[6]);
				domain.setHired(hired);
				domain.setDep_id(Integer.parseInt(columns[8]));

				//--リストに追加
				MembersList.add(domain);
			} else {
				result = "データ有効性エラー";
				 logger.error(className);
				return result;
			}
		}
		//リストをDB登録
		try {
			MembersDao MembersDao = DaoFactory.createMembersDao();
			result = MembersDao.insertMast(MembersList);

		} catch (Exception e) {
			e.printStackTrace();
			result = "DB接続エラー";

			 logger.error(className+e.getMessage());
			return result;
		}

		return result;
	}

	/**
	 * データ有効性チェック
	 * String型配列に格納したファイルのD行の
	 * データが有効かであるか検査します
	 *
	 * @return	処理結果を返却します
	 * @param	columns	ファイルのD行
	 *			index0には"D"が格納されています
	 *			検査対象はindex1からになります
	 * **/
	protected boolean enableLine(String[] columns) {

		// データ行の列で空のデータがないか
		for (int i = 1; i < columns.length; i++) {
			//空のデータがあれば終了
			if (!DataValid.isNotNull(columns[i]) && i!=3) {
				errorCode="203";
				return false;
			}
		}
		// データ項目の個別チェック
		if(!DataValid.limitChar(columns[1],8)) {
			errorCode="205";
			return false;
		}//"INCORRECT_FORMAT_ERROR";
		if(!DataValid.limitChar(columns[2], 50)) {
			errorCode="205";
			return false;
		}//return "INCORRECT_FORMAT_ERROR";
		if(!DataValid.isKana(columns[3])) {
			errorCode="204";
			return false;
		}//return "INCORRECT_FORMAT_ERROR";
		if(!DataValid.limitChar(columns[3],50)) {
			errorCode="205";
			return false;
		}//"INCORRECT_FORMAT_ERROR";
		if(!DataValid.isDateFormat(columns[4],"yyyy/M/d")) {
			errorCode="200";
			return false;
		}//"DATE_FORMAT_ERROR";
		if(!DataValid.isTelFormat(columns[6])) {
			errorCode="200";
			return false;
		}//"INCORRECT_FORMAT_ERROR";
		if(!DataValid.isDateFormat(columns[7], "M月d日")) {
			errorCode="200";
			return false;
		}//"DATE_FORMAT_ERROR";
		if(!DataValid.isRange(Integer.parseInt(columns[8]),1,5)) {
			errorCode="200";
			return false;
		}// "OUT_OF_INDEX_ERROR";

		return true;

	}

}
