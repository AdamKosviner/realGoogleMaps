package com.example.kosvinera9166.googlemaps;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGpsenabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private Location myLocation;
    private static final int MY_LOC_ZOOM_FACTOR = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng birthplace = new LatLng(40.7831, -73.9712);
        mMap.addMarker(new MarkerOptions().position(birthplace).title("Born Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(birthplace));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyMapsApp", "Failed permission check two");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        mMap.setMyLocationEnabled(true);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //get GPS status
            isGpsenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGpsenabled) Log.d("MyMaps", "getLocation: Gps is enabled");

            //get network enabled
            isNetworkEnabled = locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER));
            if (isNetworkEnabled) Log.d("MyMaps", "getLocation: Network is enabled");

            if (!isNetworkEnabled && !isGpsenabled) {
                Log.d("MyMaps", "getLocation: no provider is is enabled");
            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    Log.d("MyMaps", "getLocation: Network  enabled-requesting location updates");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);


                    Log.d("MyMaps", "getLocation: networkloc update request succesful");
                    Toast.makeText(this, "Using network", Toast.LENGTH_SHORT);
                }
                if (isGpsenabled) {
                    Log.d("MyMaps", "getLocation: Network  enabled-requesting location updates");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("MyMaps", "GpsEnabled: permission check failed");
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerGps);

                    Log.d("MyMaps", "getLocation: networkloc update request succesful");
                    Toast.makeText(this, "Using GPS", Toast.LENGTH_SHORT);
                }
            }


        } catch (Exception e) {
            Log.d("MyMaps", "Caught exception in get location");
            e.printStackTrace();
            ;
        }
    }

    android.location.LocationListener locationListenerGps = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //output in Log.d and toast that gps enabled and working
            Log.d("MyMaps", "GpsEnabled: gps enablednd working");
            Toast howzit = Toast.makeText(getApplicationContext(), "Using GPS", Toast.LENGTH_SHORT);
            howzit.show();
            //output a marker on map create dropmarker method
            //remove the network location updates see LocationManager
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //output in Log.d and toast that gps enabled and working
            //setup switch statement to check the status imput param
            //case LocationProvider.AVAILABLE --> output message t log d
            //case above but out ofservice or unavailable -->request NETWORK_pROVIDER updates
            // request defaults
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    };
    android.location.LocationListener locationListenerNetwork = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //output in Log.d and toast that gps enabled and working
            //output a marker on map call dropmarker method
            //relaunch the network provider request updates see request LocationUpdates (NETWORK_PROVIDER)
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //output in Log.d and toast that network enabled and working
            //setup switch statement to check the status imput param
            //case LocationProvider.AVAILABLE --> output message t log d
            //case above but out ofservice or unavailable -->request NETWORK_pROVIDER updates
            // request defaults
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    };

    public void dropMarker(String provider) {
        LatLng userLocation = null;
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                Log.d("MyMaps", "dropMarker: permission check failed");
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            myLocation = locationManager.getLastKnownLocation(provider);

        }
        if (myLocation==null){
            Log.d("MyMaps", "dropMarker: previous known location is null");
        }
        else{


            userLocation= new LatLng(myLocation.getLatitude(),myLocation.getLongitude());

            //display lat long message
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, MY_LOC_ZOOM_FACTOR);

            //drop the actual marker on map
            //reference android circle class
            Circle circle =mMap.addCircle(new CircleOptions()
                    .center(userLocation)
                    .radius(1)
                    .strokeColor(Color.BLACK)
                    .strokeWidth(2)
                    .fillColor(Color.RED));

            mMap.animateCamera(update);

        }




    }






}
