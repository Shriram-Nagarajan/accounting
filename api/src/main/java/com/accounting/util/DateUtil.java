package com.accounting.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	public static LocalDate getDate(String dateStr, String dateFormat) throws ParseException {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
		return LocalDate.parse(dateStr, dateTimeFormatter);
	}
	
	public static boolean isBefore(String dateString1, String dateString2, String dateFormat) throws ParseException {
		
		if(dateString1 != null && dateString2 != null) {
	        // ParseException to be thrown if given dates are not valid.
			LocalDate date1 = getDate(dateString1, dateFormat);
			LocalDate date2 = getDate(dateString2, dateFormat);
					
			return date1.isBefore(date2);
		}
		return false;
		
	}

}
