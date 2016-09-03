
package com.androidweardocs.wearablemessage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.DismissOverlayView;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Sample that shows how to set up a basic Google Map on Android Wear.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener {

    public static LatLng LOC = null ;
    public static double Lat=0,Lon=0,Lat1=0,Lon1=0,Lat2=0,Lon2=0,Lat3=0,Lon3=0;
    public static LatLng LOC1=new LatLng(Lat1,Lon1);
    public static LatLng LOC2=new LatLng(Lat2,Lon2);
    public static LatLng LOC3=new LatLng(Lat3,Lon3);
    public static String Snippet=null;
    public static String snippet1=null;
    public static String snippet2=null;
    public static String snippet3=null;
    public static int Marker=0;


    private DismissOverlayView mDismissOverlay;


    private GoogleMap mMap;
    boolean markerClicked;
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Set the layout. It only contains a SupportMapFragment and a DismissOverlay.
        setContentView(R.layout.activity_map);
        Toast.makeText(this, "Long Click on Map to exit", Toast.LENGTH_LONG);
        final FrameLayout topFrameLayout = (FrameLayout) findViewById(R.id.root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);

        // Set the system view insets on the containers when they become available.
        topFrameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Call through to super implementation and apply insets
                insets = topFrameLayout.onApplyWindowInsets(insets);

                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                // Add Wearable insets to FrameLayout container holding map as margins
                params.setMargins(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                mapFrameLayout.setLayoutParams(params);

                return insets;
            }
        });

        // Obtain the DismissOverlayView and display the intro help text.
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.intro_text);
        mDismissOverlay.showIntroIfNecessary();

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        ///Toast.makeText(this, "Long Click on Map to exit", Toast.LENGTH_LONG);
        mMap = googleMap;
        // Set the long click listener as a way to exit the map.
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);


        // Add a marker with a title that is shown in its info window.
        if(Marker==0){
        mMap.addMarker(new MarkerOptions().position(LOC).snippet(Snippet).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.msqq));
            mMap.addMarker(new MarkerOptions().position(LOC1).snippet(snippet1).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC2).snippet(snippet2).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC3).snippet(snippet3).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
        }
         else if(Marker==1){
        mMap.addMarker(new MarkerOptions().position(LOC1).snippet(snippet1).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.msqq));
            mMap.addMarker(new MarkerOptions().position(LOC).snippet(Snippet).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC2).snippet(snippet2).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC3).snippet(snippet3).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));

        }
        else if(Marker==2) {
            mMap.addMarker(new MarkerOptions().position(LOC2).snippet(snippet2).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.msqq));
            mMap.addMarker(new MarkerOptions().position(LOC).snippet(Snippet).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC1).snippet(snippet1).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC3).snippet(snippet3).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));

        }
        else if(Marker==3){
            mMap.addMarker(new MarkerOptions().position(LOC3).snippet(snippet3).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.msqq));
            mMap.addMarker(new MarkerOptions().position(LOC).snippet(Snippet).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC1).snippet(snippet1).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));
            mMap.addMarker(new MarkerOptions().position(LOC2).snippet(snippet2).title("Get Directions!")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mosq));

        }


        // Move the camera to show the marker.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOC, 10));

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Display the dismiss overlay with a button to exit this activity.
        mDismissOverlay.show();
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
//onInfoWindowClick(marker);
        marker.showInfoWindow();
      /*  Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + LOC);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
*/

        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getSnippet().equalsIgnoreCase(Snippet)) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Lat + "," + Lon + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        else if(marker.getSnippet().equalsIgnoreCase(snippet1)) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Lat1 + "," + Lon1 + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
       else if(marker.getSnippet().equalsIgnoreCase(snippet2)) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Lat2 + "," + Lon2 + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        else if(marker.getSnippet().equalsIgnoreCase(snippet3)) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Lat3 + "," + Lon3 + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }
}
