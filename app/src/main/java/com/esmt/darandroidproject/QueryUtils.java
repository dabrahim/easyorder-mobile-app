package com.esmt.darandroidproject;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String CLIENT_REGISTRATION_URL = "http://192.168.2.76/easyorder/client/add";

    private QueryUtils (){
    }

    /**
     *
     * @param urlString
     * @return
     */
    public static URL createUrl(String urlString) {
        URL url = null;
        try{
            url = new URL(urlString);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Unable to create URL", e);
        }
        return url;
    }

    /**
     *
     * @param params
     * @return
     */
    public static String registerClient (HashMap<String, String> params) {
        URL url = createUrl( CLIENT_REGISTRATION_URL );
        String jsonResponse = makeHttpRequest(url, "POST", params);
        return jsonResponse;
    }

    /**
     *
     * @param url
     * @param verb
     * @param params
     * @return
     */
    public static String makeHttpRequest(URL url, String verb, HashMap<String, String> params) {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod(verb);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            if (params != null) {
                Uri.Builder builder = new Uri.Builder();

                Iterator it = params.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry pair = (Map.Entry) it.next();
                    builder.appendQueryParameter((String)pair.getKey(), (String) pair.getValue());
                    it.remove();
                }
                String query = builder.build().getEncodedQuery();

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }

            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readInputStream(inputStream);

        } catch (IOException e){
            Log.e(LOG_TAG, "Error making HTTP request", e);
        }

        return  jsonResponse;
    }

    /**
     *
     * @param inputStream
     * @return
     */
    private static String readInputStream(InputStream inputStream) {
        StringBuffer jsonResponse = new StringBuffer ();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String currentLine = null;
            do {
                currentLine = bufferedReader.readLine();
                if( currentLine != null ) {
                    jsonResponse.append(currentLine);
                }
            } while (currentLine != null);

        } catch (IOException e){
            Log.e(LOG_TAG, "ERROR reading InputStream", e);
        }

        return  jsonResponse.toString();
    }

}
