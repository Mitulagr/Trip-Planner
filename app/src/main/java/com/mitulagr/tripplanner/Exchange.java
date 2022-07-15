package com.mitulagr.tripplanner;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Exchange {

    Context context;
    double rate;

    public Exchange(Context con) {
        context = con;
    }

    double getRate(String c1, String c2){

        //final double[] rate = new double[1];

        String url = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"+c1+"/"+c2+".json";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                //rate[0] = Double.parseDouble(parseJsonData(string,c2));
                parseJsonData(string,c2);
                //Toast.makeText(context,String.valueOf(rate), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
        //rQueue.start();

        Toast.makeText(context,String.valueOf(rate), Toast.LENGTH_SHORT).show();

        return rate;
    }

    void parseJsonData(String jsonString, String c) {
        try {
            JSONObject object = new JSONObject(jsonString);
            //return object.getString(c);
            rate = Double.parseDouble(object.getString(c));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return "0.0";
    }

}
