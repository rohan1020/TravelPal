package com.example.rohanraja.travelpal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class finder extends Activity implements LocationListener {

    public static String server_ip = "10.137.40.239:8081" ;
    boolean isDone = true ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);

        updateLocation();
//        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    void updateLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);


    }

    @Override
    public void onLocationChanged(Location location) {
        String curTxt = ("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //Log.d("myLoc", curTxt);

        if(isDone == true)
        {
            UpdateMyLocation up = new UpdateMyLocation();
            up.execute("rohan",String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            isDone = false;
        }



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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class UpdateMyLocation extends AsyncTask<String, String, Long> {

        protected void onPreExecute()
        {

        }

        protected Long doInBackground(String... inputPars) {
            long totalSize = 0;


            postData(inputPars);

            return totalSize;
        }

        protected void onProgressUpdate(String... progress) {
            //     setProgressPercent(progress[0]);

        }

        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");


        }


        public void postData(String... urlPars) {

            String pUser= urlPars[0];
            String pLat= urlPars[1];
            String pLong = urlPars[2];
            String url_base = "http://" + finder.server_ip + "/travelpal/update?user="+ pUser + "&lat=" + pLat + "&lon=" + pLong;

            try {


                String get_url = (url_base);

                Log.d("REQUEST", url_base);



                HttpClient Client = new DefaultHttpClient();
                HttpGet httpget;
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                httpget = new HttpGet(get_url);
                final String content = Client.execute(httpget, responseHandler);


                Log.d("LOCATIONCONT", content);

                final JSONObject[] jobj = {null};

                jobj[0] = (new JSONObject(content));


                JSONArray usersObj = jobj[0].getJSONArray("users");
                //String imgUrl = "http://" + finder.server_ip + "/travelpal/avatar/?user=" + usersObj.getJSONObject(1).getString("user_name");

                //URL newurl = new URL(imgUrl);
                //final Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

                finder.this.runOnUiThread(new Runnable() {
                    public void run() {


                        try {
                            jobj[0] = (new JSONObject(content));


                            final JSONArray usersObj = jobj[0].getJSONArray("users");


                            TextView tv;
                            ImageView imV;

                            tv = (TextView) findViewById(R.id.txt1) ;
                            tv.setText(usersObj.getJSONObject(0).getString("user_name") + ", " + usersObj.getJSONObject(0).getString("rating"));
                            imV = (ImageView) findViewById(R.id.im1);
//                            imV.setImageBitmap(mIcon_val);
                            imV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(finder.this, Locator.class);
                                    try {
                                        intent.putExtra("CHATEE",usersObj.getJSONObject(0).getString("jabber_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                            });


                            tv = (TextView) findViewById(R.id.txt2) ;
                            tv.setText(usersObj.getJSONObject(1).getString("user_name") + ", " + usersObj.getJSONObject(1).getString("rating"));
                            imV = (ImageView) findViewById(R.id.im2);
//                            imV.setImageBitmap(mIcon_val);
                            imV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(finder.this, Locator.class);
                                    try {
                                        intent.putExtra("CHATEE",usersObj.getJSONObject(1).getString("jabber_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                            });

                            tv = (TextView) findViewById(R.id.txt3) ;
                            tv.setText(usersObj.getJSONObject(2).getString("user_name") + ", " + usersObj.getJSONObject(2).getString("rating"));
                            imV = (ImageView) findViewById(R.id.im3);
//                            imV.setImageBitmap(mIcon_val);
                            imV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(finder.this, Locator.class);
                                    try {
                                        intent.putExtra("CHATEE",usersObj.getJSONObject(2).getString("jabber_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                            });

                            tv = (TextView) findViewById(R.id.txt4) ;
                            tv.setText(usersObj.getJSONObject(3).getString("user_name") + ", " + usersObj.getJSONObject(3).getString("rating"));
                            imV = (ImageView) findViewById(R.id.im4);
//                            imV.setImageBitmap(mIcon_val);
                            imV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(finder.this, Locator.class);
                                    try {
                                        intent.putExtra("CHATEE",usersObj.getJSONObject(3).getString("jabber_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //adapter.notifyDataSetChanged();
                    }
                });





            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("GmapsTime", e.toString());
            }
        }





        private byte[] readFully(InputStream inputStream)
                throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        }
    }}
