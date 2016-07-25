package com.yingks.infra.utils;

import org.apache.log4j.Logger;

public final class LogHelper 
{
    private static Logger logger = null;

    private static void init() 
    {
        //BasicConfigurator.configure();
        logger = Logger.getLogger(LogHelper.class);
    }
    
    private LogHelper()
    {
    	
    }
    
    private static Logger getLogger() 
    {
        if (null == logger) 
        {
            init();
        }
        return logger;
    }

	public static void debug(Object message, Throwable t) {
		
		getLogger();
		
		logger.debug(message, t);
	}

	public static void debug(Object message) {
		
		getLogger();
		
		logger.debug(message);
	}

	public static void error(Object message, Throwable t) {
		
		getLogger();
		
		logger.error(message, t);
	}

	public static void error(Object message) {
		
		getLogger();
		
		logger.error(message);
	}

	public static void info(Object message, Throwable t) {
		
		getLogger();
		
		logger.info(message, t);
	}

	public static void info(Object message) {
		getLogger();
		logger.info(message);
	}

	public static void trace(Object message, Throwable t) {
		getLogger();
		logger.trace(message, t);
	}

	public static void trace(Object message) {
		getLogger();
		logger.trace(message);
	}

	public static void warn(Object message, Throwable t) {
		getLogger();
		logger.warn(message, t);
	}

	public static void warn(Object message) {
		getLogger();
		logger.warn(message);
	}
}
