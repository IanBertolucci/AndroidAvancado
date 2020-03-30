package com.example.treinamentoandroidavancado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    LocationManager locationManager;

    Handler handler;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        handler = new Handler();

        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                atualizarMapa(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                assert status.getStatusMessage() != null;
                Log.e("mapsapp", status.getStatusMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
            handler.removeCallbacksAndMessages(null);
        }
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        verificarStatusGPS();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    private void localizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            final Location location = (Location) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                cont = 0;
                atualizarMapa(new LatLng(location.getLatitude(), location.getLongitude()));
            } else if(cont < 10){
                cont++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        localizacao();
                    }
                }, 2000);
            }
        }
    }

    private void verificarStatusGPS(){
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder locatSettingsRequest = new LocationSettingsRequest.Builder();
        locatSettingsRequest.setAlwaysShow(true);
        locatSettingsRequest.addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locatSettingsRequest.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:
                        localizacao();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapsActivity.this, 2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("MAPS-APP", "Não foi possível obter a localização");
                        break;
                }
            }
        });
    }

    private void atualizarMapa(LatLng local){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 17.0f));
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(local).title("Local atual"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            if (resultCode == RESULT_OK){
                cont = 0;
                handler.removeCallbacksAndMessages(null);
                localizacao();
            }

        }
    }
}
