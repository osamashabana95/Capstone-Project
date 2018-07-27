package com.example.osama.spot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private static final int LOADER_ID = 44;
    String link;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.subreddit_name) TextView textView_name;
    @BindView(R.id.title_text) TextView textView_title;
    @BindView(R.id.link_text) TextView textView_url;
    @BindView(R.id.comments_text) TextView textView_comments;
    Context context ;
    private String[] array =new  String[5];

    //to load post details data

    private LoaderManager.LoaderCallbacks<String[]> load =new LoaderManager.LoaderCallbacks<String[]>() {
        @SuppressLint("StaticFieldLeak")
        @NonNull
        @Override
        public Loader<String[]> onCreateLoader(int id, @Nullable final  Bundle args) {
            return new AsyncTaskLoader<String[]>(getContext()) {
                String[] mData = null;
                @Override
                protected void onStartLoading() {
                    if (mData != null) {
                        deliverResult(mData);
                    } else {
                        forceLoad();
                    }
                }

                @Nullable
                @Override
                public String[] loadInBackground() {
                    String s = null ;
                    if(args!=null){
                        s = args.getString("link");
                    }
                  //  Log.v(getActivity().toString(),s);
                    URL requestUrl = Utility.buildDetailsUrl(s);

                    //  Log.v(getActivity().toString(), String.valueOf(requestUrl));

                    try {

                        String jsonResponse = Utility.getResponseFromDetailsHttpUrl(requestUrl);
                        String[] details = Utility.getPostMoreDetails(jsonResponse);

                        // Log.v(getActivity().toString(), String.valueOf(jsonResponse));
                        //String [] r = {jsonResponse};

                        return details;


                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                @Override
                public void deliverResult(String[] data) {

                    mData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {
            if (data != null) {
                array=data;
               textView_name.setText(data[0]);
               textView_title.setText(data[1]);
               textView_url.setText(data[2]);
                if ( data[3]==null||TextUtils.isEmpty(data[3])) {
                    Picasso
                            .with(context)
                            .cancelRequest(imageView)
                    ;


                }
                else {
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(data[3].toString())
                            .fit()
                            .into(imageView);
                }
                textView_comments.setText(data[4]);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String[]> loader) {

        }
    };


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // to create  details fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this,view);
        context = view.getContext();
        Bundle queryBundle = new Bundle();
        queryBundle.putString("link",  link);
        LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<Object> loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(LOADER_ID, queryBundle, load);
        }
        loaderManager.restartLoader(LOADER_ID, queryBundle, load);

        return view;
    }

// to save state of fragment
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("details",array);


    }

    //to load the last state of fragment
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
           link=savedInstanceState.getString("url");
           array=savedInstanceState.getStringArray("details");
           textView_name.setText(array[0]);
           textView_title.setText(array[1]);
           textView_url.setText(array[2]);
            if ( array[3]==null||TextUtils.isEmpty(array[3])) {
                Picasso
                        .with(context)
                        .cancelRequest(imageView)
                ;


            }
            else {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(array[3].toString())
                        .fit()
                        .into(imageView);
            }
            textView_comments.setText(array[4]);



        }
    }



    // to save the link of post
    public  void setLink(String s){
        link = s;
    }

}
