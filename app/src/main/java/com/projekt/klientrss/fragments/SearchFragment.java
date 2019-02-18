package com.projekt.klientrss.fragments;

/**
 * Created by Marius on 2019-02-17.
 */

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.network.RssAtomFeedRetriever;
import com.projekt.klientrss.view.SyndFeedViewAdapter;

import java.util.Iterator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SearchFragment extends Fragment {

    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    LinearLayout.LayoutParams layoutParams;
    private String query = null;
    private boolean done = false;
    private boolean custom = false;
    public static String url;

    public SearchFragment() {

        layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT  );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newest, container, false);
        mContext = getContext();
        root2 = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity.iwLove.setVisibility(View.INVISIBLE);
        myRoot = (LinearLayout) getView().findViewById(R.id.newestRoot);
        new SearchFragment.initList().execute();

    }

    public void wykonaj(String query){
        this.done = false;
        this.query = query;
        new SearchFragment.initList().execute();
    }

    public void wykonajCustom(String query, String url) {
        this.done = false;
        this.query = query;
        this.custom = true;
        this.url = url;
        new SearchFragment.initList().execute();
    }

    private class initList extends AsyncTask<String, Integer, String>
    {
        private SyndFeed feed;

        @Override
        protected String doInBackground(String... params)
        {
            RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();
            if (custom == true)
                feed = rssAtomFeedRetriever.getMostRecentNews( url );
            else
                feed = rssAtomFeedRetriever.getMostRecentNews( "https://fakty.interia.pl/feed" );
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            GifImageView gif = getView().findViewById( R.id.gifView2 );
            gif.setVisibility(View.INVISIBLE);

            List<SyndEntry> entries = feed.getEntries();

            if (query != null){
                for (Iterator<SyndEntry> iterator = entries.iterator(); iterator.hasNext();) {
                    SyndEntry entry = iterator.next();
                    if (!entry.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        // Remove the current element from the iterator and the list.
                        iterator.remove();
                    }
                }
            }

            if (done == false) {
                SyndFeedViewAdapter syndFeedViewAdapter = new SyndFeedViewAdapter(root2, entries, mContext);
                for (SyndEntry test : entries) {
                    String[] params = {"title", "description", "date"};
                    LinearLayout layout = syndFeedViewAdapter.createView(test, 1, params);
                    myRoot.addView(layout);
                }
            }
            done = true;
        }
    }
}