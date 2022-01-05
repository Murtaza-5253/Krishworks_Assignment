package com.igc.krishworksinternship;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    RequestQueue rq;
    TextInputEditText txtSID,txtSName,txtSMarks;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq = Volley.newRequestQueue(this);
        txtSID = findViewById(R.id.txtSID);
        txtSName = findViewById(R.id.txtSName);
        txtSMarks = findViewById(R.id.txtSMarks);

    }
    public void Send(View view)
    {
        sendData();
    }

    public void Fetch(View view)
    {
        startActivity(new Intent(MainActivity.this,SheetData.class));
    }

    public void Trigger(View view)
    {
        startActivity(new Intent(MainActivity.this,TriggerActivity.class));
    }
    private void sendData()
    {
        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait");
        hud.show();
        String url="https://script.google.com/macros/s/AKfycbzIr5kRHgqkW0gi6bVUTbFKRlZ1V7B4pSsjQqlpfWmzAWgHV8g_EGi0EngmrJFA9Vax6Q/exec";
        StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                hud.dismiss();
                Toast.makeText(MainActivity.this, "Response:- "+response, Toast.LENGTH_SHORT).show();
                txtSID.setText("");
                txtSMarks.setText("");
                txtSName.setText("");
                Log.e("Response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                hud.dismiss();
                Toast.makeText(MainActivity.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> M = new HashMap<>();
                M.put("action","addStudent");
                M.put("Student_ID",txtSID.getText().toString().trim());
                M.put("Student_Name",txtSName.getText().toString().trim());
                M.put("Marks",txtSMarks.getText().toString().trim());
                return M;
            }


        };
        int socketTimeout=50000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strq.setRetryPolicy(retryPolicy);
        rq.add(strq);
    }
}