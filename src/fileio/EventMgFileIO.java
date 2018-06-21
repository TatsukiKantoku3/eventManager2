package fileio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.OutputLog;
/**
 *  このクラスはCSVファイルの読み込みと
 *  ファイル共通のファイル有効性検査を行うクラスです
 *  ファイル有効性検査はファイルが取り込み可能であるか検査します
 *  検査結果はフィールドresultにセットされ
 *  呼び出し元のクラスではsetResult()によって結果を取得できます
 *  @version 1.0
 *  @author ICG
 */

public abstract class EventMgFileIO extends OutputLog {
	/** ファイル名 インスタンス時に外部からセットします*/
	private String fileName;
	/** データタイプコードを含めた列数 インスタンス時に外部からセットします*/
	private int cols;
	/** データ項目数 ファイル取り込み時に指定されます*/
	private String result;
	/** D行を格納する */
	private List<String[]> dataList;
	/** 正常結果値100 */
	protected static final String SUCCESS = "100";
	/** ファイルの開始行 */
	private static final String START_ROW = "S";
	/** ファイルのヘッダ行 */
	private static final String HEAD_ROW = "H";
	/** ファイルのデータ行 */
	private static final String DATA_ROW = "D";
	/** ファイルの終了行 */
	private static final String END_ROW = "E";
	/** 区切り文字 カンマ */
	private static final String SEPARATE = ",";

	/**
	 * ファイル名と列数をセットします
	 * @param	fileName	パスを含めたファイル名
	 * @param	cols	データタイプコード列を含めた列数
	 *
	 * **/
	EventMgFileIO(String fileName, int cols)   {
		this.cols = cols;
		this.fileName = fileName;
	}

	/**
	 * このクラスのメイン処理です
	 * @return String 結果コードを返却します
	 */
	abstract protected String main()throws Exception;

	/**
	 * データ有効性チェック
	 *
	 * @return	処理結果を返却します
	 * @param	columns 検査対象のファイル行です
	 *
	 * **/
	abstract protected boolean enableLine(String[] columns)throws Exception;

	/**
	 * ファイル有効性チェックを行い
	 * 有効性チェックが正常であれば
	 * D行を格納したList<String[]>を返却します
	 *
	 * @return ファイルから読み出したデータ
	 * @param	reader
	 * @throws NoSuchFileException
	 *
	 * **/
	protected List<String[]> enableFile() throws NoSuchFileException {

		int recordCounter = 0;
		int dataCounter = 0;
		boolean endFlg = false; //終端行

		dataList = new ArrayList<>();;
		Path path=Paths.get(this.fileName);

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {

				String[] columns = line.split(SEPARATE); // 区切り文字","で分割する
				int columnCounter = 1;

				//１列目が S H D E以外の行が存在しないか確認
				if (!matchType(columns[0])) {
					setResult("不明行存在");
					reader.close();
					dataList.clear();
					return dataList;

				}

				//列数繰り返し
				for (int j = 0; j < columns.length; j++) {

					//レコードごとのデータの数がフォーマットとあっているか確認
					if (columnCounter > this.cols && !(columns[j].isEmpty())) {
						if (recordCounter == 1) {
							setResult("ヘッダ行異常１");
							reader.close();
							dataList.clear();
							return dataList;

						}
						if (!endFlg) {
							if (2 <= recordCounter) {
								setResult("ヘッダ行異常１");
								reader.close();
								dataList.clear();
								return dataList;

							}
						}
					}

					//ヘッダまたはデータが空じゃないか確認
					if (recordCounter > 0 && !endFlg && columnCounter <= this.cols) {
						if (columns[j].isEmpty() && recordCounter == 1) {
							setResult("ヘッダ行異常2");
							reader.close();
							dataList.clear();
							return dataList;

						}

					}

					//2行目の最初がHかどうか確認
					if (!(HEAD_ROW.equals(columns[j])) && (recordCounter == 1) && (columnCounter == 1)) {
						setResult("ヘッダ行異常2");
						reader.close();
						dataList.clear();
						return dataList;

					}

					//最終行のデータ合計を表す数字と、実際のデータ数があっているか確認
					if (endFlg) {
						try {
							if (!(Integer.parseInt(columns[j]) == dataCounter) && (columnCounter == 2)) {
								setResult("終端行異常１");
								reader.close();
								dataList.clear();
								return dataList;

							}
						} catch (NumberFormatException e) {//終端の値が数値ではない
							setResult("終端行異常１");
							dataList.clear();
							return dataList;

						}
					}

					//D行の数をカウントする
					if (DATA_ROW.equals(columns[j])) {
						dataCounter++;
					}

					//E行に来たら最終行変数を起動
					if (END_ROW.equals(columns[j])) {
						endFlg = true;
					}
					columnCounter++;
				} //forの閉じ--列終了

				//正常なD行ならばコレクションに格納
				if (DATA_ROW.equals(columns[0])) {
					dataList.add(columns);
				}
				recordCounter++;
			} //whileの閉じ --終端

			reader.close();

			//最終行以降にデータがないか確認
			if (recordCounter != dataCounter + 3) {
				setResult("終端行異常２");
				dataList.clear();
				return dataList;
			}
		}catch(MalformedInputException e) {
			e.printStackTrace();
			setResult("ファイルキャラクターセットエラー");
			dataList.clear();
			return dataList;
		} catch (IOException e) {
			e.printStackTrace();
			setResult("ファイル読み込みエラー");
			dataList.clear();
			return dataList;

		}
		setResult(SUCCESS);
		return dataList;
	}

	/**
	 * enableFileメソッドの結果を格納します
	 *
	 * @param s 結果値
	 *
	 */

	private void setResult(String s) {
		this.result = s;
	}

	/**
	 * enableFileメソッドの結果を返却します
	 *
	 *
	 * @return  result 結果値
	 */
	protected String getResult() {
		return this.result;
	}

	/**
	 * データタイプ(S,H,D,E)のみが入っているか検査します
	 * それ以外であればfalseを返却します
	 *
	 * @param d 検査対象
	 * @return  boolean
	 */
	protected boolean matchType(String d) {

		if (START_ROW.equals(d) || HEAD_ROW.equals(d) || DATA_ROW.equals(d) || END_ROW.equals(d)) {
			return true;
		}
		return false;
	}

}
