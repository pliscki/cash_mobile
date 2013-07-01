package projetofinal.ftec.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.app.Application;

public class CustomDecimalUtils {
	public static final String FORMATO_PERCENT = "#,###,##0.##";
	
	public static Double toDouble (int inteiro, int decimal) {
		String sIntPart = Integer.toString(inteiro);
		String sDecPart = Integer.toString(decimal);
		return Double.parseDouble(sIntPart + "." + sDecPart);
	}
	
	public static Double toDouble (String sInt, String sDec) {
		int intPart = 0;
		int decPart = 0;
		if (sInt.compareTo("") != 0) {
			intPart = Integer.parseInt(sInt);
		}
		if (sDec.compareTo("") != 0) {
			decPart = Integer.parseInt(sDec);
		}
		
		return toDouble(intPart, decPart);
	}
	
	public static String format(Double valor) {				
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		return df.format(valor);
	}
	
	public static String format(String format, Double valor) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(valor);
	}
	
	public static String getIntPart(Double valor) {
		String[] result = format("######0.00",valor).split("\\" + getDecimalSeparator());
		return result[0];
	}
	
	public static String getDecPart(Double valor) {
		String[] result = format("######0.00",valor).split("\\" + getDecimalSeparator());
		return result[1];
	}
	
	public static char getDecimalSeparator() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		return dfs.getDecimalSeparator();
	}
	
	public static char getGroupingSeparator() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		return dfs.getGroupingSeparator();
	}
	
		
}

