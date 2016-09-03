package com.androidweardocs.wearablemessage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends Activity  {
    private List<ListViewItem> viewItemList = new ArrayList<>();
    private TextView mTextView;
    WatchViewStub stub = null;
    public static enum ScreenShape {ROUND, MOTO_ROUND, RECTANGLE, UNDETECTED}
    private static int screenWidthPX = 0;
    private static int screenHeightPX = 0;
    private static ScreenShape shape = ScreenShape.UNDETECTED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        initShapeDetection(this.getWindow().getDecorView().findViewById(android.R.id.content));
        if( shape == ScreenShape.RECTANGLE){setContentView(R.layout.rect_activity_message);}
        else {setContentView(R.layout.round_activity_message);}


               mTextView = (TextView)findViewById(R.id.text);

        //wearableListView.setAdapter(new ListViewAdapter(this, viewItemList));

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }




    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.v("myTag", "Main activity received message: " + message);


                ParserTask parserTask = new ParserTask(message);
                parserTask.execute(message);
        }
    }

    private static void initShapeDetection(View view){
        view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                if (insets.isRound()) {
                    shape = ScreenShape.ROUND;
                    if(screenWidthPX == 320 && screenHeightPX == 290) {
                        shape = ScreenShape.MOTO_ROUND;
                    }
                } else {
                    shape = ScreenShape.RECTANGLE;
                }

                return insets;
            }
        });
    }
    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;
        String message = "";

        public ParserTask(String message) {
            this.message = message;
        }

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(message);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lmain);
            RelativeLayout rel = (RelativeLayout) findViewById(R.id.main);
            for (int i = 0; i < 4; i++) {
                final Button nb = new Button(MessageActivity.this);
                // Creating a marker
                // MarkerOptions markerOptions = new MarkerOptions();

                final int j = i;
				if(list!=null){
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);

                    // Getting latitude of the place
                    final double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    final double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    String name = hmPlace.get("place_name");


                    // Getting vicinity

                    final String vicinity = hmPlace.get("vicinity");
                    // Getting rating
                    final String rating = hmPlace.get("rating");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(

                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(20, 10, 0, 10);
//if(i!=0)
                    //  mTextView.setText(mTextView.getText() + "\n" + name  );
                    //            else
                    //mTextView.setText( "\n" + name );
                    if (i == 0) {
                        MapActivity.LOC = new LatLng(lat, lng);
                        MapActivity.Snippet = vicinity;
                        MapActivity.Lat = lat;
                        MapActivity.Lon = lng;
                    } else if (i == 1) {
                        MapActivity.LOC1 = new LatLng(lat, lng);
                        MapActivity.snippet1 = vicinity;
                        MapActivity.Lat1 = lat;
                        MapActivity.Lon1 = lng;
                    } else if (i == 2) {
                        MapActivity.LOC2 = new LatLng(lat, lng);
                        MapActivity.snippet2 = vicinity;
                        MapActivity.Lat2 = lat;
                        MapActivity.Lon2 = lng;
                    } else if (i == 3) {
                        MapActivity.LOC3 = new LatLng(lat, lng);
                        MapActivity.snippet3 = vicinity;
                        MapActivity.Lat3 = lat;
                        MapActivity.Lon3 = lng;
                    }
                    if (name.length() > 20) {
                        name = name.substring(0, Math.min(name.length(), 20));
                    }
                    nb.setText("" + name);
                    nb.setTextColor(Color.parseColor("Black"));
                    nb.setWidth(270);
                    nb.setBackgroundResource(R.drawable.buttonshape);
                    nb.setLayoutParams(params);
                    //  stub.removeAllViews();
                    // rel.onApplyWindowInsets();
                    //viewItemList.add(new ListViewItem(R.drawable.mosq, name));


                    // Context c = getApplicationContext();
                    //wearableListView.setAdapter(new ListViewAdapter(c, viewItemList));
                    mTextView.setVisibility(View.INVISIBLE);
                    nb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MapActivity.c = j;
                            // MapActivity.r=rating;
                            Context c = getApplicationContext();
                            Intent myIntent = new Intent();
                            myIntent.setClass(c, MapActivity.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            c.startActivity(myIntent);
                        }
                    });
                    linearLayout.addView(nb);
                    // else

                    //   linearLayout.addView(nb);

                    // wearableListView.setClickListener(MessageActivity.this);}

                }
                else
    mTextView.setText("Please Ensure you're connected to \n the internet and GPS then \n launch/relaunch app");
            }


            }
    }
}
