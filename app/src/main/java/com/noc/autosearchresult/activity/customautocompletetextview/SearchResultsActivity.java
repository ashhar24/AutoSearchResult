package com.noc.autosearchresult.activity.customautocompletetextview;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.noc.autosearchresult.R;

/**
 * Created by defoliate on 24-10-2015.
 */
public class SearchResultsActivity extends Activity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent (Intent intent)
    {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent (Intent intent)
    {

        if(Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
            ((TextView)findViewById(R.id.tvtest)).setText(query);
            Toast.makeText(SearchResultsActivity.this, query, Toast.LENGTH_SHORT).show();

        }

    }
}
