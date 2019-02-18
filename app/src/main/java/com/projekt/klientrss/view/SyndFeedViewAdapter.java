package com.projekt.klientrss.view;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;

import org.jsoup.Jsoup;

import java.util.Date;
import java.util.List;

public class SyndFeedViewAdapter {

    LinearLayout root;
    List<SyndEntry> entries;
    Context mContext;
    int previousPosition;

    public int id;
    View viewRoot;

    public SyndFeedViewAdapter( View root2, List<SyndEntry> _entries, Context context ){
        this.entries = _entries;
        this.viewRoot = root2;
        this.mContext = context;
    }

    public LinearLayout createView( SyndEntry test, int channelId , String[] params ){

        final SyndEntry syndEntry = test;
        LinearLayout a = new LinearLayout(mContext);
        a.setOrientation(LinearLayout.VERTICAL);


        id = channelId;
       for( String param : params ){
            TextView textView = new TextView(mContext);

            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.oxygen);

            if( param.equals("title") ){
                textView.setText( test.getTitle() );
                textView.setPadding(10, 20, 10, 5);
                textView.setTypeface(typeface, Typeface.BOLD);
                textView.setTextSize(17);
            }else if( param.equals( "description") ){

                String description = test.getDescription().getValue();
                description = html2text( description );

                textView.setText( description + "..." );
                textView.setPadding(10, 0, 10, 5);
                textView.setTypeface(typeface);

            }else if( param.equals( "date" ) ){
                Date date = test.getPublishedDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                textView.setText( dateFormat.format(date) );
                textView.setTypeface(typeface, Typeface.ITALIC);
                textView.setPadding(10, 0, 10, 20);
                textView.setTextSize(10);
            }

            a.addView(textView);
        }

        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    String title = syndEntry.getTitle();
                    Date date = syndEntry.getPublishedDate();
                    String link = syndEntry.getUri();
                    String description2 = syndEntry.getDescription().getValue();

                    Bundle bundle = new Bundle();
                    bundle.putString( "title", title );
                    bundle.putString( "link", link );

                    MainActivity.feedContent.title2 = title;
                    MainActivity.feedContent.link2 =link;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String data = dateFormat.format(date);

                    MainActivity.feedContent.date2 = data;
                    MainActivity.feedContent.desc2 = html2text( description2 );
                    MainActivity.feedContent.channelId2 = id;

                    MainActivity.previousPosition.add( MainActivity.viewPager.getCurrentItem() );
                    MainActivity.viewPager.setCurrentItem( 3 );

                MainActivity.exit = 0;

                MainActivity.feedContent.compute();
                }
        });

        return a;

    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }



}
