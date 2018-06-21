package com;

import java.io.IOException;

import javax.naming.NamingException;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;


public class TestDBAccess {

	/**
	 *JNDI準備.DBアクセス処理を行います.staticイニシャライザでクラス読み込み時に1度だけ呼び出します.
	 **/
	static {

     // JNDI準備
	    try {
			JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
			//System.out.println("staticイニシャライザが呼ばれました。");
		} catch (NamingException | IOException e) {
			e.printStackTrace();
		}
    }


}
