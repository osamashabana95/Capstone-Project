package com.example.osama.spot.fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.osama.spot.R;
import com.example.osama.spot.adapters.DBRecyclerViewAdapter;
import com.example.osama.spot.adapters.MyItemRecyclerViewAdapter;
import com.example.osama.spot.PostContract;
import com.example.osama.spot.utility.Utility;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment {

    private static final int LOADER_ID = 55;
    private static final int LOADER_ID_2 = 66;
    MyItemRecyclerViewAdapter mAdapter;
    DBRecyclerViewAdapter dAdapter;
    @BindView(R.id.list) RecyclerView recyclerView;
    SharedPreferences sharedpref;
    String[] tags = {"politics","worldnews","nba","Jokes","funny","soccer","pics","gaming","movies","news","science","aww","relationships","technology","sports","Documentaries","books","food"};
    ArrayList<String> names=new ArrayList<String>();
    Context context = getContext();
    LinearLayoutManager sLayoutManager;
    @BindView(R.id.no_departments_text) TextView textView;
    private OnListFragmentInteractionListener mListener;
    private int mPosition = RecyclerView.NO_POSITION;
    private Parcelable mListState1;


    // to load data and extract json  to get information needed
    private LoaderManager.LoaderCallbacks<String[]> load =new LoaderManager.LoaderCallbacks<String[]>() {



        @SuppressLint("StaticFieldLeak")
        @Nullable
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
                    ArrayList<String> keys = new ArrayList<String>();
                    if(args!=null){
                   keys = args.getStringArrayList("keys");
                    }
                   Log.v(getActivity().toString(),keys.toString());
                    URL[] requestUrl = Utility.buildUrl(keys);

                  //  Log.v(getActivity().toString(), String.valueOf(requestUrl));

                    try {

                        String[] jsonResponse = Utility.getResponseFromHttpUrl(requestUrl);

                       // Log.v(getActivity().toString(), String.valueOf(jsonResponse));
                       //String [] r = {jsonResponse};

                        return jsonResponse;


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
                try {
                    String[][] posts = Utility.getPostDetails(data);
                    Log.v(getActivity().toString(), String.valueOf(posts.length));
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.setData(posts);
                    sLayoutManager.onRestoreInstanceState(mListState1);


                  int n= getActivity().getContentResolver().delete(PostContract.PostEntry.CONTENT_URI,null,null);
                  Log.v(getActivity().toString(), "*/*/"+String.valueOf(n));
                    for(int i=0 ; i<posts.length;i++) {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PostContract.PostEntry.COLUMN_NAME, posts[i][0]);
                        contentValues.put(PostContract.PostEntry.COLUMN_TITLE, posts[i][1]);
                        contentValues.put(PostContract.PostEntry.COLUMN_PATH, posts[i][2]);
                        Uri uri = getActivity().getContentResolver().insert(PostContract.PostEntry.CONTENT_URI, contentValues);
                        if (uri != null) {
                            Log.v(getActivity().toString(), uri.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sLayoutManager.onRestoreInstanceState(mListState1);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String[]> loader) {

        }
    };


    // to load data from database
    private LoaderManager.LoaderCallbacks<Cursor> load2 =new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch(id){
                case LOADER_ID_2:
                    String sortOrder = PostContract.PostEntry._ID + " ASC";
                    return new CursorLoader(getActivity(),
                            PostContract.PostEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            sortOrder);


                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            dAdapter.swapCursor(data);

            /*if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

            recyclerView.smoothScrollToPosition(mPosition);*/

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            dAdapter.swapCursor(null);

        }
    };
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // to check each swith is on or off!!

    private void check() {
      for (String s : tags) {

        if (sharedpref.getBoolean(s, false) == true) names.add(s);
      }
    }


    // to save list state and position
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        mListState1 = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("state", mListState1);

    }


    // to   reload  state and position of list
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mListState1 = savedInstanceState.getParcelable("state");
        }
    }

    //to create fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this,view);
        sharedpref = this.getActivity().getSharedPreferences("my",0);
        check();

        Log.v(getActivity().toString(),names.toString());
            if(names.isEmpty()){
             textView.setVisibility(View.VISIBLE);
            }else {
            // Set the adapter

             Context context = view.getContext();

             sLayoutManager = new LinearLayoutManager(context);
             recyclerView.setLayoutManager(sLayoutManager);

             dAdapter = new DBRecyclerViewAdapter(context);
             recyclerView.setAdapter(dAdapter);

             LoaderManager loaderManager2 = getLoaderManager();
             android.support.v4.content.Loader<Object> loader2 = loaderManager2.getLoader(LOADER_ID_2);


              if (loader2 == null) {
                     loaderManager2.initLoader(LOADER_ID_2, null, load2);
              }
              loaderManager2.restartLoader(LOADER_ID_2, null, load2);



              mAdapter = new MyItemRecyclerViewAdapter(context, mListener);
              Bundle queryBundle = new Bundle();
              queryBundle.putStringArrayList("keys", names);
              LoaderManager loaderManager = getLoaderManager();
              android.support.v4.content.Loader<Object> loader = loaderManager.getLoader(LOADER_ID);
              if (loader == null) {
                   loaderManager.initLoader(LOADER_ID, queryBundle, load);
              }
              loaderManager.restartLoader(LOADER_ID, queryBundle, load);

        }

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

   @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(int id,String s1,String s2);
    }
}
