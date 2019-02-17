package com.projekt.klientrss.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.database.Category;
import com.projekt.klientrss.database.CategoryAdapter;
import com.projekt.klientrss.database.Channel;
import com.projekt.klientrss.database.ChannelsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscoverFragment extends Fragment {
    public static final String TAG = NewestFragment.class.getSimpleName();


    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    public EditText et1;
    public Spinner spinner2;

    public String linkuj = "";

    public ChannelsAdapter adapterek;
    public CategoryAdapter kategorie;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        mContext = getContext();
        root2 = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //wykonaj();
       /* ChannelsAdapter adapterek = new ChannelsAdapter(getContext());
        try {
            adapterek.createDatabase();
            adapterek.open();
            List<Channel> lista = adapterek.getAllChannels(1);

            LinearLayout a = new LinearLayout(mContext);


            TextView textView2 = new TextView(mContext);
            textView2.setText( "Kategoria: " );
            textView2.setTextSize(19);
            textView2.setPadding(10, 20, 10, 5);

            a.addView(textView2);

            for( Channel test : lista ){
                a.setOrientation(LinearLayout.VERTICAL);

                TextView textView = new TextView(mContext);
                textView.setText( test.getName() );
                textView.setTextSize(14);
                textView.setPadding(10, 20, 10, 5);

                a.addView(textView);
            }
            myRoot.addView( a );

        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterek.close();


        et1 = getView().findViewById( R.id.editText );
        et1.setText( "1" );
        et1.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {

                                Integer cat = Integer.parseInt( et1.getText().toString() );
                                myRoot.removeAllViews();

                                ChannelsAdapter adapterek = new ChannelsAdapter(getContext());
                                try {
                                    adapterek.createDatabase();
                                    adapterek.open();
                                    List<Channel> lista = adapterek.getAllChannels(cat);

                                    LinearLayout a = new LinearLayout(mContext);

                                    TextView textView2 = new TextView(mContext);
                                    textView2.setText( "Kategoria: " );
                                    textView2.setTextSize(19);
                                    textView2.setPadding(10, 20, 10, 5);

                                    a.addView(textView2);
                                    for( Channel test : lista ){
                                        a.setOrientation(LinearLayout.VERTICAL);

                                        TextView textView = new TextView(mContext);
                                        textView.setText( test.getName() );
                                        textView.setTextSize(14);
                                        textView.setPadding(10, 20, 10, 5);

                                        a.addView(textView);
                                    }
                                    myRoot.addView( a );

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                adapterek.close();

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
*/
        // new initList().execute();

    }

    public void wykonaj(){
        MainActivity.iwLove.setVisibility(View.INVISIBLE);
        myRoot = (LinearLayout) getView().findViewById(R.id.newestRoot);

        kategorie = new CategoryAdapter(getContext());
        try{
            spinner2 = getView().findViewById(R.id.lista);


            List<String> list = new ArrayList<String>();

            kategorie.createDatabase();
            kategorie.open();
            List<Category> lista = kategorie.getAllCategories();

            for( Category temp : lista )
                list.add( temp.getName() );

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(dataAdapter);

            // Here, you set the data in your ListView
            spinner2.setAdapter(dataAdapter);

            String currentItem = spinner2.getSelectedItem().toString();


            Category currentCat = kategorie.getCategoryByName( currentItem );

            adapterek = new ChannelsAdapter(mContext);


            try {
                adapterek.createDatabase();
                adapterek.open();

                List<Channel> lista2 = adapterek.getAllChannels( currentCat.getCatId());


                for( Channel test : lista2 ){
                    LinearLayout a = new LinearLayout(mContext);
                    a.setOrientation(LinearLayout.VERTICAL);

                    TextView textView = new TextView(mContext);
                    textView.setText( test.getName() );
                    textView.setTextSize(14);
                    textView.setPadding(10, 20, 10, 5);

                    a.addView(textView);

                    TextView textView2 = new TextView(mContext);
                    textView2.setText( test.getUrl()  );
                    textView2.setTextSize(14);
                    textView2.setPadding(10, 0, 10, 15);

                    a.addView(textView2);
                    myRoot.addView( a );
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }


            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String currentItem = spinner2.getSelectedItem().toString();
                    myRoot.removeAllViews();

                    kategorie.createDatabase();
                    kategorie.open();
                    final Category currentCat = kategorie.getCategoryByName( currentItem );

                    try {

                        adapterek = new ChannelsAdapter(mContext);
                        adapterek.createDatabase();
                        adapterek.open();
                        List<Channel> lista2 = adapterek.getAllChannels( currentCat.getCatId());

                        for( final Channel test : lista2 ){
                            LinearLayout a = new LinearLayout(mContext);
                            a.setOrientation(LinearLayout.VERTICAL);

                            TextView textView = new TextView(mContext);
                            textView.setText( test.getName() );
                            textView.setTextSize(14);
                            textView.setPadding(10, 20, 10, 5);

                            a.addView(textView);

                            final TextView textView2 = new TextView(mContext);
                            textView2.setText( test.getUrl()  );
                            linkuj = test.getUrl();
                            textView2.setTextSize(14);
                            textView2.setPadding(10, 0, 10, 15);

                            a.addView(textView2);
                            myRoot.addView( a );

                            a.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    MainActivity.disc.url2 = textView2.getText().toString();
                                    MainActivity.disc.channelId = test.getCatId();

                                    MainActivity.previousPosition.add( MainActivity.viewPager.getCurrentItem() );
                                    MainActivity.viewPager.setCurrentItem( 4 );
                                    MainActivity.lol = 0;

                                }
                            });

                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_LONG).show();


        }

        kategorie.close();
    }
}
