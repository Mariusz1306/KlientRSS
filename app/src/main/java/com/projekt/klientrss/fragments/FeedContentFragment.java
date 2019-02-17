package com.projekt.klientrss.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.database.Entry;
import com.projekt.klientrss.database.EntryAdapter;

import static java.util.Objects.isNull;

public class FeedContentFragment extends Fragment {


    public static String title2;
    public static String link2;
    public static String date2;
    public static String desc2;
    public static int channelId2 = 0;
    public static int id2 = 0;

    TextView titl;
    TextView date;
    TextView desc;
    Button button2;
    Button button;

    public Context mContext;
    public View root;
    View rootView;

    public FeedContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_feed_content, container, false);
        mContext = getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        titl = rootView.findViewById( R.id.textView );
        date = rootView.findViewById( R.id.textView2 );
        desc = rootView.findViewById( R.id.textView3 );

        button2 =  rootView.findViewById(R.id.button2);
        button =  rootView.findViewById(R.id.button);
        this.compute();


    }

    public void compute(){

        MainActivity.iwLove.setVisibility(View.INVISIBLE);
        titl.setText( title2 );

        date.setText( date2 );

        desc.setText( desc2 + " " + channelId2 );


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToUrl ( link2 );
            }
        });

        EntryAdapter entries = new EntryAdapter(getContext());

        entries.createDatabase();
        entries.open();

        Entry test = null;
        try{
            test = entries.getEntryById( id2 );
        }catch(Exception e){
        }

        if (test == null) {
            button2.setText( "Zapisz na później" );
        }else{

            button2.setText( "Usuń z listy" );
        }


        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if( button2.getText().equals( "Zapisz na później" ) ){
                    EntryAdapter entries = new EntryAdapter(getContext());

                    entries.createDatabase();
                    entries.open();

                    Entry entry = new Entry( 0, title2, desc2, link2, channelId2, null );

                    entries.addEntry( entry );
                    button2.setText( "Usuń z listy" );
                }else{
                    EntryAdapter entries = new EntryAdapter(getContext());

                    entries.createDatabase();
                    entries.open();

                    entries.removeEntryById( id2 );

                    button2.setText( "Zapisz na później" );
                }

            }
        });
    }

    public static void setChannelId( int channelId ){

        channelId2 = channelId;

    }

    public static void setTitle( String title ){

        title2 = title;

    }

    public static void setLink( String title ){

        link2 = title;

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void setDate( String date ){

        TextView titl = rootView.findViewById( R.id.textView );
        titl.setText( date );

    }

    public void setDescription( String desc ){

        TextView titl = rootView.findViewById( R.id.textView );
        titl.setText( desc );

    }

}
