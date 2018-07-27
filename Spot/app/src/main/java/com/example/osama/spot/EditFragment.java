package com.example.osama.spot;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EditFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    SharedPreferences sharedpref;


    @BindView(R.id.switch_politics) Switch politics;
    @BindView(R.id.switch_worldnews) Switch worldNews;
    @BindView(R.id.switch_nba) Switch nba;
    @BindView(R.id.switch_funny) Switch funny;
    @BindView(R.id.switch_soccer) Switch soccer;
    @BindView(R.id.switch_pics) Switch pics;
    @BindView(R.id.switch_gaming) Switch gaming;
    @BindView(R.id.switch_movies) Switch movies;
    @BindView(R.id.switch_news) Switch news;
    @BindView(R.id.switch_pictures) Switch pictures;
    @BindView(R.id.switch_relationships) Switch relationships;
    @BindView(R.id.switch_technology) Switch technology;
    @BindView(R.id.switch_jokes) Switch jokes;
    @BindView(R.id.switch_science) Switch science;
    @BindView(R.id.switch_sports) Switch sports;
    @BindView(R.id.switch_documentaries) Switch documentaries;
    @BindView(R.id.switch_books) Switch books;
    @BindView(R.id.switch_food) Switch food;



    public EditFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //to create fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_edit, container, false);

        ButterKnife.bind(this, view);

        sharedpref = this.getActivity().getSharedPreferences("my",0);

        politics.setOnCheckedChangeListener(this);
        worldNews.setOnCheckedChangeListener(this);
        nba.setOnCheckedChangeListener(this);
        jokes.setOnCheckedChangeListener(this);
        funny.setOnCheckedChangeListener(this);
        soccer.setOnCheckedChangeListener(this);
        pics.setOnCheckedChangeListener(this);
        gaming.setOnCheckedChangeListener(this);
        movies.setOnCheckedChangeListener(this);
        news.setOnCheckedChangeListener(this);
        science.setOnCheckedChangeListener(this);
        pictures.setOnCheckedChangeListener(this);
        relationships.setOnCheckedChangeListener(this);
        technology.setOnCheckedChangeListener(this);
        sports.setOnCheckedChangeListener(this);
        documentaries.setOnCheckedChangeListener(this);
        books.setOnCheckedChangeListener(this);
        food.setOnCheckedChangeListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        check(politics);
        check(worldNews);
        check(nba);
        check(jokes);
        check(funny);
        check(soccer);
        check(pics);
        check(gaming);
        check(movies);
        check(news);
        check(science);
        check(pictures);
        check(relationships);
        check(technology);
        check(sports);
        check(documentaries);
        check(books);
        check(food);
    }

    //to check each switch state from shared preferences and load each switch with it's state

    public void check(Switch sw){
       // Log.v(getActivity().getBaseContext().toString(), (String) sw.getTag());
        String tag =(String) sw.getTag();

        boolean state = sharedpref.getBoolean(tag,false);
        String s = sharedpref.getString(tag+tag,"OFF");
        if(sharedpref.contains(tag)) {
            Log.v(getActivity().getBaseContext().toString(),tag);
                sw.setChecked(state);
                sw.setText(s);
                Log.v(getActivity().getBaseContext().toString(),s);

        }


    }




    //to save switch state when changed
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor=sharedpref.edit();
        String name= (String) compoundButton.getTag();
        Log.v(getActivity().getBaseContext().toString(),name);
        if(b){
            compoundButton.setText("ON");
            editor.putBoolean(name,true);
            editor.putString(name+name,"ON");
        }else{
            compoundButton.setText("OFF");
            editor.putBoolean(name,false);
            editor.putString(name+name,"OFF");
        }
        editor.commit();
    }


}
