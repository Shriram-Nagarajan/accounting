package com.accounting.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static Date getDate(String dateStr, String dateFormat) throws ParseException {
		// Create a SimpleDateFormat object with the expected format
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        
        // ParseException to be thrown if given dates are not valid.
		return sdf.parse(dateStr);
	}
	
	public static boolean isBefore(String dateString1, String dateString2, String dateFormat) throws ParseException {
		
		if(dateString1 != null && dateString2 != null) {
	        // ParseException to be thrown if given dates are not valid.
			Date date1 = getDate(dateString1, dateFormat);
			Date date2 = getDate(dateString2, dateFormat);
					
			return date1.before(date2);
		}
		return false;
		
	}

}
