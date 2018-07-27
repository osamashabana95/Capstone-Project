package com.example.osama.spot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    String[]list;
    String []list2;
    String []list3;
    String []list4;
    private Context ctxt=null;



    public ListRemoteViewsFactory(Context context, Intent intent){
        ctxt=context;
        list =intent.getStringArrayExtra("ext1");
        list2=intent.getStringArrayExtra("ext2");
        list3=intent.getStringArrayExtra("ext3");
        list4=intent.getStringArrayExtra("ext4");

    }




    @Override
    public void onCreate() {

    }



    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (list.length);
    }




    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(ctxt.getPackageName(), R.layout.spot_widget);
        views.setTextViewText(R.id.text_name_widget,list[i]);
        views.setTextViewText(R.id.text_title_widget,list2[i]);



        Intent intent=new Intent();
        Bundle extras=new Bundle();
        extras.putString("url", list4[i]);
        intent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.item_id, intent);

        return views;
    }




    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
