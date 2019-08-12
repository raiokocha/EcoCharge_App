package com.example.yuusha.ecocharge.Controller;

import android.content.Context;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Model.Aparelho;
import com.example.yuusha.ecocharge.Model.Serial;
import com.example.yuusha.ecocharge.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SerialController extends ArrayAdapter<Serial> {

    private List<Serial> serialList;
    LayoutInflater inflater;
    private Context mCtx;
    private SparseBooleanArray mSelectedItemsIds;

    public SerialController(Context mCtx, int resourceId, List<Serial> serialList) {
        super(mCtx, resourceId, serialList);
        this.serialList = serialList;
        this.mCtx = mCtx;
        mSelectedItemsIds = new SparseBooleanArray();
        inflater =  LayoutInflater.from(mCtx);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int position1 = position;

        this.inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.single_item_serial, null, true);

        TextView textViewnome = listViewItem.findViewById(R.id.text_serial);

        Serial serial = serialList.get(position);

        textViewnome.setText((CharSequence) serial.getSerial());

        return listViewItem;
    }
}