package com.projekt.klientrss.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;
import com.projekt.klientrss.database.Category;
import com.projekt.klientrss.database.CategoryAdapter;
import com.projekt.klientrss.database.Channel;
import com.projekt.klientrss.database.ChannelsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    public static final String TAG = NewestFragment.class.getSimpleName();


    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    public EditText et1;
    public Spinner spinner2;

    public String linkuj = "";

    public ChannelsAdapter adapter;
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

            adapter = new ChannelsAdapter(mContext);


            try {
                adapter.createDatabase();
                adapter.open();

                List<Channel> lista2 = adapter.getAllChannels( currentCat.getCatId());


                for( Channel test : lista2 ){
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    TextView textView = new TextView(mContext);
                    textView.setText( test.getName() );
                    textView.setTextSize(14);
                    textView.setPadding(10, 20, 10, 5);

                    layout.addView(textView);

                    TextView textView2 = new TextView(mContext);
                    textView2.setText( test.getUrl()  );
                    textView2.setTextSize(14);
                    textView2.setPadding(10, 0, 10, 15);

                    layout.addView(textView2);
                    myRoot.addView( layout );
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

                        adapter = new ChannelsAdapter(mContext);
                        adapter.createDatabase();
                        adapter.open();
                        List<Channel> lista2 = adapter.getAllChannels( currentCat.getCatId());

                        for( final Channel test : lista2 ){
                            LinearLayout layout = new LinearLayout(mContext);
                            layout.setOrientation(LinearLayout.VERTICAL);

                            TextView textView = new TextView(mContext);
                            textView.setText( test.getName() );
                            textView.setTextSize(14);
                            textView.setPadding(10, 20, 10, 5);

                            layout.addView(textView);

                            final TextView textView2 = new TextView(mContext);
                            textView2.setText( test.getUrl()  );
                            linkuj = test.getUrl();
                            textView2.setTextSize(14);
                            textView2.setPadding(10, 0, 10, 15);

                            layout.addView(textView2);
                            myRoot.addView( layout );

                            layout.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    MainActivity.disc.url2 = textView2.getText().toString();
                                    MainActivity.disc.channelId = test.getCatId();

                                    MainActivity.previousPosition.add( MainActivity.viewPager.getCurrentItem() );
                                    MainActivity.viewPager.setCurrentItem( 4 );
                                    MainActivity.exit = 0;

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
