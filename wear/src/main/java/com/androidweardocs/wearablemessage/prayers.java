package com.androidweardocs.wearablemessage;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//This class implements the view for prayer timings
// calculates current prayer timings
// sets alarm and notification for each prayer through TimerService



public class prayers extends Activity
        implements LocationListener {
    private static final String TAG = "MainActivity";
globvar g1=new globvar();
    double lon1,lat1;
    protected LocationManager locationManager;
    int hfajr,mfajr,hzur,mzur,hasr,masr,hmag,mmag,hish,mish;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.rpa);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);



        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        // TODO Auto-generated method stub
        double timezone = (Calendar.getInstance().getTimeZone()
                .getOffset(Calendar.getInstance().getTimeInMillis()))
                / (1000 * 60 * 60);

        PrayerTime prayers = new PrayerTime();

        prayers.setTimeFormat(prayers.Time12);
        prayers.setCalcMethod(prayers.ISNA);
        prayers.setAsrJuristic(prayers.Hanafi);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = { 0, 0, 0, 0, 0, 0, 0 }; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        g1.ac=this;
        ArrayList prayerTimes = prayers.getPrayerTimes(cal, 37.4051150,
                -122.1145240, timezone);
        ArrayList prayerNames = prayers.getTimeNames();


        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());


        for(int i = 0; i < 7; i++ ) {
            if (i== 1 || i == 4) { continue;}
            if (i==0){
                String[] splitTime = String.format("%s", prayerTimes.get(i)).replace(" ", "").split(":");

                hfajr=Integer.parseInt(splitTime[0]);
                mfajr=Integer.parseInt(splitTime[1]);
                registerAlarmManager( "Fajr",hfajr, mfajr,1 );
                TextView newTextView = new TextView(prayers.this);
                newTextView.setText("Fajr");
                newTextView.setTextSize(20);
                newTextView.setTextColor(Color.parseColor("#636363"));

                linearLayout.addView(newTextView);

                TextView newTextView2 = new TextView(prayers.this);
                newTextView2.setText("" + prayerTimes.get(i));
                newTextView2.setTextSize(15);
                newTextView2.setTextColor(Color.parseColor("#636363"));
                newTextView2.setPadding(0, 0, 0 , 20);

                newTextView2.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, R.drawable.line);

                linearLayout.addView(newTextView2);




                }

            if (i==2){
                String[] splitTime = String.format("%s", prayerTimes.get(i)).replace(" ", "").split(":");

                hzur=Integer.parseInt(splitTime[0]);
                //Check if hour is greater than 12 since clock is using 12-hour format
                if(hzur>12)
                {hzur=hzur+12;}
                mzur=Integer.parseInt(splitTime[1]);
                registerAlarmManager("Dhuhr", hzur, mzur, 1);
                TextView newTextView = new TextView(prayers.this);
                newTextView.setText("Dhuhr");
                newTextView.setTextSize(20);
                newTextView.setTextColor(Color.parseColor("#636363"));

                linearLayout.addView(newTextView);

                // LinearLayout.addView(img);
                TextView newTextView2 = new TextView(prayers.this);
                newTextView2.setText("" + prayerTimes.get(i));
                newTextView2.setTextSize(15);
                newTextView2.setTextColor(Color.parseColor("#636363"));
                newTextView2.setPadding(0, 0, 0 , 20);
                newTextView2.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, R.drawable.line);
                linearLayout.addView(newTextView2);



            }
            if (i==3){
                String[] splitTime = String.format("%s", prayerTimes.get(i)).replace(" ", "").split(":");
                //Check if hour is greater than 12 since clock is using 12-hour format
                hasr=(Integer.parseInt(splitTime[0]))+12;
                masr=Integer.parseInt(splitTime[1]);
                registerAlarmManager( "Asr",hasr, masr,2 );

                TextView newTextView = new TextView(prayers.this);
                newTextView.setText("Asr");
                newTextView.setTextSize(20);
                newTextView.setTextColor(Color.parseColor("#636363"));

                linearLayout.addView(newTextView);

                TextView newTextView2 = new TextView(prayers.this);
                newTextView2.setText("" + prayerTimes.get(i));
                newTextView2.setTextSize(15);
                newTextView2.setTextColor(Color.parseColor("#636363"));
                newTextView2.setPadding(0, 0, 0 , 20);

                newTextView2.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, R.drawable.line);

                linearLayout.addView(newTextView2);

            }
            if (i==5){
                String[] splitTime = String.format("%s", prayerTimes.get(i)).replace(" ", "").split(":");
                //Check if hour is greater than 12 since clock is using 12-hour format
                hmag=(Integer.parseInt(splitTime[0]))+12;
                mmag=Integer.parseInt(splitTime[1]);
                registerAlarmManager( "Maghrib",12,20,3 );
                TextView newTextView = new TextView(prayers.this);
                newTextView.setText("Maghrib");
                newTextView.setTextSize(20);
                newTextView.setTextColor(Color.parseColor("#636363"));

                linearLayout.addView(newTextView);

                TextView newTextView2 = new TextView(prayers.this);
                newTextView2.setText("" + prayerTimes.get(i));
                newTextView2.setTextSize(15);
                newTextView2.setTextColor(Color.parseColor("#636363"));
                newTextView2.setPadding(0, 0, 0 , 20);

                newTextView2.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, R.drawable.line);

                linearLayout.addView(newTextView2);

            }
            if (i==6){
                String[] splitTime = String.format("%s", prayerTimes.get(i)).replace(" ", "").split(":");
                //Check if hour is greater than 12 since clock is using 12-hour format
                hish=(Integer.parseInt(splitTime[0]))+12;
                mish=Integer.parseInt(splitTime[1]);
                registerAlarmManager( "Isha",hish, mish,4 );
                TextView newTextView = new TextView(prayers.this);
                newTextView.setText("Isha");
                newTextView.setTextSize(20);
                newTextView.setTextColor(Color.parseColor("#636363"));

                linearLayout.addView(newTextView);


                TextView newTextView2 = new TextView(prayers.this);
                newTextView2.setText("" + prayerTimes.get(i));
                newTextView2.setTextSize(15);
                newTextView2.setTextColor(Color.parseColor("#636363"));
                newTextView2.setPadding(0, 0, 0 , 20);

                newTextView2.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, R.drawable.line);

                linearLayout.addView(newTextView2);

            }
        }
    }

    private void registerAlarmManager( String prayer,int hour,int min,int id ) {
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Intent intent = new Intent( prayer,null,this, TimerService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );
    }

    public void onLocationChanged(Location location) {
        lon1 = location.getLongitude();
        lat1 = location.getLatitude();
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


    public void current_azan_time(double latitude, double longitude) {
        // TODO Auto-generated method stub


        // TODO Auto-generated method stub
        double timezone = (Calendar.getInstance().getTimeZone()
                .getOffset(Calendar.getInstance().getTimeInMillis()))
                / (1000 * 60 * 60);

        PrayerTime prayers = new PrayerTime();

        prayers.setTimeFormat(prayers.Time12);
        prayers.setCalcMethod(prayers.Karachi);
        prayers.setAsrJuristic(prayers.Shafii);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = { 0, 0, 0, 0, 0, 0, 0 }; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        g1.ac=this;
        ArrayList prayerTimes = prayers.getPrayerTimes(cal, g1.lati,
                g1.longi, timezone);
        ArrayList prayerNames = prayers.getTimeNames();}





    private void scroll(final int scrollDirection) {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.card_scroll_view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(scrollDirection);
            }
        });
    }
}