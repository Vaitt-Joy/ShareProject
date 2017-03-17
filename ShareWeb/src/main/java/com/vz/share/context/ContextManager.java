package com.vz.share.context;

import com.vz.share.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * context manager
 * @author huang.jb
 *
 */
public class ContextManager {
	
	private static ThreadLocal<Context> contextLocal = new ThreadLocal<Context>();
	private static ThreadLocal<Map<String, Object>> valueLocal = new ThreadLocal<Map<String, Object>>();
	
	private static final String DATA_ACTION_TYPE_KEY = "dataActionType";
	
	/**
	 * get context
	 * @return
	 */
	public static Context getContext() {
		Context c = contextLocal.get();
		if (c == null) {
			contextLocal.set(new Context());
		}
		return contextLocal.get();
	}
	/**
	 * set context
	 * @param context
	 */
	public static void setContext(Context context) {
		/*if (context == null) {
			throw new IllegalArgumentException("设置上下文失败，值不允许为空。");
		}
		*/
		contextLocal.set(context);
	}
	/**
	 * get value
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(String key) {
		if (StringUtil.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("获取上下文中的值失败，KEY不允许为空。");
		}
		
		Map<String, Object> values = valueLocal.get();
		if (values != null && values.size() > 0 && values.containsKey(key)) {
			return (T) values.get(key);
		} else {
			return null;
		}
	}
	/**
	 * set value
	 * @param key
	 * @param value
	 */
	public static void setValue(String key, Object value) {
		if (StringUtil.isNullOrEmpty(key) || value == null) {
			throw new IllegalArgumentException("设置上下文中的值失败，KEY和VALUE都不允许为空。");
		}
		
		Map<String, Object> values = valueLocal.get();
		if (values == null) {
			values = new HashMap<String, Object>();
			valueLocal.set(values);
		}
		values.put(key, value);
	}
	/**
	 * 获取线程中数据操作类型
	 * @return
	 */
	public static String getDataActionType() {
		return getValue(DATA_ACTION_TYPE_KEY);
	}
	/**
	 * 设置线程中数据操作类型
	 * @param dataActionType
	 */
	public static void setDataActionType(String dataActionType) {
		setValue(DATA_ACTION_TYPE_KEY, dataActionType);
	}
	
}
