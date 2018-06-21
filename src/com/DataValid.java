/**
 *
 */
package com;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author 002
 *
 * データ有効性チェック
 *
 */
public class DataValid {

	//※intだと実際にnullだった場合にfalseを返す前にnull point exceptionで止まってしまう
	//	ため、引数の型をIntegerに変更しました
	/**
	 * int型の引数がnullならfalseを返却する
	 * @param int value
	 * @return boolean result
	 */
	public static boolean isNotNull(Integer value) {
		boolean result = true;
		if (Objects.isNull(value))
			result = false;
		return result;
	}

	/**
	 * String型の引数がnullならfalseを返却する
	 * @param String str
	 * @return boolean result
	 */
	public static boolean isNotNull(String str) {
		boolean result = true;
		if (Objects.isNull(str) || "".equals(str))
			result = false;
		return result;
	}

	//修正しました
	///--Dataではなくて、Dateでは？
	/**
	 * Data型の引数がnullならfalseを返却する
	 * @param Data data
	 * @return boolean result
	 */
	public static boolean isNotNull(Date date) {
		boolean result = true;
		if (Objects.isNull(date))
			result = false;
		return result;
	}
	///--必要なら変更

	/**
	 * 文字数制限
	 * String型の引数の文字数がint型の引数limitNum以上ならfalseを返却する
	 * @param String str, int limitNum
	 * @return boolean result
	 */
	public static boolean limitChar(String str, int limitNum) {
		boolean result = true;
		if (str.length() > limitNum)
			result = false;
		return result;
	}

	/**
	 * 桁数制限
	 * int型の引数の数字数がint型の引数limitNum以上ならfalseを返却する
	 * @param int value, int limitNum
	 * @return boolean result
	 */
	public static boolean limitChar(int value, int limitNum) {
		boolean result = true;
		if (String.valueOf(value).length() > limitNum)
			result = false;
		return result;
	}

	//ハイフンまで含めるか否かの違いです。説明文を修正しました
	//--これの違いは？
	/**
	 * String型の引数が半角英数字以外ならfalseを返却する
	 * @param String str
	 * @return boolean result
	 */
	public static boolean chkLiteAndNum(String str) {
		boolean result = true;
		if (!str.matches("[0-9a-zA-Z]+"))
			result = false;
		return result;
	}

	/**
	 * String型の引数が半角英数字またはハイフン以外ならfalseを返却する
	 * @param String str
	 * @return boolean result
	 */
	public static boolean isAlphanum(String str) {
		boolean result = true;
		if (!str.matches("[0-9a-zA-Z\\-]+"))
			result = false;
		return result;
	}
	//--これの違いは？

	/**
	 * String型の引数が半角英字以外ならfalseを返却する
	 * 数字は含まない
	 * @param String str
	 * @return boolean result
	 */
	public static boolean inNotNum(String str) {
		boolean result = true;
		if (!str.matches("[a-zA-Z]+"))
			result = false;
		return result;
	}

	/**
	 * String型の引数が半角数字以外ならfalseを返却する
	 * @param int value
	 * @return boolean result
	 */
	public static boolean isNum(String value) {
		boolean result = true;
		if (!value.matches("[0-9]+"))
			result = false;
		return result;
	}

	/**
	 * String型の引数がカタカナ以外ならfalseを返却する
	 * @param String str
	 * @return boolean result
	 */
	public static boolean isKana(String str) {
		boolean result = true;
		if (!str.matches("^[\\u30A0-\\u30FF]+$"))
			result = false;
		return result;
	}

	//不等号の向きと論理演算子を下記説明通りになるように変更しました
	/**
	 * int型の第一引数Valueが第二引数begin以上、第三引数end以下ならtrueを返す
	 * @param int value, int begin, int true
	 * @return boolean result
	 */
	public static boolean isRange(int value, int begin, int end) {
		boolean result = true;
		if (!(begin <= value) || !(value <= end))
			result = false;
		return result;
	}

	/**
	 * String型の第一引数dataが、String型の第二引数typeで指定した
	 * " yyyy/MM/dd","M月d日",その他Date型形式 以外ならfalseを返却する
	 *
	 * @param String data, String type
	 * @return boolean result
	 */
	public static boolean isDateFormat(String date, String type) {
		boolean result = true;
		try {
			DateFormat df = new SimpleDateFormat(type);
			df.setLenient(false);
			String stdate = df.format(df.parse(date));
			if (!date.equals(stdate))
				result = false;
		} catch (ParseException p) {
			result = false;
		}
		return result;
	}

	/**
	 * String型の引数が hh:mm 以外ならfalseを返却する
	 * @param String time
	 * @return boolean result
	 */
	public static boolean isTimeFormat(String time) {
		if (time == null || "".equals(time))
			return false;
		Pattern patt = Pattern.compile("^([0-1][0-9]|[2][0-3]):[0-5][0-9]$");
		Matcher matc = patt.matcher(time);
		if (!matc.find())
			return false;
		return true;
	}

	//trueになる例を右側に書きくわえました
	/**
	 * String型の引数が電話番号フォーマット以外ならfalseを返却する
	 * @param String number
	 * @return boolean result
	 */
	public static boolean isTelFormat(String number) {
		if (number.matches("^0\\d-\\d{4}-\\d{4}$")) //固定電話(例 03-2222-2222)
			return true;
		if (number.matches("^\\(0\\d\\)\\d{4}-\\d{4}$")) //固定電話(例 (03)2222-2222)
			return true;
		if (number.matches("^(070|080|090)-\\d{4}-\\d{4}$")) //携帯電話(例 090-1234-5678)
			return true;
		if (number.matches("^050-\\d{4}-\\d{4}$")) //IP電話(例 050-0000-0000)
			return true;
		if (number.matches("^0120-\\d{3}-\\d{3}$")) //フリーダイヤル(例 0120-123-789)
			return true;
		return false;
	}

}
