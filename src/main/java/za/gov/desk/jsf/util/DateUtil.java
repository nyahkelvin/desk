package za.gov.desk.jsf.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class DateUtil
{
  public static Integer getCurrentYear()
  {
    Calendar now = Calendar.getInstance();
    int year = now.get(1);
    return Integer.valueOf(year);
  }
  
  public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate)
  {
    Calendar startCal = Calendar.getInstance();
    startCal.setTime(startDate);
    
    Calendar endCal = Calendar.getInstance();
    endCal.setTime(endDate);
    
    int workDays = 0;
    if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
      return 0;
    }
    if (startCal.getTimeInMillis() > endCal.getTimeInMillis())
    {
      startCal.setTime(endDate);
      endCal.setTime(startDate);
    }
    do
    {
      startCal.add(5, 1);
      if ((startCal.get(7) != 7) && (startCal.get(7) != 1)) {
        workDays++;
      }
    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
    return workDays;
  }
  
  public static long getDateDiffInDays(Date startDate, Date endDate)
  {
    long diff = startDate.getTime() - endDate.getTime();
    
    long days = diff / 86400000L;
    
    return days;
  }
  
  public static int getDateDiffInDaysInt(Date startDate, Date endDate)
  {
    DateTime start = new DateTime(startDate);
    DateTime end = new DateTime(endDate);
    int days = Days.daysBetween(start, end).getDays();
    return days;
  }
  
  public Date stringToDate(String dateInString)
  {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    Date date = new Timestamp(new Date().getTime());
    try
    {
      date = formatter.parse(dateInString);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return date;
  }
  
  public static Date stringToDateNoTime(String dateInString)
  {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    Date date = new Timestamp(new Date().getTime());
    try
    {
      date = formatter.parse(dateInString);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return date;
  }
  
  public static String getCurrentDateInString()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(new Date());
  }
  
  public static String getCurrentDatePlusPriorityInString(Integer priority)
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(new Date());
  }
  
  public Date addMonthToDate(int month, Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(2, month);
    return cal.getTime();
  }
  
  public static Date addDays(int days, Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(5, days);
    return cal.getTime();
  }
  
  public static Date addHours(int hours, Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(10, hours);
    return cal.getTime();
  }
  
  public int getDateDiffInMonths(Date startDate, Date endDate)
  {
    Calendar startCalendar = new GregorianCalendar();
    startCalendar.setTime(startDate);
    
    Calendar endCalendar = new GregorianCalendar();
    endCalendar.setTime(endDate);
    
    int diffYear = endCalendar.get(1) - startCalendar.get(1);
    int diffMonth = diffYear * 12 + endCalendar.get(2) - startCalendar.get(2);
    return diffMonth;
  }
  
  public int getDateDiffInYears(Date startDate, Date endDate)
  {
    Calendar startCalendar = new GregorianCalendar();
    startCalendar.setTime(startDate);
    
    Calendar endCalendar = new GregorianCalendar();
    endCalendar.setTime(endDate);
    
    int diffYear = endCalendar.get(1) - startCalendar.get(1);
    
    return diffYear;
  }
}
