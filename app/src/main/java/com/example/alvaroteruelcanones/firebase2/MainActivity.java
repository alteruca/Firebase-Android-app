package com.example.alvaroteruelcanones.firebase2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends Activity {


    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    double GlobalLat, GlobalLon;
    private static final String TAG = "Adios!";

    //PERMISSIONS REQUESTS
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;

    //FIREBASE GLOBAL VARIABLES
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private DatabaseReference myRefChild = myRef.child("Geolocalización");
    FirebaseUser currentUser;

    //Minimo tiempo para updates en Milisegundos
    private static final long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 60 * 1; // 1 minuto
    //Minima distancia para updates en metros.
    private static final double MIN_CAMBIO_DISTANCIA_PARA_UPDATES = 1.5; // 1.5 metros

    //LocationListener mylistener = null;
    LocationManager mymanager = null;
    String provider;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        mAuth = FirebaseAuth.getInstance();
        mTextMessage = (TextView) findViewById(R.id.message);
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            return;
        }


        mymanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO locationListenerGPS onStatusChanged

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            public void onLocationChanged(Location location) {
                //if it finds the location
                if (location != null) {
                    GlobalLon =  location.getLongitude();
                    GlobalLat =  location.getLatitude();

/*                  mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();
                    //save text in edittext into the firebase
                    if (!String.valueOf(GlobalLat).equals(""))
                        myRef.child("users").child(currentUser.getUid()).child("latitude").setValue(GlobalLat);
                    if (!String.valueOf(GlobalLon).equals(""))
                        myRef.child("users").child(currentUser.getUid()).child("longitude").setValue(GlobalLon);
*/
                }
                else{

                    GlobalLon = 77;
                    GlobalLat = 88;
                }
            }

        };

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = mymanager.getBestProvider(criteria, false);




        // Get the location from the given provider
        Location location = mymanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        mymanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if (location != null)
            locationListener.onLocationChanged(location);
        else
            location = mymanager.getLastKnownLocation(provider);

        if (location != null)
            locationListener.onLocationChanged(location);
        else

            Toast.makeText(getBaseContext(), "Location can't be retrieved",
                    Toast.LENGTH_SHORT).show();


    }


    public void onStart() {
        super.onStart();


        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         //   requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            return;
        //}
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, (android.location.LocationListener) locationListener);
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,  0, (android.location.LocationListener) locationListener);

    }

    //when the app resumes, turn on the GPS locations
    public void onResume() {
        super.onResume();
    }


    public void StoreLocUser(View view) {
        //getLocation();
        myRef.setValue(currentUser);
        myRefChild.setValue(GlobalLon);

    }

    @Override
    protected void onStop() {
        Log.w(TAG, "App stopped");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "App destroyed");

        super.onDestroy();
    }

}