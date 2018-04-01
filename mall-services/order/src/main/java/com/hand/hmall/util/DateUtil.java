package com.hand.hmall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DateUtil {
	
	/**
	 * 将日期通过某种格式转换为字符串类型
	 * @param date
	 * @param template
	 * @return
	 */
	public static String getdate(Date date,String template){
		SimpleDateFormat sdf = new SimpleDateFormat(template); 
		return sdf.format(date);
	}
	
	/**
	 * 将字符串转换为Date类型
	 * @param date 日期类型的字符串
	 * @param template 要转换的模板
	 * @return
	 */
	public static Date getDate(Object date,String template){
		if(date==null||date.equals("")){
			return null;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(template);
		try {
			return sdf.parse(date.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	/**
	 * 将map中有的Date转换为字符串格式
	 * （只要是字段名中有Time或者有Date的，都会自动转换为时间格式）
	 * @param map 
	 * @return
	 */
	public static Map<String, Object> formDate(Map<String, Object> map){
		for (String key : map.keySet()) {
			if(key.indexOf("Time")!=-1||key.indexOf("Date")!=-1){
				//获取当前key得值
				Object value=map.get(key);
				try{ 
					Date date=new Date(Long.valueOf(value.toString()));
					map.put(key, DateUtil.getdate(date, "yyyy-MM-dd HH:mm:ss"));
				}catch(Exception ex){
					continue;
				}
			}
		}
		return map;
		
	}
	
	
	public static List<Map<String, ?>> formDate(List<Map<String, ?>> list){
		for (int i = 0; i < list.size(); i++) {
			list.set(i, DateUtil.formDate((Map<String, Object>)list.get(i)));
		}
		return list;
	}
	
	
	
	
}
