package com.opurex.ortus.client.adapters;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

public class DeviceListAdapter extends SimpleAdapter {
    private List<HashMap<String, String>> mDataList;

    public DeviceListAdapter(Context context, List<HashMap<String, String>> data,
                             int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.mDataList = data;
    }

    public void addItem(HashMap<String, String> item) {
        mDataList.add(item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public boolean containsDeviceWithMac(String mac) {
        for (HashMap<String, String> item : mDataList) {
            if (item.containsKey("MAC") && mac.equals(item.get("MAC"))) {
                return true;
            }
        }
        return false;
    }

    public List<HashMap<String, String>> getDataList() {
        return mDataList;
    }
}