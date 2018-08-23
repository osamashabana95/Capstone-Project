package com.example.osama.spot.utility;

import android.net.Uri;
import android.util.Log;

import com.example.osama.spot.activities.DetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Utility {



    final static String REDDIT_BASE_URL = "https://www.reddit.com/r/";
    final static String DETAILS__BASE_URL = "https://www.reddit.com";




    /* to build needed url*/
    public static URL[] buildUrl(ArrayList<String> arrayList) {

       URL[] urls=new URL[arrayList.size()];
        for(String s:arrayList) {
            Uri uri = Uri.parse(REDDIT_BASE_URL).buildUpon()
                    .appendPath(s)
                    .appendPath("new.json")
                    .build();
            URL url = null;

            try {

                url = new URL(uri.toString());
                urls[arrayList.indexOf(s)] = url;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    /* to build needed url*/
    public static URL buildDetailsUrl(String s) {


            Uri uri = Uri.parse(DETAILS__BASE_URL+s+".json").buildUpon()
                    .build();
            URL url = null;

            try {

                url = new URL(uri.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        return url;
    }

    /*to make stable connection and get response*/
    public static String[] getResponseFromHttpUrl(URL[] url) throws IOException {
      String[] jsons = new String[url.length];
           for(int i = 0;i<url.length;i++) {
               HttpURLConnection urlConnection = (HttpURLConnection) url[i].openConnection();
               try {
                   InputStream in = urlConnection.getInputStream();

                   Scanner scanner = new Scanner(in);
                   scanner.useDelimiter("\\A");

                   boolean hasInput = scanner.hasNext();
                   if (hasInput) {
                       jsons[i] = scanner.next();

                   } else {
                       return null;
                   }
               } finally {
                   urlConnection.disconnect();
               }
           }
           return jsons;

    }

    /*to make stable connection and get response*/
    public static String getResponseFromDetailsHttpUrl(URL url) throws IOException {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                     return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }



    }



   // to get needed data from json and arrange posts
    public static String[][] getPostDetails(String[] json) throws JSONException {
        String[][] list = new String[json.length*25][4];

      //  Log.v(MainActivity.class.toString(), json[2]);
      //  Log.v(MainActivity.class.toString(), json[3]);

        for (int i = 0; i < 25; i++) {
            for (int l = 0; l < json.length; l++) {

            JSONObject jsonObject = new JSONObject(json[l]);
            JSONObject object = jsonObject.getJSONObject("data");
            JSONArray jsonArray = object.getJSONArray("children");
            JSONObject obj = jsonArray.getJSONObject(i);
            JSONObject obj1 = obj.getJSONObject("data");

                int n = l+(i*json.length);
                            list[n][0]= obj1.optString("subreddit");

                            list[n][1] = obj1.optString("title");

                            list[n][2] = obj1.optString("thumbnail");

                            list[n][3] = obj1.optString("permalink");

                }
            }

         //  for (int j=0;j<json.length*25;j++){
              //  Log.v(String.valueOf(MainActivity.class), list[j][0]+" ");
            //  Log.v(String.valueOf(MainActivity.class), list[j]+" ");
           // }

            return list;

    }

    // to get needed data from json for details
    public static String[] getPostMoreDetails(String json) throws JSONException {
                String[] list = new String[5];
                String s="";
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONObject object = jsonObject.getJSONObject("data");
                JSONArray jsonArray1 = object.getJSONArray("children");
                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                JSONObject object2 = jsonObject1.getJSONObject("data");
                list[0]= object2.optString("subreddit");
                int k = object2.optInt("num_comments");
                list[1] = object2.optString("title");

                list[2] = object2.optString("url");
              if(object2.has("preview")) {
            JSONObject object1 = object2.getJSONObject("preview");
            JSONArray array = object1.getJSONArray("images");
            JSONObject object3 = array.getJSONObject(0);
            JSONObject object4 = object3.getJSONObject("source");
            list[3] = object4.optString("url");
             }
                JSONObject jsonObject10 = jsonArray.getJSONObject(1);
                JSONObject jsonObject11 = jsonObject10.getJSONObject("data");
                JSONArray jsonArray10 = jsonObject11.getJSONArray("children");
                for (int i=0; i<jsonArray10.length();i++){
                    JSONObject jsonObject12 = jsonArray10.getJSONObject(i);
                    JSONObject jsonObject13 = jsonObject12.getJSONObject("data");
                    s+="   --"+jsonObject13.optString("body")+"\n\n" ;
                    if(jsonObject13.isNull("data")){}
                    else {
                        JSONObject jsonObject14 = jsonObject13.optJSONObject("replies");

                            JSONObject jsonObject15 = jsonObject14.optJSONObject("data");
                            JSONArray jsonArray11 = jsonObject15.optJSONArray("children");
                            for (int j = 0; j < jsonArray11.length(); j++) {
                                JSONObject jsonObject16 = jsonArray11.getJSONObject(j);
                                JSONObject jsonObject17 = jsonObject16.getJSONObject("data");
                                s += "         ----" + jsonObject17.optString("body") + "\n";
                            }
                        }

                   s+="\n\n\n ------------------------ \n";
                }
                list[4]=s;

        Log.v(String.valueOf(DetailsActivity.class), list[0]+" "+list[1]+" "+list[2]+" "+list[3]+" "+list[4]);
        return list;

    }





















}
