package com.example.osama.spot;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnListFragmentInteractionListener{
    protected static   boolean mTwoPane;
    HomeFragment fragment1;
    @BindView(R.id.adView) AdView mAdView;
   // private final String AD_ID= "ca-app-pub-9659885486560738~1361391166";
    Button update;
    EditFragment fragment2;
    FragmentManager fragmentManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    // to handle click update button in big size device case
    private  Button.OnClickListener mOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragment1 = new HomeFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container,fragment1).commit();
        }
    };


    // to control clicks on navidation view
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   fragment1 = new HomeFragment();
                   fragmentManager = getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.container,fragment1).commit();
                    return true;
                case R.id.navigation_edit:
                    fragment2 = new EditFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container,fragment2).commit();
                    return true;

            }
            return false;
        }
    };


// to create activity with it's fragment
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
      //  Log.v(this.getClass().toString(),AD_ID);
       // MobileAds.initialize(this, AD_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



            if (findViewById(R.id.two_pane) != null) {
                mTwoPane = true;
            } else {
                mTwoPane = false;
            }

            if (mTwoPane) {
                fragment2 = new EditFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.edit_container, fragment2).commit();
                update= (Button) findViewById(R.id.button_update);
                update.setOnClickListener(mOnClickListener);

            } else {
                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }

            if (savedInstanceState == null) {
                fragment1 = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.container, fragment1).commit();

            } else {
                fragment1 = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .show(fragment1)
                        .commit();


            }


    }

    // to handle click action on list item
    @Override
    public void onListFragmentInteraction(int id,String s1,String s2) {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, s1);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "list item");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        if(mTwoPane)
        {

           DetailsFragment fragment3 = new DetailsFragment();
            fragment3.setLink(s2);
            FragmentManager fragmentManager1 = getSupportFragmentManager();

            fragmentManager1.beginTransaction()
                    .replace(R.id.details_container, fragment3)
                    .commit();

        }else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("url", s2);
            startActivity(intent);
        }

    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "fragment", fragment1);
    }

}
