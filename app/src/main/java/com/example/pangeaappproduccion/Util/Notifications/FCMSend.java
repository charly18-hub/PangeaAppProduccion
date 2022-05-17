package com.example.pangeaappproduccion.Util.Notifications;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAAv-g-1UA:APA91bFR2frYGt4WPzSACY4Yhh-nO8K64dx7qtR8yAKTKlZ7oM3ongCOi6pQBLZOun2TBgK9KUSL_WWlhaVWbXz2l4H-XIfEdMcjWBXuMv5gy_uF3Y0DmSMOq1UrmUrnu3lwnPHN2uK9";

    public static void pushNotification(Context context, String token, String title, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JSONObject json = new JSONObject();
            json.put("to", token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            json.put("notification", notification);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, error -> Toast.makeText(context.getApplicationContext(), "Error"+ error, Toast.LENGTH_LONG).show()){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization",SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}