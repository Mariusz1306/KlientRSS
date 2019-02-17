package com.projekt.klientrss.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.database.Category;
import com.projekt.klientrss.database.CategoryAdapter;
import com.projekt.klientrss.database.Channel;
import com.projekt.klientrss.database.Entry;
import com.projekt.klientrss.database.EntryAdapter;
import com.projekt.klientrss.network.RssAtomFeedRetriever;
import com.projekt.klientrss.view.SyndFeedViewAdapter;

import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static com.projekt.klientrss.view.SyndFeedViewAdapter.html2text;


public class SavedFeedFragment extends Fragment {

    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    LinearLayout.LayoutParams layoutParams;
    public String linkuj = "";

    public SavedFeedFragment() {

        layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT  );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_feed, container, false);
        mContext = getContext();
        root2 = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        myRoot = (LinearLayout) getView().findViewById(R.id.newestRoot);

        try{

            EntryAdapter entries = new EntryAdapter(getContext());

            entries.createDatabase();
            entries.open();
            List<Entry> lista = entries.getAllEntries();

            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.oxygen);

            for( final Entry test : lista ){
                LinearLayout a = new LinearLayout(mContext);
                a.setOrientation(LinearLayout.VERTICAL);

                TextView textView = new TextView(mContext);
                textView.setText( test.getName() );
                textView.setPadding(10, 20, 10, 5);
                textView.setTypeface(typeface, Typeface.BOLD);
                textView.setTextSize(17);

                a.addView(textView);

                TextView textView3 = new TextView(mContext);
                String date = test.getDate();

                textView3.setText( date );
                textView3.setTypeface(typeface, Typeface.ITALIC);
                textView3.setPadding(10, 0, 10, 20);
                textView3.setTextSize(10);
                a.addView( textView3 );

                String description = html2text( test.getDescription() );
                final TextView textView2 = new TextView(mContext);
                textView2.setText( test.getDescription()  );
                linkuj = description;

                textView2.setText( description + "..." );
                textView2.setPadding(10, 0, 10, 5);
                textView2.setTypeface(typeface);

                a.addView(textView2);
                myRoot.addView( a );

                a.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        MainActivity.feedContent.id2 = test.getId();
                        MainActivity.feedContent.title2 = test.getName();
                        MainActivity.feedContent.link2 = test.getLink();

                        String data = test.getDate();

                        MainActivity.feedContent.date2 = data;
                        MainActivity.feedContent.desc2 = html2text( test.getDescription() );
                        MainActivity.feedContent.channelId2 = test.getChannelId();

                        MainActivity.previousPosition.add( MainActivity.viewPager.getCurrentItem() );
                        MainActivity.viewPager.setCurrentItem( 3 );
                        MainActivity.lol = 0;
                        MainActivity.feedContent.compute();
                    }
                });

            }

        }catch( Exception e ){

        }

        MainActivity.iwLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources(); // need this to fetch the drawable
                Drawable draw = res.getDrawable( R.drawable.ic_love2);
                MainActivity.iwLove.setImageDrawable( draw );
            }
        });

        GifImageView gif = getView().findViewById( R.id.gifView2 );
        gif.setVisibility(View.INVISIBLE);

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