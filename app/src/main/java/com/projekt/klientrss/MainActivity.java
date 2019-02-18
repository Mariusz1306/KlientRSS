package com.projekt.klientrss;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.projekt.klientrss.fragments.DiscoverFragment;
import com.projekt.klientrss.fragments.FeedContentFragment;
import com.projekt.klientrss.fragments.NewestFragment;
import com.projekt.klientrss.fragments.NoInternetFragment;
import com.projekt.klientrss.fragments.SavedFeedFragment;
import com.projekt.klientrss.fragments.SearchFragment;
import com.projekt.klientrss.fragments.TrackedFragment;
import com.projekt.klientrss.fragments.WebsiteFragment;
import com.projekt.klientrss.tabs.BottomBarAdapter;
import com.projekt.klientrss.tabs.NoSwipePager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public AHBottomNavigation bottomNavigation = null;
    public static NoSwipePager viewPager = null;
    public static List<Integer> previousPosition = new ArrayList<>();
    public static WebsiteFragment disc;
    public static FeedContentFragment feedContent;
    public static FeedContentFragment dynamic;

    public static SavedFeedFragment savedFragment;
    SearchFragment searchFragment;
    DiscoverFragment discover;

    public static  ImageView iwLove;
    public static  ImageView iwSaved;
    public static int lol = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        iwLove = findViewById( R.id.icon );
        iwSaved = findViewById( R.id.icon2 );

        /* MENU DOLNE

        https://github.com/aurelhubert/ahbottomnavigation

         */

        bottomNavigation = findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem( R.string.bottomnav_title_0, R.drawable.ic_home, R.color.color_tab_0 );
        AHBottomNavigationItem item2 = new AHBottomNavigationItem( R.string.bottomnav_title_1, R.drawable.ic_fire, R.color.color_tab_1 );
        AHBottomNavigationItem item3 = new AHBottomNavigationItem( R.string.bottomnav_title_2, R.drawable.ic_discover, R.color.color_tab_2 );

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setForceTint(false);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setCurrentItem(0);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);


        BottomBarAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        if ( isNetworkAvailable() ) {
            NewestFragment newest = new NewestFragment();
            pagerAdapter.addFragments(newest);

            TrackedFragment tracked = new TrackedFragment();
            pagerAdapter.addFragments(tracked);

            discover = new DiscoverFragment();
            pagerAdapter.addFragments( discover);

            feedContent = new FeedContentFragment();
            pagerAdapter.addFragments( feedContent );

            disc = new WebsiteFragment();
            pagerAdapter.addFragments( disc );

            savedFragment = new SavedFeedFragment();
            pagerAdapter.addFragments( savedFragment );

            searchFragment = new SearchFragment();
            pagerAdapter.addFragments( searchFragment );
        } else {
            NoInternetFragment noInternet1 = new NoInternetFragment();
            pagerAdapter.addFragments(noInternet1);
        }

        iwSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.previousPosition.add( viewPager.getCurrentItem() );
                viewPager.setCurrentItem( 5 );
                MainActivity.lol = 0;

            }
        });

        viewPager.setAdapter(pagerAdapter);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if( wasSelected )
                    return true;

                if( bottomNavigation != null && (position == 0 || position == 1 || position == 2) )
                    bottomNavigation.setNotification(new AHNotification(), position);

                if (!wasSelected){
                    MainActivity.previousPosition.add( viewPager.getCurrentItem() );
                    viewPager.setCurrentItem( position );
                    MainActivity.lol = 0;
                }

                switch( position ) {

                    case 0: break; //Najnowsze
                    case 1: break; //Sledzone
                    case 2: if (isNetworkAvailable()) discover.wykonaj(); break; //Odkrywaj
                    case 3: break; //FeedContent ?
                    case 4: break; //Website ?
                    case 5: break; //SavedFeed ?
                    case 6: break; //Search

                }

                return true;
            }
        });

        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {

            }
        });

        handleIntent(getIntent()); //Search
    }

    @Override
    protected void onNewIntent(Intent intent) { //Search
        handleIntent(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onBackPressed() {


        if( MainActivity.viewPager.getCurrentItem() != 0 && !MainActivity.previousPosition.isEmpty() ){
            lol = 0;
            MainActivity.viewPager.setCurrentItem( MainActivity.previousPosition.get( MainActivity.previousPosition.size() - 1 ) );
            MainActivity.previousPosition.remove( MainActivity.previousPosition.size() - 1);

            if( MainActivity.viewPager.getCurrentItem() == 0 || MainActivity.viewPager.getCurrentItem() == 1 || MainActivity.viewPager.getCurrentItem() == 2 )
                bottomNavigation.setCurrentItem( MainActivity.viewPager.getCurrentItem() );

            if( MainActivity.viewPager.getCurrentItem() == 2 )
                discover.wykonaj();
        } else if( lol == 1 ){
            finish();
            System.exit(0);
        }else{
            Toast.makeText(getBaseContext(), "Naciśnij jeszcze raz, by opuścić aplikację",
                    Toast.LENGTH_LONG).show();
            lol = 1;
        }

    }

    private void handleIntent(Intent intent) { //Search

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (disc.entriesBackup.size() > 1)
                searchFragment.wykonajCustom(query, disc.url2);
            else {
                searchFragment.wykonaj(query);
            }
            MainActivity.previousPosition.add(viewPager.getCurrentItem());
            viewPager.setCurrentItem(6);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Search
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}