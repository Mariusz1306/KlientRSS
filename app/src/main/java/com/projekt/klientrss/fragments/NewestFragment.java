package com.projekt.klientrss.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.network.RssAtomFeedRetriever;
import com.projekt.klientrss.view.SyndFeedViewAdapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class NewestFragment extends Fragment {

    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    LinearLayout.LayoutParams layoutParams;

    public NewestFragment() {

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
       new initList().execute();

    }

    private class initList extends AsyncTask<String, Integer, String>
    {
        private SyndFeed feed;

        @Override
        protected String doInBackground(String... params)
        {
            RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();
            feed = rssAtomFeedRetriever.getMostRecentNews( "https://fakty.interia.pl/feed" );
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            GifImageView gif = getView().findViewById( R.id.gifView2 );
            gif.setVisibility(View.INVISIBLE);

            List<SyndEntry> entries = feed.getEntries();
            SyndFeedViewAdapter lolek  = new SyndFeedViewAdapter( root2, entries, mContext );
            for( SyndEntry test : entries ){
                String[] params = {"title", "description", "date"};
                LinearLayout a = lolek.createView( test, 1, params );
                myRoot.addView( a );
            }

        }
    }



        /*ChannelsAdapter adapterek = new ChannelsAdapter(getContext());
        try {
            adapterek.createDatabase();
            adapterek.open();
            List<Channel> lista = adapterek.getAllChannels(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapterek.close();*/
}