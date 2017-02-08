package com.jungwonlee.bmn_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

public class Search extends AppCompatActivity implements LocationListener {

    private GoogleApiClient client;
    private GoogleApiClient client2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void ViewMap() {


        FrameLayout frameLayout = new FrameLayout(this);
        TMapView tMapView = new TMapView(this);

        tMapView.setSKPMapApiKey("e17e2369-9a7c-3270-b592-4320bbd3b7e6");
        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        //현재위치로 초기화
       LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double Longitude , Latitude;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(lm.GPS_PROVIDER, 1000, 0, this);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
        }
        else{
            lm.requestLocationUpdates(lm.NETWORK_PROVIDER,1000 ,0 ,this);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
        }
        tMapView.setLocationPoint(Longitude, Latitude);
        frameLayout.addView(tMapView);
        setContentView(frameLayout);


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
       // lm.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    //현재위치로 화면을 옮기는 함수.
    public void CurLoc() {
        FrameLayout frameLayout = new FrameLayout(this);
        TMapView tMapView = new TMapView(this);
        TMapMarkerItem tItem = new TMapMarkerItem();

        TMapPoint tpoint = tMapView.getLocationPoint();
        double Latitude = tpoint.getLatitude();
        double Longitude = tpoint.getLongitude();
        tItem.setTMapPoint(tpoint);


    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Search Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
        client2.disconnect();
    }
}
