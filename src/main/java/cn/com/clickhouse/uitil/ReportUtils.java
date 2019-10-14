package cn.com.clickhouse.uitil;

import cn.com.clickhouse.vo.IntervalItme;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ReportUtils.class);
	
	public static List<Object> getArrayListVal(Object obj){
		if(obj != null){
			return JSON.parseObject(obj.toString(), ArrayList.class);
		}
		return null;
	}
	
	public static Map<String,Object> getHashMapVal(Object obj){
		if(obj != null){
			return JSON.parseObject(obj.toString(), HashMap.class);
		}
		return null;
	}
	
	public static Map<String,String> getStringHashMapVal(Object obj){
		if(obj != null){
			return JSON.parseObject(obj.toString(), HashMap.class);
		}
		return null;
	}
	
	public static Boolean getBooleanVal(Object obj){
		return obj == null ?false:Boolean.parseBoolean(obj.toString());
	}
	
	public static String getStringVal(Object obj){
		return obj == null ? null:obj.toString();
	}
	
	public static int getIntVal(Object obj){
		return obj == null ? 0:Integer.parseInt(obj.toString());
	}
	
	public static long getLongVal(Object obj){
		return obj == null ? 0:Long.parseLong(obj.toString());
	}
	
	public static Double getDoubleVal(Object obj){
		return obj == null ? 0d:Double.parseDouble(obj.toString());
	}
	
	public static Date getDateVal(Object obj){
		Date result = null;
		if(obj != null){
			if(obj instanceof Long){
				return new Date((long) obj);
			}
			if(obj instanceof Date){
				return (Date) obj;
			}
		}
		return result;
	}
	
	public static String getUniqueRandColor(Map<String,String> colorMap){
		String color = "#"+getRandColorCode();
		if(colorMap.values().contains(color)){
			getUniqueRandColor(colorMap);
		}else{
			return color;
		}
		return color;
	}
	
	public static String getRandColorCode(){  
		  String r,g,b;  
		  Random random = new Random();  
		  r = Integer.toHexString(random.nextInt(256)).toUpperCase();  
		  g = Integer.toHexString(random.nextInt(256)).toUpperCase();  
		  b = Integer.toHexString(random.nextInt(256)).toUpperCase();  
		    
		  r = r.length()==1 ? "0" + r : r ;  
		  g = g.length()==1 ? "0" + g : g ;  
		  b = b.length()==1 ? "0" + b : b ;  
		    
		  return r+g+b;  
	}
	/**
	 * 获取等分区间
	 * @param list
	 * @param minVal
	 * @param maxVal
	 */
	public static void getIntervalItmes(List<IntervalItme> list, double minVal, double maxVal, int division){
		long max = (long) Math.ceil(maxVal);
	    long min = (long) Math.floor(minVal);
	    long temp = max-min;
	    long startScore = min;
	    if(division==0)//默认为10等分
	    	division=10;
	    if(temp>=division){
	    	long n = (long)(Math.ceil(1.0 * temp/division));
	    	long rows = (long)(Math.ceil(1.0 * temp/n));
	    	for(int i=1;i<=rows;i++){
	    		long endScore = Math.min(min+n*i, max);
	    		if(i==rows){
	    			IntervalItme item = new IntervalItme(startScore,endScore,"[","]",startScore+"-"+endScore);
	    			list.add(item);
	    		}else {
	    			IntervalItme item = new IntervalItme(startScore,endScore,"[",")",startScore+"-"+endScore);
	    			list.add(item);
	    		}
	    		startScore = endScore;
	    	}
	    }else if (temp==0){
	    	IntervalItme item = new IntervalItme(min,min,"[","]",min+"");
			list.add(item);
	    }else{
	    	for(int i=1;i<=temp;i++){
	    		long endScore = Math.min(min+i, max);
	    		if(i==temp){
	    			IntervalItme item = new IntervalItme(startScore,endScore,"[","]",startScore+"-"+endScore);
	    			list.add(item);
	    		}else {
	    			IntervalItme item = new IntervalItme(startScore,endScore,"[",")",startScore+"-"+endScore);
	    			list.add(item);
	    		}
	    		startScore = endScore;
	    	}
	    }
	}
	
	public static String formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(d);
    }
	
	/**
	 * 获取查询时间
	 * @param paramsMap
	 */
	public static void getQueryTime(Map<String, Object> paramsMap){
		SimpleDateFormat fmtYear = new SimpleDateFormat(DateUtil.FORMAT_YEAR);
		SimpleDateFormat fmtMonth = new SimpleDateFormat(DateUtil.FORMAT_MONTH);
		SimpleDateFormat fmtDate = new SimpleDateFormat(DateUtil.FORMAT_DATE);
		SimpleDateFormat fmtDateTime = new SimpleDateFormat(DateUtil.FORMAT_DATETIME);
		//1-非固定频度;2-日报;3-周报;4-月报;5-季报;6-年报
		int queryType = getIntVal(paramsMap.get("queryType"));
		Date startDate = new Date();
		Date endDate = new Date();
		Date tempDate = new Date();
		try{
			switch (queryType) {
			case 1:
//				2019-04-11 00:00:00
				String startTime = getStringVal(paramsMap.get("startTime"));
				String endTime = getStringVal(paramsMap.get("endTime"));
				startDate = fmtDateTime.parse(startTime);
				endDate = fmtDateTime.parse(endTime);
				break;
			case 2:	
//				2019-04-10
				String startDay = getStringVal(paramsMap.get("startDay"));
				tempDate = fmtDate.parse(startDay);
				startDate = DateUtil.getStartTime(tempDate);
				endDate = DateUtil.getEndTime(tempDate);
				break;
			case 3:
//				2019-03-31
				String startWeek = getStringVal(paramsMap.get("startWeek"));
				tempDate = fmtDate.parse(startWeek);
				startDate = DateUtil.getStartTime(tempDate);
				endDate = DateUtil.getEndTime(tempDate);
				endDate = DateUtil.addDate(endDate, 6);
				break;
			case 4:
//				2019-03
				String startMonth = getStringVal(paramsMap.get("startMonth"));
				tempDate = fmtMonth.parse(startMonth);
				startDate = DateUtil.getStartTime(tempDate);
				endDate = DateUtil.getLastDayOfMonth(tempDate);
				endDate = DateUtil.getEndTime(endDate);
				break;
			case 5:
//				2018
				String startQuarterYear = getStringVal(paramsMap.get("startQuarterYear"));
				int startQuarter = getIntVal(paramsMap.get("startQuarter"));
				tempDate = fmtYear.parse(startQuarterYear);
				startDate = DateUtil.getQuarterOfYear(tempDate,startQuarter-1);
				endDate = DateUtil.getQuarterOfYear(tempDate,startQuarter);
				break;
			case 6:
//				2018
				String startYear = getStringVal(paramsMap.get("startYear"));
				startDate = fmtYear.parse(startYear);
				endDate = DateUtil.getLastDayOfYear(startDate);
				endDate = DateUtil.getEndTime(endDate);
				break;
			default:
				break;
			}
			paramsMap.put("startQueryDate", startDate);
			paramsMap.put("endQueryDate", endDate);
		}catch (ParseException e) {
			logger.error("查询日期转换出错：{}",e);
		}
		
	}

}
