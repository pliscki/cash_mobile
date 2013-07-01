package projetofinal.ftec.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.text.format.DateFormat;

public class CustomDateUtils {
	
	public static final String DATE_SQLITE = "yyyy-MM-dd";
	
	public static int compararDataDMY(long msData1, long msData2) {
		Calendar data1 = Calendar.getInstance();
		Calendar data2 = Calendar.getInstance();
		data1.setTimeInMillis(msData1);
		data2.setTimeInMillis(msData2);
		
		return toSQLDate(data1).compareTo(toSQLDate(data2));
	}
	
	public static int compararDataDMY(Calendar data1, Calendar data2) {
		return toSQLDate(data1).compareTo(toSQLDate(data2));
	}
	
	public static Calendar toCalendar(String dataSQL) throws ParseException {
		Calendar dataCalendario = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_SQLITE);
		dataCalendario.setTime(sdf.parse(dataSQL));
		return dataCalendario; 
	}
	
	public static String toSQLDate(Calendar dataCal) {
		return DateFormat.format(CustomDateUtils.DATE_SQLITE, dataCal).toString();
	}
	
	

}
