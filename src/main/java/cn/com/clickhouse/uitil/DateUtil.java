package cn.com.clickhouse.uitil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 日期处理
 */
public class DateUtil {
	public final static String FORMAT_YEAR = "yyyy";
	public final static String FORMAT_MONTH = "yyyy-MM";
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public final static String TYPE_DATE = "date";
    public final static String TYPE_DATETIME = "datetime";
    
    /*public static void main(String[] args) {
    	Date date = new Date();
		System.out.println(date.compareTo(addMinutes(date, -30)));
	}*/
    
    /**
     * 用字符串获得日期
     * 
     * @throws ParseException
     * @dateValue 日期字符串
     * @dateType 格式化的类型,date和datetime
     */
    public static Date getDate(String dateValue, String dateType)
            throws ParseException {
        if (dateValue == null)
            return null;
        if (dateType.equals(TYPE_DATE)) {
            SimpleDateFormat sfdate = new SimpleDateFormat(FORMAT_DATE);
            return sfdate.parse(dateValue);
        } else if (dateType.equals(TYPE_DATETIME)) {
            SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATETIME);
            return sftime.parse(dateValue);
        }
        return null;
    }
    
    /**
     * 
     * 用日期获得x年前或者x年后的日期，返回格式化日期
     *
     * @param date 日期
     * @param year 加或者减 年
     * @param dateType 格式化的类型,date和datetime
     * @return 字符串类型日期
     */
    public static String addDateByYears(Date date, final int year, final String dateType) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        Date targetDate = calendar.getTime();
        if (dateType.equals(TYPE_DATE)) {
            SimpleDateFormat sfdate = new SimpleDateFormat(FORMAT_DATE);
            return sfdate.format(targetDate);
        } else if (dateType.equals(TYPE_DATETIME)) {
            SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATETIME);
            return sftime.format(targetDate);
        }
        return null;
    }
    
    public static Date addMinutes(Date date, int minutes) {
    	if(date==null){
    		date = new Date(); 
    	}
    	
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        Date targetDate = calendar.getTime();
    	
    	return targetDate;
	}
    
    /**
     * 日期相加
     * @param date 初始日期
     * @param days 相加天数
     * @return date
     */
    public static Date addDate(Date date,int days){
    	if(date==null){
    		date = new Date(); 
    	}
    	
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date targetDate = calendar.getTime();
    	
    	return targetDate;
    }
    
    /**
     * 
     * 格式化日期
     *
     * @author cxw
     * @param date 日期
     * @param dateType dateType 格式化的类型,date和datetime
     * @return
     */
    public static String formateTime(Date date, String dateType) {
        if(date == null) {
            return null;
        }
        if (dateType.equals(TYPE_DATE)) {
            SimpleDateFormat sfdate = new SimpleDateFormat(FORMAT_DATE);
            return sfdate.format(date);
        } else if (dateType.equals(TYPE_DATETIME)) {
            SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATETIME);
            return sftime.format(date);
        }
        return null;
    }
    /**
     * 获取开始时间
     * @param date 日期
     * @return  该天的开始时间
     */
    public static Date getStartTime(Date date){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取结束时间
     * @param date 日期
     * @return  该天的结束时间
     */
    public static Date getEndTime(Date date){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }
    /**
     * 获取当月第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
    /**
     * 获取当月最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }
    /**
     * 获取当年最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }
    
    public static Date getQuarterOfYear(Date date,int quarter){
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (quarter) {
		case 1:
			calendar.add(Calendar.MONTH, 3);
			break;
		case 2:
			calendar.add(Calendar.MONTH, 6);
			break;
		case 3:
			calendar.add(Calendar.MONTH, 9);
			break;
		case 4:
			calendar.add(Calendar.MONTH, 12);
			break;
		default:
			break;
		}
        return calendar.getTime();
    }
    /*public static void main(String[] args) {
		Date now = new Date();
		Date d1 = DateUtil.addMinutes(now, -90);
		System.out.println(DateUtil.formateTime(d1, DateUtil.TYPE_DATETIME));
		System.out.println(now.compareTo(d1));
	}*/

}
