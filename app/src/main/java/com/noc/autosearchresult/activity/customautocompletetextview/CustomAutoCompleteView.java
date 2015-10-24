package com.noc.autosearchresult.activity.customautocompletetextview;

/*
 * Created by defoliate on 22-10-2015.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteView extends AutoCompleteTextView
{

    public CustomAutoCompleteView (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    // this is how to disable AutoCompleteTextView filter
    @Override
    protected void performFiltering (final CharSequence text, final int keyCode)
    {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

    /*
     * after a selection we have to capture the new value and append to the existing text
     */
    @Override
    protected void replaceText (final CharSequence text)
    {
        super.replaceText(text);
    }
}