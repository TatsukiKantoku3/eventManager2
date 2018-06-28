package fileio;


import java.util.ArrayList;
import java.util.List;

import com.DataValid;

import dao.AccountDao;
import dao.DaoFactory;
import domain.Account;

public class AccountFileReader extends EventMgFileIO {
	static String CHECKCODE="100";
	static final String className = new Object(){}.getClass().getEnclosingClass().getName();

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

		fileRead = enableFile();//ファイル有効性チェック

		result = getResult(); //結果セット
		if (!result.equals(SUCCESS)) {//異常であれば終了
			logger.info(className);
			return result;
		}

		// ドメインリスト
		List<Account> accountList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {

				// ドメインにセット
				Account acoData = new Account();

				// accountsのインスタンスに格納
				acoData.setMemberId(columns[1]);
				acoData.setLoginId(columns[2]);
				acoData.setLoginPass(columns[3]);
				acoData.setAuthId(new Integer(Integer.parseInt(columns[4])));

				// リストに追加
				accountList.add(acoData);

			} else {
				result = CHECKCODE;
				logger.info(className);
				return result;
			}
		}
		//リストをDB登録
		AccountDao accountDao = DaoFactory.createAccountDao();

		// Accountリストデータをinsert
		result=accountDao.insertAcount(accountList);
		logger.info(className);
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
				CHECKCODE="205";
				return false;
			}
		}
		// データ項目の個別チェック
		if(!DataValid.limitChar(columns[1], 8) || !DataValid.chkLiteAndNum(columns[1])) {
			CHECKCODE="205";

			return false;
		}
		if(!DataValid.limitChar(columns[2],20) || !DataValid.isAlphanum(columns[2])) {
			CHECKCODE="205";
			return false;
		}
		if(DataValid.limitChar(columns[3],7)  || !DataValid.chkLiteAndNum(columns[3])) {
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
