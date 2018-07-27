package com.example.osama.spot;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        return new ListRemoteViewsFactory(this.getApplicationContext(),intent);
    }

}
