package robert.sysc3010;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GPS extends AppCompatActivity{
    //updates the Database with the lat,long for the user
    //also updates the GPS to the screen(not required just for testing)

    TextView gpsLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        gpsLocation = (TextView) findViewById(R.id.gpsLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lastKnown = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gpsLocation.setText(location.getLatitude() +  " "+location.getLongitude() );
                update();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
		checkLocationPermission();
        Log.d("GPS",""+checkLocationPermission());
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            if(Build.VERSION.SDK_INT>=23) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},15);
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 0, locationListener);//wait every 2 minutes for a change
            update();//force an initial update
        }
        else{
             Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
             startActivity(intent);
        }
    }

    //Method for adding the GPS location of the user into our database
    public void update(){
        Log.d("GPS","Updating");
        String method = "GPS";
        String longitude = ""+lastKnown.getLongitude();
        String latitude = "" +lastKnown.getLatitude();
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method,latitude,longitude);
    }

	public boolean checkLocationPermission()//This method is required when using recquestLocationUpdates
    {
        String permission = "android.permission.ACCESS_COARSE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

   public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}
