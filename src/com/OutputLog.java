package com;

import org.apache.log4j.Logger;

import fileio.FileController;

public class OutputLog {

	protected static final Logger logger = Logger.getLogger(FileController.class);
	protected static String code=null;

	public static void  output() {

		if(code.equals("100")) {
		logger.info(code);
		}else {
			logger.error(code);
		}


	}
}
