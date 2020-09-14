package com.TUBEDELIVERIES.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.TUBEDELIVERIES.R;
import com.TUBEDELIVERIES.Utility.CommonUtil;
import com.TUBEDELIVERIES.Utility.CommonUtilities;
import com.TUBEDELIVERIES.Utility.DirectionsJSONParser;
import com.TUBEDELIVERIES.Utility.GPSTracker;
import com.TUBEDELIVERIES.Utility.ParamEnum;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddressPicker extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener
{

    private GoogleMap mMap;
    private TextView detectLocationTextView;
    private ImageView backImageView;
    private Button submitButton;
    private GPSTracker locationClass;
    private TextView searchTextView;

    @BindView(R.id.menuIv)
    ImageView menuIv;

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    DownloadTask downloadTask=null;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    List<Polyline> polylines=new ArrayList<>();



    private ArrayList markerPoints;
    private final int PLACE_REQ_CODE=12,GETTING_ADDRESS=1,NOT_SERVE_THIS_AREA=2,HIDE_INFO_WINDOW=3,GETTING_SELECTED_CITY=4,CHECKING_CITY=5;
    Marker marker=null ;
    Geocoder geocoder;
//    String selectedCity="";
    private Handler handler;
    private boolean isInsideCity=false;
    private GetAddress getAddress;
    private LatLng homeLatLong;
    String km;

    @SuppressLint("HandlerLeak")
    private void init()
    {
        submitButton=findViewById(R.id.submitButton);
        searchTextView=findViewById(R.id.searchTextView);
        detectLocationTextView=findViewById(R.id.detectLocationTextView);
        locationClass=new GPSTracker(AddressPicker.this);
        markerPoints= new ArrayList();
        submitButton.setOnClickListener(this);
        searchTextView.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);
        new CommonUtil().setStatusBarGradiant(this,AddressPicker.class.getSimpleName());
        ButterKnife.bind(this);
        menuIv.setVisibility(View.GONE);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,"Select Location");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();





    }

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object=new JSONObject(result);
                JSONArray routesArray=object.getJSONArray("routes");
                if(routesArray.length()>0) {
                    JSONArray legsArray = ((JSONObject) routesArray.get(0)).getJSONArray("legs");
                    if(legsArray.length()>0)
                    {
                       JSONObject distanceObj= ((JSONObject) legsArray.get(0)).getJSONObject("distance");
                       km=distanceObj.getString("text");

                    }

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                ParserTask parserTask = new ParserTask();
                parserTask.execute(result);

            }





        }
    }




    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;



            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            for (Polyline line : polylines) {
                if (polylines.get(polylines.size() - 1).equals(line)) {
                    line.remove();
                    polylines.remove(line);
                }
            }
            progress_bar.setVisibility(View.GONE);
            polylines.add(mMap.addPolyline(lineOptions));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(getIntent().getStringExtra(ParamEnum.LATITUDE.theValue())), Double.parseDouble(getIntent().getStringExtra(ParamEnum.LONGITUDE.theValue())))).title("Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_ic)));
            detectLocationTextView.setText("Estimated: "+km);

        }
    }

        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getAddress=new GetAddress();
                homeLatLong = new LatLng(locationClass.getLatitude(), locationClass.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLatLong, 12.0f));
                marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.location_c)));

                handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        if(msg.what==GETTING_ADDRESS)
                        {
                            marker.setTitle("Getting address…");
                            marker.showInfoWindow();
                        }else if(msg.what==NOT_SERVE_THIS_AREA)
                        {
                            marker.setTitle("Sorry we do not serve here yet…");
                            marker.showInfoWindow();
                        }else if(msg.what==HIDE_INFO_WINDOW)
                        {
                            marker.hideInfoWindow();
                        }

                    }
                };

                geocoder = new Geocoder(AddressPicker.this, Locale.getDefault());
                getAddress.execute(GETTING_SELECTED_CITY);

                GetAddress getSelectedCityAddress=new GetAddress();
                getSelectedCityAddress.execute();

                GetSelectedAddress getSelectedAddress=new GetSelectedAddress();
                getSelectedAddress.execute();

                markerPoints.add(homeLatLong);



            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_ic)));

                } else {
                    marker.setPosition(mMap.getCameraPosition().target);
                    handler.sendEmptyMessage(HIDE_INFO_WINDOW);

//                    if (getIntent().getStringExtra("Direction").equalsIgnoreCase("Yes")) {
//
//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr="+marker.getPosition().latitude+","+marker.getPosition().longitude+"&daddr="+getIntent().getStringExtra(ParamEnum.LATITUDE.theValue())+","+getIntent().getStringExtra(ParamEnum.LONGITUDE.theValue())));
//                        startActivity(intent);
//
//                    }

                }
            }


        });



        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


                if(getAddress.getStatus()!= AsyncTask.Status.RUNNING)
                {
                    getAddress=new GetAddress();
                    getAddress.execute(CHECKING_CITY);
                }else
                {
                    getAddress.cancel(true);
                    getAddress=new GetAddress();
                    getAddress.execute(CHECKING_CITY);
                }

            }
        });

            }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            Log.e("url",strUrl);
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    @Override
    public void onClick(View v)
    { switch (v.getId())
        {
            case R.id.submitButton:
                onSubmit();

                break;
            case R.id.detectLocationTextView:
                LatLng latLng = new LatLng(locationClass.getLatitude(), locationClass.getLongitude());
                marker.setPosition(latLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                markerPoints.add(latLng);
                break;

            case R.id.searchTextView:
                Places.initialize(AddressPicker.this,getResources().getString(R.string.google_map_api_key));
                List<Place.Field> fields1 = Arrays.asList(Place.Field.LAT_LNG);
                Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields1).build(AddressPicker.this);
                startActivityForResult(intent1, PLACE_REQ_CODE);
                break;


            case R.id.menuIv:
            {
                finish();
            }
            break;

        }

    }


    private void onSubmit()
    {
        LatLng selectedLocation =  marker.getPosition();
        Intent intent=new Intent();
        intent.putExtra("ADDRESS",detectLocationTextView.getText().toString());
        intent.putExtra("LAT",selectedLocation.latitude+"");
        intent.putExtra("LONG",selectedLocation.longitude+"");
        this.setResult(RESULT_OK,intent);
        finish();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                markerPoints.clear();
                LatLng selectedPos = place.getLatLng();
                markerPoints.add(selectedPos);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPos, 12.0f));
            }
        }
    }

    private class GetAddress extends AsyncTask<Integer, Void, String>
    {

        LatLng latLng;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            isInsideCity=false;
            latLng=marker.getPosition();
            handler.sendEmptyMessage(GETTING_ADDRESS);
        }

        @Override
        protected String doInBackground(Integer... AddressType) {
            String address="";

            try
            {

                List<Address> addressesList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

                if(addressesList!=null&&!addressesList.isEmpty())
                {
                    Address addresses = addressesList.get(0);
                       address=addresses.getAddressLine(0);
                        isInsideCity=true;
                        handler.sendEmptyMessage(HIDE_INFO_WINDOW);
                }else
                {
                    handler.sendEmptyMessage(NOT_SERVE_THIS_AREA);
                    address="";
                }











            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if(e.getMessage().equalsIgnoreCase("grpc failed"))
                {
                CommonUtilities.snackBar(AddressPicker.this,"Please enable GPS");
                }


            }

           /* if(AddressType[0]==GETTING_SELECTED_CITY)
            {
                selectedCity=address;
            }*/

            return address;
        }



        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.e("updated address",s);
            detectLocationTextView.setText(s);
        }
    }

    private class GetSelectedAddress extends AsyncTask<Integer, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //isInsideCity=false;
           // latLng=marker.getPosition();
/*
            marker.setTitle(HomeMapsActivity.this.getString(R.string.getting_address));
            marker.showInfoWindow();*/
         //   handler.sendEmptyMessage(GETTING_ADDRESS);
        }

        @Override
        protected String doInBackground(Integer... AddressType) {
            String address="";

            try
            {

                List<Address> addressesList=geocoder.getFromLocation(homeLatLong.latitude,homeLatLong.longitude,1);
                if(addressesList.size()>0) {
                    address = addressesList.get(0).getAddressLine(0);
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

           /* if(AddressType[0]==GETTING_SELECTED_CITY)
            {
                selectedCity=address;
            }*/

            return address;
        }



        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.e("addresss",s);




//            searchTextView.setText("Sel");
            detectLocationTextView.setText(s);
        }
    }


}
