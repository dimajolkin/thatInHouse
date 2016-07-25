package com.example.dimaj.geo.houses;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dimaj.geo.R;

import java.util.ArrayList;
import java.util.List;

public class HousesListView {
    private ArrayAdapter<String> list;
    private ArrayList<String> strings;

    public HousesListView(Context context, ListView listView) {
        list = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        strings = new ArrayList<>();
        listView.setAdapter(list);
    }

    public void add(String address)
    {
        if (strings.contains(address)) {
            return;
        }

        strings.add(address);
        list.add(address);
    }

    public void clear() {

        list.clear();
        strings.clear();
    }

}
