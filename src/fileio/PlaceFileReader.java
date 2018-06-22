package fileio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.DataValid;

import dao.DaoFactory;
import dao.PlaceDao;
import domain.Place;

public class PlaceFileReader extends EventMgFileIO {


	static String errorCode="100";
	static final String className = new Object(){}.getClass().getEnclosingClass().getName();

	/**
	 * ファイル名と列数をセットします
	 * @param	fileName	パスを含めたファイル名
	 * @param	cols	データタイプコード列を含めた列数
	 *
	 * **/
	PlaceFileReader(String filename, int columns){
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

		fileRead = enableFile();

		//ファイル有効性チェック
		result = getResult(); //結果セット
		if (!result.equals(SUCCESS)) {//異常であれば終了
			logger.info(className);
			return result;
		}

		// ドメインリスト
		List<Place> PlaceList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {
				// ドメインにセット
				Place acoData = new Place();
	            SimpleDateFormat sdFormat = new SimpleDateFormat("hh:mm");
	            Date locktime;

				locktime = sdFormat.parse(columns[7]);
				acoData.setPlace(columns[1]);
				acoData.setCapa(new Integer(Integer.parseInt(columns[2])));
				acoData.setEqu_mic(new Integer(Integer.parseInt(columns[3])));
				acoData.setEqu_whitebord(new Integer(Integer.parseInt(columns[4])));
				acoData.setEqu_projector(new Integer(Integer.parseInt(columns[5])));
				acoData.setAdmin_id(new Integer(Integer.parseInt(columns[6])));
				acoData.setLocking_time(locktime);

				// リストに追加
				PlaceList.add(acoData);

			} else {
				result =errorCode;
				logger.info(className);
				return result;
			}

			}
		//リストをDB登録
		PlaceDao PlaceDao=DaoFactory.createPlaceDao();
		result=PlaceDao.insert(PlaceList);
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
				return false;
			}
		}

		//データ行のチェック
		if (!DataValid.limitChar(columns[1], 20) ||
				!DataValid.limitChar(columns[6], 8) ||
				!DataValid.isTimeFormat(columns[7]) ||
				!(columns[3].equals("0") || columns[3].equals("1")) ||
				!(columns[4].equals("0") || columns[4].equals("1")) ||
				!(columns[5].equals("0") || columns[5].equals("1"))) {

			errorCode="200";
			return false;

		}
		return true;

	}

}
