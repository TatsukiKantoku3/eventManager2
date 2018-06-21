package fileio;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import com.DataValid;
import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

import dao.DaoFactory;
import dao.PlaceDao;
import domain.Place;

public class PlaceFileReader extends EventMgFileIO {


	public static void main(String args[]) {
		int valid_data_quantity = 8;
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader("C:\\work_1\\place_20180601.csv",
					valid_data_quantity);

			/*
			 *Junitを使うまではこれで接続します
			 */
			try {
				JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
			} catch (NamingException | IOException e) {

				e.printStackTrace();
			}

			String result = PlaceFileReader.main();

			System.out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ファイル名と列数をセットします
	 * @param	fileName	パスを含めたファイル名
	 * @param	cols	データタイプコード列を含めた列数
	 *
	 * **/
	PlaceFileReader(String filename, int columns) throws NoSuchFileException {
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
				result = "データ有効性エラー";
				return result;
			}

			}
		//リストをDB登録

		try {
			PlaceDao PlaceDao=DaoFactory.createPlaceDao();
			result=PlaceDao.insert(PlaceList);
		}catch(Exception e) {
			e.printStackTrace();
			result = "DB接続エラー";
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
			return false;

		}
		return true;

	}

}
