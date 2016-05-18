package com.mirrorchannelth.internship.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.config.Constant;

/**
 * Created by rooney on 4/12/2016.
 */
public class StudentMapFragment extends Fragment implements OnMapReadyCallback {
    private View rootView = null;
    private GoogleMap map = null;
    private final int kMapZoomLevel = Constant.MAP_ZOOM;

    private SeekBar seekBar = null;
    private TextView progressLabel = null;
    private int progress = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView != null) {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if(viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_student_map, container, false);
        } catch (InflateException ex) {
            //ex.printStackTrace();
        } finally {
            initToolbar();
            seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
            seekBar.setProgress(progress - 1);
            seekBar.setMax(Constant.MAP_DISTANCE - 1);
            // /setSeekBarListeners();
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        return rootView;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView textTitle = (TextView) rootView.findViewById(R.id.toolbar_title);
        textTitle.setText(getString(R.string.student_map_title));
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setIcon(R.mipmap.ic_toolbar_logo);
    }

    /*private void setSeekBarListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue + 1;
                progressLabel.setText(getString(R.string.student_map_distance_km, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressLabel.setText(getString(R.string.student_map_distance_km, progress));

                //  Save distance to search data
                //SearchPreference searchPreference = SearchPreference.getInstance(getActivity());
                //searchPreference.setValueInt(SearchPreference.DISTANCE, progress);

                //  Refresh map & list
                //onFragmentActionListener.onRefresh();
            }
        });
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        //map.setOnMarkerDragListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //  Get base location from search preference
        //SearchPreference searchPreference = SearchPreference.getInstance(getActivity());
        //lat = searchPreference.getValueDouble(SearchPreference.LAT);
        //lng = searchPreference.getValueDouble(SearchPreference.LNG);

        //LatLng point = new LatLng(lat, lng);
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, kMapZoomLevel));

        //  Set up cluster
        //setUpClustering();

        //  Create user's marker
        //createUserMarkerWithRadius();

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        //if (!isFragmentCreated) {
        //    isFragmentCreated = true;
        //    requestMap();
        //}
    }
}
