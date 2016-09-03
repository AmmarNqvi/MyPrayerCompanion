
package com.androidweardocs.wearablemessage;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidweardocs.wearablemessage.logic.QiblaCompassManager;
import com.androidweardocs.wearablemessage.util.ConcurrencyUtil;
import com.androidweardocs.wearablemessage.util.ConstantUtilInterface;
import com.androidweardocs.wearablemessage.util.LocationEnum;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

// This class takes care of the Qibla direction aspect of the app
// Animations with pointers are created as a compass view showing directions and pointer to Qibla


public class Qibla extends Activity implements AnimationListener,
        OnSharedPreferenceChangeListener, ConstantUtilInterface {
   
   
   // Current location 
    public Location crntLoc = null;
    public float lon2=0,lat2=0;
    public double lat1=Math.toRadians(21.4167);
    public double lon1=Math.toRadians(39.8167);
    public float bearing =20;
    private boolean Up = true, gpsFound = true;


    private double PrevQiblaFrmN = 0, PrevQib = 0, PrevNorth = 0;

    // This animation is used to rotate north and qibla images
    private RotateAnimation animation;

    private ImageView compassImg, qiblaImg;
    // This class informs us about changes in qibla and north direction
    private final QiblaCompassManager qiblaManager = new QiblaCompassManager(
            this);

  
    private boolean angleSignaled = false;
    private Timer timer = null;

    private SharedPreferences prefs;

    
    public boolean isRegistered = false;

    // TimerTask notifies about changes in direction of north and Qibla

    private final Handler MyHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {

            if (message.what == ROTATE_IMAGES_MESSAGE) {

                Bundle bundle = message.getData();
                // These are for us to know that if qibla direction is changed
                // or north direction is changed.
                boolean isQiblaChanged = bundle.getBoolean(IS_QIBLA_CHANGED);
                boolean isCompassChanged = bundle
                        .getBoolean(IS_COMPASS_CHANGED);
                // These are the delta angles from north and qibla (first set to
                // zero and if they are changed in this message, we will update
                // them)
                double qiblaNewAngle = 0;
                double compassNewAngle = 0;
                if (isQiblaChanged)
                    qiblaNewAngle = (Double) bundle.get(QIBLA_BUNDLE_DELTA_KEY);
                if (isCompassChanged) {
                    compassNewAngle = (Double) bundle
                            .get(COMPASS_BUNDLE_DELTA_KEY);
                }
                // This
                syncQiblaAndNorthArrow(compassNewAngle, qiblaNewAngle,
                        isCompassChanged, isQiblaChanged);
                angleSignaled = false;
            }
        }

    };

    public void setLocationText(String textToShow) {
    }

    /*
     * This is actually a loop task that check for new angles when no animation
     * is in run and then provide a Message for QiblaActivity. Please note that
     * this class is running in another thread.
     */
    private TimerTask getTimerTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                if (angleSignaled && !ConcurrencyUtil.isAnyAnimationOnRun()) {

                    // numAnimationOnRun += 2;
                    Map<String, Double> newAnglesMap = qiblaManager
                            .fetchDeltaAngles();
                    Double newNorthAngle = newAnglesMap
                            .get(QiblaCompassManager.NORTH_CHANGED_MAP_KEY);
                    Double newQiblaAngle = newAnglesMap
                            .get(QiblaCompassManager.QIBLA_CHANGED_MAP_KEY);

                    Message message = MyHandler.obtainMessage();
                    message.what = ROTATE_IMAGES_MESSAGE;
                    Bundle b = new Bundle();
                    if (newNorthAngle == null) {
                        b.putBoolean(IS_COMPASS_CHANGED, false);
                    } else {
                        ConcurrencyUtil.incrementAnimation();
                        b.putBoolean(IS_COMPASS_CHANGED, true);

                        b.putDouble(COMPASS_BUNDLE_DELTA_KEY, newNorthAngle);
                    }
                    if (newQiblaAngle == null) {
                        b.putBoolean(IS_QIBLA_CHANGED, false);

                    } else {
                        ConcurrencyUtil.incrementAnimation();
                        b.putBoolean(IS_QIBLA_CHANGED, true);
                        b.putDouble(QIBLA_BUNDLE_DELTA_KEY, newQiblaAngle);
                    }

                    message.setData(b);
                    MyHandler.sendMessage(message);
                } else if (ConcurrencyUtil.getNumAimationsOnRun() < 0) {
                    Log.d(NAMAZ_LOG_TAG,
                            " Number of animations are negetive numOfAnimation: "
                                    + ConcurrencyUtil.getNumAimationsOnRun());
                }
            }
        };
        return timerTask;
    }


    private void schedule() {

        if (timer == null) {
            timer = new Timer();
            this.timer.schedule(getTimerTask(), 0, 200);
        } else {
            timer.cancel();
            timer = new Timer();
            timer.schedule(getTimerTask(), 0, 200);
        }
    }

    private void cancelSchedule() {

        if (timer == null)
            return;
        // timer.cancel();
    }

    private void onInvalidateQible(String message) {
    }

    private void requestForValidationOfQibla() {
        ImageView arrow = ((ImageView) findViewById(R.id.arrowImage));
        ImageView compass = ((ImageView) findViewById(R.id.compassImage));
        //ImageView frame = ((ImageView) findViewById(R.id.frameImage));
        FrameLayout qiblaFrame = ((FrameLayout) findViewById(R.id.qiblaLayout));

        if (Up && (gpsFound || crntLoc != null)) {
          } else {
            if (!Up) {
                onScreenDown();
            } else if (!(gpsFound || crntLoc != null)) {
                onGPSOn();
            }
        }
    }

    private void onGPSOn() {
        gpsFound = false;
        onInvalidateQible(getString(R.string.no_location_yet));
    }


    public void onScreenDown() {
        Up = false;
        onInvalidateQible(getString(R.string.screen_down_text));
    }

    public void onScreenUp() {
        Up = true;
        requestForValidationOfQibla();
    }
public void onNewLocationFromGPS(Location location) {
        gpsFound = true;
        crntLoc = location;
    lat2= (float)( Math.toRadians(location.getLatitude()));

    lon2= (float) ( Math.toRadians(location.getLongitude()));
    float lonDelta = (float) (lon2 - lon1);
    float y = (float) (Math.sin(lonDelta) * Math.cos(lat2));
    float x = (float) (Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lonDelta));
    bearing = (float) Math.toDegrees(Math.atan2(y, x));
        this.setLocationText(getLocationForPrint(location.getLatitude(),
                location.getLongitude()));
        requestForValidationOfQibla();
    }

    private void onGPSOff(Location defaultLocation) {
        crntLoc = defaultLocation;
        gpsFound = false;
        requestForValidationOfQibla();
    }
    private String getLocationForPrint(double latitude, double longitude) {
        int latDegree = (new Double(Math.floor(latitude))).intValue();
        int longDegree = (new Double(Math.floor(longitude))).intValue();
        String latEnd = getString(R.string.latitude_south);
        String longEnd = getString(R.string.longitude_west);
        if (latDegree > 0) {
            latEnd = getString(R.string.latitude_north);

        }
        if (longDegree > 0) {
            longEnd = getString(R.string.longitude_east);
        }
        double latSecond = (latitude - latDegree) * 100;
        double latMinDouble = (latSecond * 3d / 5d);
        int latMinute = new Double(Math.floor(latMinDouble)).intValue();

        double longSecond = (longitude - longDegree) * 100;
        double longMinDouble = (longSecond * 3d / 5d);
        int longMinute = new Double(Math.floor(longMinDouble)).intValue();
        return String.format(getString(R.string.geo_location_info), latDegree,
                latMinute, latEnd, longDegree, longMinute, longEnd);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        registerListeners();
        Context context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);
        String gpsPerfKey = getString(R.string.gps_pref_key);



        boolean isGPS = false;
        try {
            isGPS = Boolean.parseBoolean(prefs.getString(gpsPerfKey, "false"));
        } catch (ClassCastException e) {
            isGPS = prefs.getBoolean(gpsPerfKey, false);
        }
        if (!isGPS) {
            unregisterForGPS();
            useDefaultLocation(prefs,
                    getString(R.string.state_location_pref_key));
        } else {
            registerForGPS();
            onGPSOn();
        }
        this.qiblaImg = (ImageView) findViewById(R.id.arrowImage);
        this.compassImg = (ImageView) findViewById(R.id.compassImage);
    }

    private void unregisterListeners() {
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                .removeUpdates(qiblaManager);

        ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                .removeUpdates(qiblaManager);
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gsensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor msensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.unregisterListener(qiblaManager, gsensor);
        mSensorManager.unregisterListener(qiblaManager, msensor);
        cancelSchedule();

    }

    private void registerForGPS() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        String provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
	        locationManager.requestLocationUpdates(provider, MIN_LOCATION_TIME,
	                MIN_LOCATION_DISTANCE, qiblaManager);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_LOCATION_TIME, MIN_LOCATION_DISTANCE, qiblaManager);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, MIN_LOCATION_TIME,
                MIN_LOCATION_DISTANCE, qiblaManager);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            qiblaManager.onLocationChanged(location);
        }

    }

    private void unregisterForGPS() {
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                .removeUpdates(qiblaManager);

    }


    private void registerListeners() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean(getString(R.string.gps_pref_key), false)) {
            registerForGPS();
        } else {
            useDefaultLocation(prefs,
                    getString(R.string.state_location_pref_key));
        }
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gsensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor msensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(qiblaManager, gsensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(qiblaManager, msensor,
                SensorManager.SENSOR_DELAY_GAME);
        schedule();
        isRegistered = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConcurrencyUtil.setToZero();
        ConcurrencyUtil.directionChangedLock.readLock();
        unregisterListeners();
    }



    /*
     * This method synchronizes the Qibla and North arrow rotation.
     */
    public void syncQiblaAndNorthArrow(double northNewAngle,
            double qiblaNewAngle, boolean northChanged, boolean qiblaChanged) {
        if (northChanged) {
            PrevNorth = rotateImageView(northNewAngle, PrevNorth,
                    compassImg);
            // if North is changed and our location are not changed(Though qibla
            // direction is not changed). Still we need to rotated Qibla arrow
            // to have the same difference between north and Qibla.
            if (qiblaChanged == false && qiblaNewAngle != 0) {
                PrevQiblaFrmN = qiblaNewAngle;
                PrevQib = rotateImageView(qiblaNewAngle + northNewAngle,
                        PrevQib, qiblaImg);
            } else if (qiblaChanged == false && qiblaNewAngle == 0)

                PrevQib = rotateImageView(bearing
                        + northNewAngle, PrevQib, qiblaImg);

        }
        if (qiblaChanged) {
            PrevQiblaFrmN = qiblaNewAngle;
            PrevQib = rotateImageView(bearing + PrevNorth,
                    PrevQib, qiblaImg);

        }
    }

    private double rotateImageView(double newAngle, double fromDegree,
            ImageView imageView) {

        newAngle = newAngle % 360;
        double rotationDegree = fromDegree - newAngle;
        rotationDegree = rotationDegree % 360;
        long duration = new Double(Math.abs(rotationDegree) * 2000 / 360)
                .longValue();
        if (rotationDegree > 180)
            rotationDegree -= 360;
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.qiblaLayout);
        float toDegree = new Double(newAngle % 360).floatValue();
        final int width = Math.abs(frameLayout.getRight()
                - frameLayout.getLeft()),  height = Math.abs(frameLayout.getBottom()
                - frameLayout.getTop());

        LinearLayout main = (LinearLayout) findViewById(R.id.mainLayout);
        float pivotX = width / 2f, pivotY = height / 2f;
        animation = new RotateAnimation(new Double(fromDegree).floatValue(),
                toDegree, pivotX, pivotY);
        animation.setRepeatCount(0);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setAnimationListener(this);
        Log.d("", "rotating image from :" + fromDegree
                + " degree to : " + rotationDegree + " ImageView: "
                + imageView.getId());
        imageView.startAnimation(animation);
        return toDegree;

    }

    public void signalForAngleChange() {
        this.angleSignaled = true;
    }

    public void onAnimationEnd(Animation animation) {
        if (ConcurrencyUtil.getNumAimationsOnRun() <= 0) {
            Log.d("",
                    "An animation error occurred!");
        } else {
            ConcurrencyUtil.decrementAnimation();
        }
        schedule();
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
        cancelSchedule();

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        String gpsPerfKey = getString(R.string.gps_pref_key);
        String defaultLocationPerfKey = getString(R.string.state_location_pref_key);
        if (gpsPerfKey.equals(key)) {
            boolean isGPS = false;
            try {
                isGPS = Boolean.parseBoolean(sharedPreferences.getString(key,
                        "false"));
            } catch (ClassCastException e) {
                isGPS = sharedPreferences.getBoolean(key, false);
            }
            if (isGPS) {
                registerForGPS();
                crntLoc = null;
                onGPSOn();
            } else {
                useDefaultLocation(sharedPreferences, defaultLocationPerfKey);
                unregisterForGPS();

            }
        } else if (defaultLocationPerfKey.equals(key)) {
            sharedPreferences.edit().putBoolean(gpsPerfKey, false);
            sharedPreferences.edit().commit();
            unregisterForGPS();
            useDefaultLocation(sharedPreferences, key);
        } else {

            Log.d("", "preference with key:" + key
                    + " is changed");
        }

    }



    private void useDefaultLocation(SharedPreferences prefs, String key) {
        int defLocationID = Integer.parseInt(prefs.getString(key, ""
                + LocationEnum.T.getId()));
        LocationEnum locationEnum = LocationEnum.values()[defLocationID - 1];
        Location location = locationEnum.getLocation();
        qiblaManager.onLocationChanged(location);
        this.setLocationText(String.format(
                getString(R.string.default_location_text),
                locationEnum.getName(this)));
        onGPSOff(location);
    }
}