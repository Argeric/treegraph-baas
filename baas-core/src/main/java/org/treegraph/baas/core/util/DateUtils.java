package org.treegraph.baas.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public final class DateUtils {

	public static final String IDCARD_END = "3000.01.01";// 身份证有效期(长期)

	public static final String SYSTEM_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SYSTEM_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SYSTEM_DATE_NO_SEPA_FORMAT = "yyyyMMdd";
	public static final String SYSTEM_DATE_FORMAT_ = "yyyy/MM/dd";
	public static final String YYYY_FORMAT = "yyyy";
	public static final String DATE_FORMAT_ = "yyyy年MM月dd日";
	public static final DateFormat DOT_YYYY_MM_DD = new SimpleDateFormat("yyyy.MM.dd");

	public static final DateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static final DateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat HH_mm_ss = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat HH_mm_ss_SSS = new SimpleDateFormat("HH:mm:ss.SSS");
	public static final DateFormat MM_dd_HH_mm_ss = new SimpleDateFormat("MM-dd HH:mm:ss");
	public static final DateFormat MM_dd_HH_mm = new SimpleDateFormat("MM-dd HH:mm");
	public static final String DATE_TO_LONG_FORMATTER = "yyyyMMddHHmmss";

	private DateUtils() {
	}

	/**
	 * 获取当前时间.
	 *
	 * @return Date - 当前时间
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * get date of source by pattern.
	 *
	 * @param source  - 时间格式字符串
	 * @param pattern - 格式
	 * @return Date - 返回时间
	 * @throws ParseException - exception when parse source by pattern
	 */
	public static final Date getDate(String source, String pattern) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(pattern);
		Date date = formatter.parse(source);
		return date;
	}

	/**
	 * 从字符串解析时间.
	 *
	 * @param source - 时间格式字符串
	 * @return Date - 返回时间
	 * @throws ParseException - exception when parse source
	 */
	public static final Date getDate(String source) throws ParseException {
		return getDate(source, SYSTEM_DATE_FORMAT);
	}

	public static final Date getDateTime(String source) throws ParseException {
		return getDate(source, SYSTEM_TIMESTAMP_FORMAT);
	}

	/**
	 * format date by pattern.
	 *
	 * @param date    - 时间
	 * @param pattern - 格式
	 * @return String - 根据指定格式格式化后的字符串
	 */
	public static String format(Date date, String pattern) {
		Calendar calendar = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat(pattern);
		formatter.setCalendar(calendar);
		return formatter.format(date);
	}

	/**
	 * format date by <code>SYSTEM_TIMESTAMP_FORMAT</code>.
	 *
	 * @param date - 时间.
	 * @return String - 根据日期时间格式格式化后的字符串
	 */
	public static String formatDateTime(Date date) {
		DateFormat formatter = new SimpleDateFormat(SYSTEM_TIMESTAMP_FORMAT);
		return formatter.format(date);
	}

	/**
	 * format date by <code>SYSTEM_DATE_FORMAT</code>.
	 *
	 * @param date - 时间.
	 * @return String - 根据日期格式格式化后的字符串
	 */
	public static String formatDate(Date date) {
		DateFormat formatter = new SimpleDateFormat(SYSTEM_DATE_FORMAT);
		return formatter.format(date);
	}

	/**
	 * 格式化当前时间.
	 *
	 * @param pattern - 格式
	 * @return String - 根据指定格式格式化后的字符串
	 */
	public static String formatNow(String pattern) {
		return format(getCurrentDate(), pattern);
	}

	/**
	 * 格式化当前时间.
	 *
	 * param pattern - 格式
	 * 
	 * @return String - 根据指定格式格式化后的字符串
	 */
	public static String formatNow() {
		return format(getCurrentDate(), SYSTEM_TIMESTAMP_FORMAT);
	}

	/**
	 * strToDate:将字符串转化成日期格式为(yyyy-MM-dd HH:mm:ss). <br/>
	 * 
	 * @param dateStr 字符串
	 * @return yyyy-MM-dd HH:mm:ss格式的日期
	 */
	public static Date strToDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * strToDateyyyyMM:将字符串转化成日期格式为(yyyy-MM). <br/>
	 * 
	 * @param dateStr 字符串
	 * @return yyyy-MM格式的日期
	 * @throws ParseException
	 */
	public static Date strToDateyyyyMM(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = sdf.parse(dateStr);

		return date;
	}

	/**
	 * strToDateyyyyMMdd:将字符串转化成日期格式为(yyyy-MM-dd). <br/>
	 * 
	 * @param dateStr 字符串
	 * @return yyyy-MM-dd格式的日期
	 * @throws ParseException
	 */
	public static Date strToDateyyyyMMdd(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateStr);

		return date;
	}

	/**
	 * strYMDToDate:将字符串转化成日期格式为(yyyyMMdd). <br/>
	 * 
	 * @param dateStr 字符串
	 * @return yyyyMMdd格式的日期
	 * @throws ParseException
	 */
	public static Date strYMDToDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse(dateStr);

		return date;
	}

	/**
	 * dateToStrYMD:将日期转出字符串格式为(yyyyMMdd). <br/>
	 * 
	 * @param date 日期
	 * @return yyyyMMdd格式的字符串
	 */
	public static String dateToStrYMD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * dateToStrYMLine:将日期转出字符串格式为(yyyy-MM). <br/>
	 * 
	 * @param date 日期
	 * @return yyyy-MM格式的字符串
	 */
	public static String dateToStrYMLine(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * getTimeYMDHMSSStr:获取当前系统时间的字符串格式为(yyyyMMddHHmmssSSS). <br/>
	 * 
	 * @return
	 */
	public static String getTimeYMDHMSSStr() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return df.format(date);
	}

	/**
	 * getDateYMDStr:获取系统当前时间字符串格式为(yyyyMMdd). <br/>
	 * 
	 * @return
	 */
	public static String getDateYMDStr() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	/**
	 * getYesterday:获取昨天的时间. <br/>
	 * 
	 * @return
	 */
	public static Date getYesterday() {
		Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24L);
		return date;

	}

	/**
	 * getDateTimeN:获取N天前的时间. <br/>
	 * 
	 * @param n
	 * @return
	 */
	public static Date getDateTimeN(long n) {
		Date date = new Date(System.currentTimeMillis() - n * 1000 * 60 * 60 * 24L);
		return date;
	}

	/**
	 * getDateStart:获取某一天的开始时间. <br/>
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateStart(Date date) {
		return strToDate(format(date, SYSTEM_DATE_FORMAT) + " 00:00:00");
	}

	/**
	 * getDateEnd:获取某一天的结束时间. <br/>
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateEnd(Date date) {
		return strToDate(format(date, SYSTEM_DATE_FORMAT) + " 23:59:59");
	}

	public static Date getTomorrowEnd() {
		return strToDate(formatDate(getDateTimeAfterN(getCurrentDate(), 1)) + " 23:59:59");
	}

	private static Date todayEndTime = null;// 今天终结时间

	public static Date getTodayEndDate() {
		if (todayEndTime == null || todayEndTime.getTime() < System.currentTimeMillis()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			todayEndTime = cal.getTime();
		}
		return todayEndTime;
	}

	public static Date getTodayStart() {
		return getDateStart(getCurrentDate());
	}

	/**
	 * getCurrentMonthStartTime:获取本月的开始时间. <br/>
	 * 
	 * @param dateParam
	 * @return
	 * @throws ParseException
	 */
	public static Date getCurrentMonthStartTime(Date dateParam) throws ParseException {

		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(dateParam);
		c.set(Calendar.DATE, 1);
		Date now = shortSdf.parse(shortSdf.format(c.getTime()));

		return now;
	}

	/**
	 * getCurrentMonthEndTime:获取本月的结束时间. <br/>
	 * 
	 * @param dateParam
	 * @return
	 * @throws ParseException
	 */
	public static Date getCurrentMonthEndTime(Date dateParam) throws ParseException {

		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(dateParam);
		c.set(Calendar.DATE, 2);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		Date now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");

		return now;
	}

	/**
	 * getDateTimeAfterN:获取指定时间N天后的时间. <br/>
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getDateTimeAfterN(Date date, long n) {
		Date resultDate = new Date(date.getTime() + n * 1000 * 60 * 60 * 24L);
		return resultDate;
	}

	/**
	 * getDateStrListBetweenStartAndEndDate:获取两个时间点之间的所有日期的集合. <br/>
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateStrListBetweenStartAndEndDate(Date startTime, Date endTime) {
		if (startTime.after(endTime)) {
			return null;
		}
		List<String> dateStrList = new LinkedList<String>();
		do {
			String startTimeStr = formatDate(startTime);
			dateStrList.add(startTimeStr);
			startTime = getDateTimeAfterN(startTime, 1);
		} while (startTime.compareTo(endTime) <= 0);
		return dateStrList;
	}

	/**
	 * getSystemStartTime:获取系统乔治·格林的开始时间. <br/>
	 * 
	 * @return 系统乔治·格林的开始时间
	 */
	public static Date getSystemStartTime() {
		return strToDate("1970-01-01 00:00:00");
	}

	/**
	 * dateToStrYMD:将日期转出字符串格式为(HHmmss). <br/>
	 * 
	 * @param date 日期
	 * @return HHmmss格式的字符串
	 */
	public static String dateToStrHMS(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static String getDateStr(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	/**
	 * 返回短日期，yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static final Date getShortDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获得两个日期之间相差的天数;先获取参数的短日期值,在进行计算。（date1 - date2）
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int intervalDays(Date date1, Date date2) {
		long intervalMillSecond = getShortDate(date1).getTime() - getShortDate(date2).getTime();
		return (int) (intervalMillSecond / (24 * 3600 * 1000));
	}

	// 计算2个日期的相差天数，如果隔天，即使不到24小时也是1天
	public static int diffDays(Date date1, Date data2) {
		return Math.abs(diffDays1(date1, data2));
	}

	// 计算2个日期的相差天数，如果隔天，即使不到24小时也是1天
	public static int diffDaysAbs(Date date1, Date data2) {
		return Math.abs(diffDays1(date1, data2));
	}

	// 计算2个日期的相差天数，如果隔天，即使不到24小时也是1天
	public static int diffDays1(Date fromDate, Date toDate) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(fromDate);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(toDate);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);

		long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 3600 * 1000L);
		return (int) days;
	}

	public static int diffMonths(Date date1, Date data2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(data2);
		long days = 12 * (c2.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) + c2.get(Calendar.MONTH)
				- c1.get(Calendar.MONTH);
		return (int) (Math.abs(days));
	}

	/**
	 * 获取某周的第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getWeekFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 一周第一天是否为星期天
		boolean isFirstSunday = (cal.getFirstDayOfWeek() == Calendar.SUNDAY);
		// 获取周几
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		// 若一周第一天为星期天，则-1
		if (isFirstSunday) {
			weekDay = weekDay - 1;
			if (weekDay == 0) {
				weekDay = 7;
			}
		}
		// 获取本周的第一天
		Date firstDay = getDateTimeAfterN(getDateStart(date), -(weekDay - 1));
		return firstDay;
	}

	static final DateFormat IDCardExpiryDate_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

	// 转换身份证有效期(yyyy.MM.dd)
	public static Date parseIDCardExpiryDate(String str, boolean igoreException) throws ParseException {
		try {
			return IDCardExpiryDate_FORMAT.parse(str);
		} catch (ParseException e) {
			if (!igoreException) {
				throw e;
			}
		}
		return null;
	}

	// 转换身份证有效期(yyyy.MM.dd)
	public static Date parseIDCardExpiryDateOrNull(String str) {
		try {
			return IDCardExpiryDate_FORMAT.parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 
	 * @Description (指定日期添加天数)
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

	public static String dateToStr(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * @param sta
	 * @param end
	 * @return sta > end
	 * @description 比较日期大小
	 */
	public static boolean compareTo(Date sta, Date end) {
		if (sta == null || end == null)
			return false;
		long d1 = sta.getTime();
		long d2 = end.getTime();
		return d1 - d2 > 0;
	}

	// 获取前几天时间
	public static Date getAgoDay(int i) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -i);
		date = calendar.getTime();
		return date;
	}

	// 获取时间的零点
	public static Date getDayZero(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date start = calendar.getTime();
		return start;
	}

	// 计算两个时间差值（毫秒数）
	public static long differFrom(Date date1, Date date2) {
		long minutes = (date2.getTime() - date1.getTime());
		return minutes;
	}

	// 获取当前时间 距离0点相差的秒数
	public static long getDifferSecond() {
		return differFromSecond(new Date(), getDayZero(getAgoDay(-1)));
	}

	// 计算两个时间差值（秒数）
	public static long differFromSecond(Date date1, Date date2) {
		long minutes = (date2.getTime() - date1.getTime()) / 1000;
		return minutes;
	}

	/**
	 * @Title: equalYearAndMonth @Description: (比较两个时间 年份跟月份 是否相等) @param @param
	 * firstDate @param @param secondDate @param @return 设定文件 @return boolean
	 * 返回类型 @author ma wei long @date 2018年2月7日 下午4:58:17 @throws
	 */
	public static boolean equalYearAndMonth(Date firstDate, Date secondDate) {
		// 判断当期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDate);
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		Date frontDate = calendar.getTime();

		Long front = Long.parseLong(format(frontDate, SYSTEM_DATE_NO_SEPA_FORMAT));
		Long first = Long.parseLong(format(firstDate, SYSTEM_DATE_NO_SEPA_FORMAT));
		Long second = Long.parseLong(format(secondDate, SYSTEM_DATE_NO_SEPA_FORMAT));
		return second >= front && second <= first ? true : false;
	}

	/**
	 * @Title: compareYearAndMonthAndDay @Description: (比较两个时间 年份跟月份跟天
	 * 大小) @param @param firstDate @param @param secondDate @param @return
	 * 设定文件 @return boolean 返回类型 @author ma wei long @date 2018年2月7日
	 * 下午4:58:17 @throws
	 */
	public static boolean compareYearAndMonthAndDay(Date startDate, Date endDate) {
		return format(startDate, SYSTEM_DATE_NO_SEPA_FORMAT)
				.compareTo(format(endDate, SYSTEM_DATE_NO_SEPA_FORMAT)) <= 0;
	}

	public static LocalDate dateToLocaleDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}

	public static Date localDateToDate(LocalDate localDate) {
		ZoneId zoneId = ZoneId.systemDefault();
		ChronoZonedDateTime<LocalDate> zonedDateTime = localDate.atStartOfDay(zoneId);
		return Date.from(zonedDateTime.toInstant());
	}

	public static int betweenDays(Date startDate, Date endDate) {
		final LocalDate start = dateToLocaleDate(startDate);
		final LocalDate end = dateToLocaleDate(endDate);
		return (int) (end.toEpochDay() - start.toEpochDay());
	}
}
