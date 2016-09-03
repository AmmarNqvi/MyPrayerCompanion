package com.androidweardocs.wearablemessage;

import android.app.Activity;
import android.app.PendingIntent;


public class globvar {

	
public static String token,user_id,pwd,time,device_id,record,locate_address;

public static double lati,longi;
public static boolean ch;

public static Activity ac;

public static  PendingIntent sender_fajr, sender_duhr , sender_asr , sender_magrib , sender_isha;


public static String fajr,duhr,asr,magrib,isha;


public static int fajr_hour,duhr_hour,asr_hour,magrib_hour,isha_hour;
public static int fajr_minute,duhr_minute,asr_minute,magrib_minute,isha_minute;

public static String mosque_addr,mosque_name;
public static double mosque_lati,mosque_longi;
	
	public globvar() {

		// TODO Auto-generated constructor stub
	
	}	


}
