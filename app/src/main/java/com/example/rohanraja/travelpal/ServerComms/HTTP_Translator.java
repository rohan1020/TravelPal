package com.example.rohanraja.travelpal.ServerComms;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rohanraja.travelpal.Locator;
import com.example.rohanraja.travelpal.OneComment;

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
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rohanraja on 18/01/15.
 */

public class HTTP_Translator extends AsyncTask<String, String, Long> {

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
        Locator.adapter.add(new OneComment(true, progress[0]));
    }

    protected void onPostExecute(Long result) {
        // showDialog("Downloaded " + result + " bytes");


    }

    public void postData(String... urlPars) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        String pText = "Au Revoir";
        String pLang = "fr";


        String url_base = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20150117T201452Z.f05f1043eb2cd4c6.4fcc4e7ae9b3c0538b2a5acb548f65b3ffb44c68&lang="+ pLang +"-en&text=" + pText;




        try {

            //    URI website = new URI(url_base + "22.3297383,87.2986599" +" &destination=" + "22.3193383,87.2996599");

            //  String get_url = (url_base);
            String get_url = (url_base);

//            HttpClient Client = new DefaultHttpClient();
//            HttpGet httpget;
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            httpget = new HttpGet(get_url);
//            String content = Client.execute(httpget, responseHandler);

            URL url = new URL(get_url);
            URLConnection urlConnection = url.openConnection();
            InputStream in = urlConnection.getInputStream();

            String content = new String(readFully(in),  "UTF-8");

            Log.d("HTTPCONT", content);

            parseJson(new JSONObject(content));


//            HttpGet request = new HttpGet();
//            request.setURI(website);
//
//            Log.d("GmapsTime", "Requesting = ");
//
//            HttpResponse response = httpclient.execute(request);
//
//            String responseStr = EntityUtils.toString(response.getEntity());

            //  Log.d("GmapsTime", "Response = " + content);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("GmapsTime", e.toString());
        }
    }


    public void parseJson(JSONObject jsonObj) throws JSONException
    {

        JSONArray legs = jsonObj.getJSONArray("text") ;

        onProgressUpdate(legs.get(0).toString());


        Log.d("THETEXT", "Text = " + legs.get(0));

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
}