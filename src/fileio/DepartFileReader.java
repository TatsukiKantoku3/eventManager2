package fileio;

import java.util.ArrayList;
import java.util.List;

import com.DataValid;

import dao.DaoFactory;
import dao.DepartDao;
import domain.Depart;

public class DepartFileReader extends EventMgFileIO {

	static String errorCode="100";
	static final String className = new Object(){}.getClass().getEnclosingClass().getName();


	/**
	 * ファイル名と列数をセットします
	 * @param	fileName	パスを含めたファイル名
	 * @param	cols	データタイプコード列を含めた列数
	 *
	 * **/
	DepartFileReader(String filename, int columns) {
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
		List<Depart> DepartList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {
				// ドメインにセット
				Depart depData = new Depart();
				depData.setDepartment(columns[1]);
				depData.setFloor(Integer.parseInt(columns[2]));
				depData.setPosition_type(Integer.parseInt(columns[3]));
				// リストに追加
				DepartList.add(depData);
			} else {
				result = errorCode;
				logger.info(className);
				return result;
			}
		}

		// Departリストデータをinsert
		DepartDao DepartDao = DaoFactory.createDepartDao();
		result = DepartDao.insert(DepartList);

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
				errorCode="200";
				return false;
			}
		}

		//データ項目の個別チェック
		if (!DataValid.limitChar(columns[1], 50) ||
				!DataValid.isNum(columns[2]) ||
				!DataValid.limitChar(columns[3], 8) ||
				!DataValid.isAlphanum(columns[3])) {
			errorCode="200";
			return false;
		}
		return true;

	}

}
