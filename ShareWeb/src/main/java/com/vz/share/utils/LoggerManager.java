package com.vz.share.utils;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工具类
 */
public class LoggerManager {

    public static final String LEVEL_DEBUG = "DEBUG";
    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";

    public static final String APPENDER_CONSOLE = "CONSOLE";
    public static final String APPENDER_FILE = "ROLLING_FILE";

    private static Logger logger = Logger.getLogger("default");

    private static Map<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();
    private static Object lockObj = new Object();

    /**
     * 获取日志输出接口
     *
     * @return
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * 设置日志输出接口
     *
     * @param log
     */
    public static void setLogger(Logger log) {
        logger = log;
    }

    /**
     * 输出信息
     *
     * @param message
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * 输出调试信息
     *
     * @param message
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    /**
     * 输出警告信息
     *
     * @param message
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * 输出错误信息
     *
     * @param message
     */
    public static void error(String message) {
        logger.error(message);
    }

    /**
     * 输出错误信息
     *
     * @param e
     */
    public static void error(Exception e) {
        logger.error(e.getMessage(), e);
    }

    /**
     * 输出错误信息
     *
     * @param message
     * @param e
     */
    public static void error(String message, Exception e) {
        logger.error(message, e);
    }

    /**
     * 输出致命错误信息
     *
     * @param message
     */
    public static void fatal(String message) {
        logger.fatal(message);
    }

    /**
     * 输出致命错误信息
     *
     * @param message
     * @param e
     */
    public static void fatal(String message, Exception e) {
        logger.fatal(message, e);
    }
}
