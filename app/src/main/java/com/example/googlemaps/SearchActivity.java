package com.example.googlemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.googlemaps.model.LatitueLongitude;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitueLongitude> latitueLongitudeList;
    Marker markerName;
    CameraUpdate cemter,zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        etCity = findViewById(R.id.actvCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString()))
                {
                    etCity.setError("Please enter a city name");
                    return;
                }

                int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    LoadMap(position);
                else
                    Toast.makeText(SearchActivity.this, "Location not found by name: " + etCity.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillArrayListAndSetAdapter()
    {
        latitueLongitudeList = new ArrayList<>();
        latitueLongitudeList.add(new LatitueLongitude(27.7134481,85.3241922,"Naagpokhari"));
        latitueLongitudeList.add(new LatitueLongitude(27.7181749,85.3173212,"Narayanhiti Palace Museum"));
        latitueLongitudeList.add(new LatitueLongitude(27.7127827,85.3265391,"Hotel Brihaspati"));

        String[] data = new String[latitueLongitudeList.size()];

        for (int i = 0; i < data.length ; i++) {
            data[i] = latitueLongitudeList.get(i).getMarker();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);
    }

    public int SearchArrayList(String name){
        for (int i = 0; i < latitueLongitudeList.size(); i++) {
            if (latitueLongitudeList.get(i).getMarker().contains(name)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        cemter = CameraUpdateFactory.newLatLng(new LatLng(27.7172453,85.3239605));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(cemter);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void LoadMap(int position){

        if (markerName!=null)
        {
            markerName.remove();
        }
        double latitude = latitueLongitudeList.get(position).getLat();
        double longitude = latitueLongitudeList.get(position).getLon();
        String marker = latitueLongitudeList.get(position).getMarker();
        cemter = CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(marker));
        mMap.moveCamera(cemter);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }
}
