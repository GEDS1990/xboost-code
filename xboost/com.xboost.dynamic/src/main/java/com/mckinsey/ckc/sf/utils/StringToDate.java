package com.mckinsey.ckc.sf.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDate {

	public static Date stringToDate(String dateStr) {
		String startDateString = "06/27/2007";
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);
			String newDateString = df.format(startDate);
			System.out.println(newDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDate;
	}

}
