package com.example.dimaj.geo.houses;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dimaj.geo.R;

public class HousesListView
{
    private ArrayAdapter<String> list;

    public HousesListView(Context context, ListView listView)
    {
        list = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        listView.setAdapter(list);
    }

    public void add(String address)
    {
        list.add(address);
    }

    public void clear()
    {
        list.clear();
    }

}
