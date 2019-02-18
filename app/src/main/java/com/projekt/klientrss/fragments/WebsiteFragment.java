package com.projekt.klientrss.fragments;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.database.EntryAdapter;
import com.projekt.klientrss.database.Follow;
import com.projekt.klientrss.database.FollowAdapter;
import com.projekt.klientrss.network.RssAtomFeedRetriever;
import com.projekt.klientrss.view.SyndFeedViewAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class WebsiteFragment extends Fragment {

    public static Context mContext;
    public static LinearLayout myRoot;
    public View root2;

    public static int choosen = 1;
    public static int channelId = -1;

    public static String url2 = "https://fakty.interia.pl/feed" ;

    LinearLayout.LayoutParams layoutParams;
    public static List<SyndEntry> entriesBackup = new ArrayList<>();

    public WebsiteFragment() {

        layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT  );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( null );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_website, container, false);
        mContext = getContext();
        root2 = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        choosen = 0;
        myRoot = (LinearLayout) getView().findViewById(R.id.websiteRoot);
        new initList2().execute();

    }

    private class initList2 extends AsyncTask<String, Integer, String>
    {
        private SyndFeed feed2 = null;

        @Override
        protected String doInBackground(String... params)
        {
            RssAtomFeedRetriever rssAtomFeedRetriever = new RssAtomFeedRetriever();

            try {
                feed2 = rssAtomFeedRetriever.getMostRecentNews(url2);
            }catch( final Exception e ){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            GifImageView gif = getView().findViewById( R.id.gifView2 );
            gif.setVisibility(View.INVISIBLE);

            TextView tw2 = getView().findViewById( R.id.textView4 );
            tw2.setVisibility(View.INVISIBLE);



            if( feed2 != null ) {
                List<SyndEntry> entries = feed2.getEntries();
                entriesBackup.addAll(entries);

                SyndFeedViewAdapter syndFeedViewAdapter = new SyndFeedViewAdapter(root2, entries, mContext);
                for (SyndEntry test : entries) {
                    String[] params = {"title", "description", "date"};
                    LinearLayout layout = syndFeedViewAdapter.createView(test, channelId, params);
                    myRoot.addView(layout);
                }

                MainActivity.iwLove.setVisibility(View.VISIBLE);

                final FollowAdapter follows = new FollowAdapter(getContext());
                follows.createDatabase();
                follows.open();

                Follow exists = null;
                try{
                    exists = follows.getFollowById( channelId );
                }catch( Exception e ){

                }

                if( exists == null ){
                    Resources res = getResources(); // need this to fetch the drawable
                    Drawable draw = res.getDrawable( R.drawable.ic_love);
                    MainActivity.iwLove.setImageDrawable( draw );
                }else{
                    Resources res = getResources(); // need this to fetch the drawable
                    Drawable draw = res.getDrawable( R.drawable.ic_love2);
                    MainActivity.iwLove.setImageDrawable( draw );
                }

                follows.close();

                MainActivity.iwLove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FollowAdapter follows2 = new FollowAdapter(getContext());
                        follows2.createDatabase();
                        follows2.open();
                        Follow exists2 = null;
                        try{
                            exists2 = follows2.getFollowById( channelId );
                        }catch( Exception e ){}

                        if( exists2 == null ){
                            follows2.addFollow( new Follow( 0, channelId, " " ) );
                            Resources res = getResources(); // need this to fetch the drawable
                            Drawable draw = res.getDrawable( R.drawable.ic_love2);
                            MainActivity.iwLove.setImageDrawable( draw );
                        }else{
                            try {
                                follows2.removeFollowById(channelId);
                            }catch( Exception e ){
                                Toast.makeText(getActivity(), channelId,
                                        Toast.LENGTH_LONG).show();
                            }
                            Resources res = getResources(); // need this to fetch the drawable
                            Drawable draw = res.getDrawable( R.drawable.ic_love);
                            MainActivity.iwLove.setImageDrawable( draw );
                        }
                        follows2.close();

                    }
                });
            }else {
                ImageView iw = new ImageView(mContext);

                Resources res = getResources(); // need this to fetch the drawable
                Drawable draw = res.getDrawable( R.drawable.blad );
                iw.setImageDrawable(draw);
                myRoot.addView(iw);
            }


        }
    }
}
