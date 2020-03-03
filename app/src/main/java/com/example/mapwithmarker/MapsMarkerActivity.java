package com.example.mapwithmarker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.maps.CameraUpdateFactory.zoomIn;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {


    GoogleMap googleMap;
    List <MapLocation> cotxeList;
    List <MapLocation> motoList;
    List <MapLocation> biciList;
    List <Marker> cotxeMarkers = new ArrayList<>();
    List <Marker> motoMarkers = new ArrayList<>();
    List <Marker> biciMarkers = new ArrayList<>();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = MapsMarkerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        cotxeList = new ArrayList<>();
        motoList = new ArrayList<>();
        biciList = new ArrayList<>();

        MapLocation c1 = new MapLocation(41.623237, 0.615603, "Ford Fiesta");
        MapLocation c2 = new MapLocation(41.624556, 0.631703, "Ford Mustang");
        MapLocation c3 = new MapLocation(41.629651, 0.626040, "Honda Civic");
        MapLocation c4 = new MapLocation(41.621625, 0.638906, "Citroen Saxo");
        MapLocation c5 = new MapLocation(41.643537, 0.618739, "Audi A7");
        MapLocation c6 = new MapLocation(41.606811, 0.640319, "Mercedes Benz");

        cotxeList.add(c1);
        cotxeList.add(c2);
        cotxeList.add(c3);
        cotxeList.add(c4);
        cotxeList.add(c5);
        cotxeList.add(c6);

        MapLocation m1 = new MapLocation(41.627237, 0.615803, "Kawasaki Z800");
        MapLocation m2 = new MapLocation(41.624237, 0.616603, "Honda CBR");
        MapLocation m3 = new MapLocation(41.622237, 0.615603, "KTM Duke ");
        MapLocation m4 = new MapLocation(41.623237, 0.617603, "Yamaha R1");

        motoList.add(m1);
        motoList.add(m2);
        motoList.add(m3);
        motoList.add(m4);

        CheckBox checkCotxe = findViewById(R.id.cotxes);
        checkCotxe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bol) {
                if (bol) {
                    showCotxe();
                } else {
                    hideCotxes();
                }
            }
        });

        CheckBox checkMoto = findViewById(R.id.motos);
        checkMoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bol) {
                if (bol) {
                    showMotos();
                } else {
                    hideMotos();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    public class MapLocation {
        MapLocation(double lt, double ln, String t) {
            lat = lt;
            lon = ln;
            title = t;
        }

        double lat;
        double lon;
        String title;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(MapsMarkerActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            new AlertDialog.Builder(MapsMarkerActivity.this)
                    .setTitle(R.string.needed)
                    .setMessage(R.string.prop)
                    .setPositiveButton(R.string.su, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocationActivity();
                        }
                    })
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showSnackbar(R.string.perm,
                                    android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            requestLocationActivity();
                                        }
                                    });
                        }
                    }).create().show();
        } else {
            Log.i(TAG, "Requesting permission");
            requestLocationActivity();
        }
    }

    private void requestLocationActivity(){
        ActivityCompat.requestPermissions(MapsMarkerActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    public void showCotxe() {
        cotxeMarkers.clear();
        for (MapLocation loc : cotxeList) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.lat, loc.lon))
                    .title(loc.title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.lat, loc.lon)).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            cotxeMarkers.add(marker);
        }
    }

    public void showMotos() {
        motoMarkers.clear();
        for (MapLocation loc : motoList) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(loc.lat, loc.lon))
                    .title(loc.title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.lat, loc.lon)).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            motoMarkers.add(marker);
        }
    }

    public void hideCotxes() {
        for (Marker marker : cotxeMarkers ) {
            marker.remove();
        }
    }

    public void hideMotos() {
        for (Marker marker : motoMarkers) {
            marker.remove();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            zoomIn();

        }
    }

    public void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions();
        }else{
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            zoomIn();

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }
}
