package com.example.osama.spot;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;

public class WidgetIntentService extends IntentService {
    SharedPreferences sharedpref;
    String[] tags = {"politics","worldnews","nba","videos","funny","soccer","pics","gaming","movies","news","gifs","aww","relationships","technology"};
    ArrayList<String> names=new ArrayList<String>();
    public static final String ACTION_WIDGETS = "spot";

    public WidgetIntentService() {
        super("WidgetIntentService");
    }



    public static void loadWidgets(Context context){


        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_WIDGETS);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WIDGETS.equals(action)) {
                handleLoadWidgets();
            }
        }
    }


    private void check() {
        for (String s : tags) {

            if (sharedpref.getBoolean(s, false) == true) names.add(s);
        }
    }

    private void handleLoadWidgets() {
        sharedpref = this.getSharedPreferences("my",0);
        check();
        URL[] requestUrl = Utility.buildUrl(names);

        //  Log.v(getActivity().toString(), String.valueOf(requestUrl));
        String[]name=new String[1];
        String[]title=new String[1];
        String[]path=new String[1];
        String[] url=new String[1];
        try {

           String[] jsonResponse = Utility.getResponseFromHttpUrl(requestUrl);

            //Log.v(getActivity().toString(), String.valueOf(jsonResponse));
            //String [] r = {jsonResponse};

            if (jsonResponse!= null) {

                String[][] posts = Utility.getPostDetails(jsonResponse);
                name=new String[posts.length];
                title=new String[posts.length];
                path=new String[posts.length];
                url=new String[posts.length];

                for(int i =0; i< posts.length;i++){
                    name[i]=posts[i][0];
                    title[i]=posts[i][1];
                    path[i]=posts[i][2];
                    url[i]=posts[i][3];

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, SpotWidget.class));


        //Now update all widgets
        SpotWidget.updatePlantWidgets(this, appWidgetManager, name,title,path,url,appWidgetIds);

    }
}
