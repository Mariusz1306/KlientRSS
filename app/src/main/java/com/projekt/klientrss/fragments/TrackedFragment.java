package com.projekt.klientrss.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.database.Channel;
import com.projekt.klientrss.database.ChannelsAdapter;
import com.projekt.klientrss.database.Follow;
import com.projekt.klientrss.database.FollowAdapter;
import com.projekt.klientrss.network.RssAtomFeedRetriever;
import com.projekt.klientrss.view.SyndFeedViewAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class TrackedFragment extends Fragment {

    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    LinearLayout.LayoutParams layoutParams;

    Map<String, Integer> mapa = new HashMap<>();

    public int channelIdActual = 1;

    public TrackedFragment() {

        layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT  );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracked, container, false);
        mContext = getContext();
        root2 = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity.iwLove.setVisibility(View.INVISIBLE);
        myRoot = (LinearLayout) getView().findViewById(R.id.newestRoot);

        //mapa.clear();

        FollowAdapter follows = new FollowAdapter( mContext );
        follows.createDatabase();
        follows.open();

        ChannelsAdapter channels = new ChannelsAdapter( mContext );
        channels.createDatabase();
        channels.open();

        List<Follow> sledzone = follows.getAllFollows();

        if( sledzone != null )
            Toast.makeText( mContext, follows.getFollowsCount(), Toast.LENGTH_LONG ).show();
        else
            Toast.makeText(mContext, "Nic nie ma :(", Toast.LENGTH_LONG).show();

       /* for( Follow temp : sledzone ){
            Channel kanal = channels.getChannel( temp.getChannelId() );
            mapa.put( kanal.getUrl(), temp.getChannelId() );

            if( kanal != null )
                new initList().execute( kanal.getUrl() );
        }*/

        follows.close();
        channels.close();

    }

    private class initList extends AsyncTask<String, Integer, String>
    {
        private SyndFeed feed;

        @Override
        protected String doInBackground(String... params)
        {
            RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();
            feed = rssAtomFeedRetriever.getMostRecentNews( params[0] );
            return params[0];
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
                String[] params2 = {"title", "description", "date"};
                LinearLayout a = lolek.createView( test, mapa.get( s ), params2 );
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