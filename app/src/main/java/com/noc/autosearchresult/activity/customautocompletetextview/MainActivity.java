package com.noc.autosearchresult.activity.customautocompletetextview;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.noc.autosearchresult.R;
import com.noc.autosearchresult.handler.DatabaseHandler;
import com.noc.autosearchresult.helper.MyObject;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    /*
     * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
     * since we are extending to customize the view and disable filter
     * The same with the XML view, type will be CustomAutoCompleteView
     */
    CustomAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // adapter for auto-search
    SimpleCursorAdapter searchAdapter;

    // for database operations
    DatabaseHandler databaseH;

    // just to add some initial value
    String[] item = new String[]{"Please search..."};

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try
        {
            // instantiate database handler
            databaseH = new DatabaseHandler(MainActivity.this);

            // put sample data to database
            insertSampleData();

            // autocompletetextview is in content_mainxml
            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

            // set our adapter
            myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);

            final String[] from = new String[]{"cityName"};
            final int[] to = new int[]{android.R.id.text1};
            searchAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
            {
                @Override
                public View newView (Context context, Cursor cursor, ViewGroup parent)
                {
                    View newView = super.newView(context, cursor, parent);
                    (newView.findViewById(android.R.id.text1)).setBackgroundColor(Color.WHITE);
                    return newView;
                }
            };
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSuggestionsAdapter(searchAdapter);
        //searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
        {
            @Override
            public boolean onSuggestionSelect (int position)
            {
                return false;
            }

            @Override
            public boolean onSuggestionClick (int position)
            {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit (String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange (String newText)
            {
                if(newText != null)
                {
                    populateAdapter(getItemsFromDb(newText));
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void populateAdapter (String[] query)
    {
        MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
        for(int i = 0; i < query.length; i++)
        {
            c.addRow(new Object[]{i, query[i]});
        }
        searchAdapter.changeCursor(c);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Take appropriate action for each action item click
        switch(item.getItemId())
        {
            case R.id.action_search:
                // search action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertSampleData ()
    {
        databaseH.create(new MyObject("January"));
        databaseH.create(new MyObject("February"));
        databaseH.create(new MyObject("March"));
        databaseH.create(new MyObject("April"));
        databaseH.create(new MyObject("May"));
        databaseH.create(new MyObject("June"));
        databaseH.create(new MyObject("July"));
        databaseH.create(new MyObject("August"));
        databaseH.create(new MyObject("September"));
        databaseH.create(new MyObject("October"));
        databaseH.create(new MyObject("November"));
        databaseH.create(new MyObject("December"));
        databaseH.create(new MyObject("New Caledonia"));
        databaseH.create(new MyObject("New Zealand"));
        databaseH.create(new MyObject("Papua New Guinea"));
        databaseH.create(new MyObject("COFFEE-1K"));
        databaseH.create(new MyObject("coffee raw"));
        databaseH.create(new MyObject("authentic COFFEE"));
        databaseH.create(new MyObject("k12-coffee"));
        databaseH.create(new MyObject("view coffee"));
        databaseH.create(new MyObject("Indian-coffee-two"));
    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getItemsFromDb (String searchTerm)
    {
        // add items on the array dynamically
        List<MyObject> products = databaseH.read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for(MyObject record : products)
        {
            item[x] = record.objectName;
            x++;
        }
        return item;
    }
}
