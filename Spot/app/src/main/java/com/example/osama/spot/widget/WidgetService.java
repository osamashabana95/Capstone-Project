package com.example.osama.spot.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.osama.spot.widget.ListRemoteViewsFactory;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        return new ListRemoteViewsFactory(this.getApplicationContext(),intent);
    }

}
