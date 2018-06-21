package fileio;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.DataValid;

import dao.AccountDao;
import dao.DaoFactory;
import domain.Account;

public class AccountFileReader extends EventMgFileIO {
	static String CHECKCODE="100";

//	public static void main(String args[]) {
//		int valid_data_quantity = 5;
//		try {
//			AccountFileReader accountFileReader = new AccountFileReader("c:\\work_1\\account_20180601.csv",
//					valid_data_quantity);
//			/*
//			 *Junitを使うまではこれで接続します
//			 */
//			try {
//				JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
//			} catch (NamingException | IOException e) {
//				e.printStackTrace();
//			}
//
//			String result = accountFileReader.main();
//			System.out.println(result);
//	System.out.println(CHECKCODE);
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
	AccountFileReader(String filename, int columns) {
		super(filename, columns);
	}

	/**
	 * このクラスのメイン処理です
	 * @return String 結果コードを返却します
	 * @throws Exception
	 */

	public String main() throws Exception {

		String result = null; //結果

		List<String[]> fileRead = new ArrayList<String[]>();
//		try {
			fileRead = enableFile();//ファイル有効性チェック
//		} catch (NoSuchFileException e) {
//			result = "指定のファイルが存在しません";
//			return result;
//		}
		result = getResult(); //結果セット
		if (!result.equals(SUCCESS)) {//異常であれば終了

			return result;
		}

		// ドメインリスト
		List<Account> accountList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {

				// ドメインにセット
				Account acoData = new Account();
				//int authId = 0;

				// ログインパスワードをhash化
				String hashPass = BCrypt.hashpw(columns[3], BCrypt.gensalt());

				//authId = new Integer(Integer.parseInt(columns[4]));


				// accountsのインスタンスに格納
				acoData.setMemberId(columns[1]);
				acoData.setLoginId(columns[2]);
				acoData.setLoginPass(hashPass);
				acoData.setAuthId(new Integer(Integer.parseInt(columns[4])));

				// リストに追加
				accountList.add(acoData);

			} else {
				result = "データ有効性エラー";
				return result;
			}
		}
		//リストをDB登録
		AccountDao accountDao = DaoFactory.createAccountDao();

		// Accountリストデータをinsert
		result=accountDao.insertAcount(accountList);

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
			if (!DataValid.isNotNull(columns[i])) {
				return false;
			}
		}
		// データ項目の個別チェック
		if(!DataValid.isNum(columns[1]) || !DataValid.limitChar(columns[1], 8)) {
			CHECKCODE="205";

			return false;
		}
		if(!DataValid.limitChar(columns[2],20) || !DataValid.isAlphanum(columns[2])) {
			CHECKCODE="205";
			return false;
		}
		if(DataValid.limitChar(columns[3],8)  || !DataValid.isAlphanum(columns[3])) {
			CHECKCODE="205";
			return false;
		}
		if(!DataValid.isRange(Integer.parseInt(columns[4]),1,2)) {
			CHECKCODE="205";
			return false;
		}
		return true;

	}

}
