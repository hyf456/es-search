package com.hivescm.search.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchLogger {
	public static final Logger LOGGER = LoggerFactory.getLogger(SearchLogger.class);

	public static void error(String msg, Exception ex) {
		LOGGER.error(msg, ex);
	}

	public static void log(Object obj) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(obj.toString().replace("\n", " ").replace("\t", " "));
		}
	}

	public static void log(String msg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(msg.replace("\n", " ").replace("\t", " "));
		}
	}

}
