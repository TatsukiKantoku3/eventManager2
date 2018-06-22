package domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.DataValid;

public class DataValidTest {

	static final int INT_NUM = 5;
	static final Integer FALSE_NUM= null;
	static final String STRING_CHAR ="あいうえお";
	static final String FALSE_NULL_CHAR =null;
	static final String FALSE_EMPTY_CHAR ="";
	static final Date DATE = new Date();
	static final  Date FALSE_DATE = null;
	static final String STR ="あいうえお";
	static final String FALSE_STR ="あいうえおかきくけこ";
	static final int LIMIT_NUM = 12345;
	static final int FALSE_LIMIT_NUM = 1234567890;
	static final String HALF="aiueo1234";
	static final String FALSE_HALF="aiueo-1234";
	static final String HAIHUN_HALF="aiueo-1234";
	static final String FALSE_HAIHUN_HALF="aiueo_1234";
	static final String ENG_HALF="aiueo";
	static final String FALSE_ENG_HALF="aiueo1234";
	static final String HALF_NUM="12345678";
	static final String FALSE_HALF_NUM="aiueo1234";
	static final String KANA="アイウエオ";
	static final String FALSE_KANA="ABCDE";
	static final int VALUE=5;
	static final int BEGIN=0;
	static final int END=10;
	static final int FALSE_VALUE_OVER=5;
	static final int FALSE_VALUE_UNDER=1;
	static final int FALSE_BEGIN=2;
	static final int FALSE_END=4;
	static final String STR_DATE="2018/06/12";
	static final String TYPE="yyyy/MM/dd";
	static final String STR_DATE1="2月27日";
	static final String TYPE1="M月d日";
	static final String FALSE_STR_DATE="2018/06/12";
	static final String FALSE_TYPE="M月d日";
	static final String FALSE_STR_DATE2="2016/2/29";
	static final String TIME="23:50";
	static final String FALSE_TIME="12時50分";
	static final String NULL_TIME=null;
	static final String EMPTY_TIME="";
	static final String TEL="00-1234-5678";
	static final String TEL1="(00)1234-5678";
	static final String TEL2="080-1234-5678";
	static final String TEL3="050-1234-5678";
	static final String TEL4="0120-123-456";
	static final String FALSE_TEL="0000-1111-2222";
	static final int LIMITTER =5;

	@Test
	public void testIsNotNullInteger() {

		boolean check = DataValid.isNotNull(INT_NUM);
		assertThat(check, is(true));


		boolean check2 = DataValid.isNotNull(FALSE_NUM);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsNotNullString() {

		boolean check = DataValid.isNotNull(STRING_CHAR);
		assertThat(check, is(true));


		boolean check2 = DataValid.isNotNull(FALSE_NULL_CHAR);
		assertThat(check2, is(false));

		boolean check3 = DataValid.isNotNull(FALSE_EMPTY_CHAR);
		assertThat(check3, is(false));
	}

	@Test
	public void testIsNotNullDate() {

        boolean check = DataValid.isNotNull(DATE);
        assertThat(check, is(true));


        boolean check2 = DataValid.isNotNull(FALSE_DATE);
        assertThat(check2, is(false));
	}

	@Test
	public void testLimitCharStringInt() {

		boolean check =DataValid.limitChar(STR, LIMITTER);
		assertThat(check, is(true));


		boolean check2 =DataValid.limitChar(FALSE_STR, LIMITTER);
		assertThat(check2, is(false));
	}

	@Test
	public void testLimitCharIntInt() {

		boolean check =DataValid.limitChar(LIMIT_NUM, LIMITTER);
		assertThat(check, is(true));


		boolean check2 =DataValid.limitChar(FALSE_LIMIT_NUM, LIMITTER);
		assertThat(check2, is(false));
	}

	@Test
	public void testChkLiteAndNum() {

		boolean check =DataValid.chkLiteAndNum(HALF);
		assertThat(check, is(true));


		boolean check2 =DataValid.chkLiteAndNum(FALSE_HALF);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsAlphanum() {

		boolean check =DataValid.isAlphanum(HAIHUN_HALF);
		assertThat(check, is(true));


		boolean check2 =DataValid.isAlphanum(FALSE_HAIHUN_HALF);
		assertThat(check2, is(false));
	}

	@Test
	public void testInNotNum() {

		boolean check =DataValid.inNotNum(ENG_HALF);
		assertThat(check, is(true));


		boolean check2 =DataValid.inNotNum(FALSE_ENG_HALF);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsNum() {

		boolean check =DataValid.isNum(HALF_NUM);
		assertThat(check, is(true));


		boolean check2 =DataValid.isNum(FALSE_HALF_NUM);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsKana() {

		boolean check =DataValid.isKana(KANA);
		assertThat(check, is(true));


		boolean check2 =DataValid.isKana(FALSE_KANA);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsRange() {



		boolean check =DataValid.isRange(VALUE, BEGIN, END);
		assertThat(check, is(true));

		boolean check2 =DataValid.isRange(FALSE_VALUE_OVER, FALSE_BEGIN, FALSE_END);
		assertThat(check2, is(false));

		boolean check3 =DataValid.isRange(FALSE_VALUE_UNDER, FALSE_BEGIN, FALSE_END);
		assertThat(check3, is(false));
	}

	@Test
	public void testIsDateFormat() {


		boolean check =DataValid.isDateFormat(STR_DATE,TYPE);
		assertThat(check, is(true));


		boolean check1 =DataValid.isDateFormat(STR_DATE1,TYPE1);
		assertThat(check1, is(true));


		boolean check2 =DataValid.isDateFormat(FALSE_STR_DATE,FALSE_TYPE);
		assertThat(check2, is(false));

		boolean check3 = DataValid.isDateFormat(FALSE_STR_DATE2,TYPE);
		assertThat(check3, is(false));
	}

	@Test
	public void testIsTimeFormat() {
		boolean check =DataValid.isTimeFormat(TIME);
		assertThat(check, is(true));


		boolean checkfalse1 =DataValid.isTimeFormat(FALSE_TIME);
		assertThat(checkfalse1, is(false));

		boolean checkfalse2 =DataValid.isTimeFormat(NULL_TIME);
		assertThat(checkfalse2, is(false));

		boolean checkfalse3 =DataValid.isTimeFormat(EMPTY_TIME);
		assertThat(checkfalse3, is(false));
	}

	@Test
	public void testIsTelFormat() {

		boolean check =DataValid.isTelFormat(TEL);
		assertThat(check, is(true));


		boolean check1 =DataValid.isTelFormat(TEL1);
		assertThat(check1, is(true));


		boolean check2 =DataValid.isTelFormat(TEL2);
		assertThat(check2, is(true));


		boolean check3 =DataValid.isTelFormat(TEL3);
		assertThat(check3, is(true));


		boolean check4 =DataValid.isTelFormat(TEL4);
		assertThat(check4, is(true));


		boolean checkfalse =DataValid.isTelFormat(FALSE_TEL);
		assertThat(checkfalse, is(false));
	}

}
