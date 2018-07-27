package com.example.osama.spot;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity  {
    DetailsFragment fragment;
    FragmentManager fragmentManager;


    // to create details activity with it's fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
       String url = getIntent().getStringExtra("url");

        if (savedInstanceState == null) {
            fragment = new DetailsFragment();
            fragment.setLink(url);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.container2,fragment).commit();

        } else {
            fragment= (DetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .show(fragment)
                    .commit();


        }

    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
    }


}
