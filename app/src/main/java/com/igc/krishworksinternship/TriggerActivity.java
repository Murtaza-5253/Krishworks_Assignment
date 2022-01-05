package com.igc.krishworksinternship;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerActivity extends AppCompatActivity
{
    RecyclerView rcvTData;
    RequestQueue rq;
    String url,result;
    private ArrayList<UserModel> userTModalArrayList;
    List<String> resultlist;
    String[] res;
    KProgressHUD hud;
    private boolean color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        rcvTData = findViewById(R.id.rcvTrData);
        rq = Volley.newRequestQueue(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvTData.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(TriggerActivity.this,LinearLayoutManager.VERTICAL);
        rcvTData.addItemDecoration(decoration);
        userTModalArrayList = new ArrayList<>();
        resultlist = new ArrayList<>();
        //sendData();
        accData();
    }

    private void accData()
    {
         hud = KProgressHUD.create(TriggerActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait");
        hud.show();
        url="https://script.google.com/macros/s/AKfycbzMDjkJHBxvFjckOkIYq9D9b6NvNw-1g_v4qBbGfXUHrpsoN6W4qED20lyf2GiA8Ehm8Q/exec?action=doTrigger";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray entryArray = response.getJSONArray("items");
                    res = new String[entryArray.length()];
//                    Toast.makeText(SheetData.this, entryArray.length(), Toast.LENGTH_SHORT).show();
                    for(int i=0; i<entryArray.length(); i++){
                        JSONObject entryObj = entryArray.getJSONObject(i);
                        String studentId = entryObj.getString("Student_Id");
                        String studentName = entryObj.getString("Student_Name");
                        String marks = entryObj.getString("Marks");
                        result = value(marks);
                        userTModalArrayList.add(new UserModel(studentId, studentName, marks,result));
                        resultlist.add(result);

                    }
                    String st1 = String.join(",",resultlist).replaceAll("([^,]+)","\"$1\"");
                    Log.e("re", String.valueOf(st1));
                    MyClass mc = new MyClass(userTModalArrayList);
                    rcvTData.setAdapter(mc);

                    sendResult(st1);

                } catch (JSONException e) {
                    e.printStackTrace();
                    hud.dismiss();
                    Toast.makeText(TriggerActivity.this, "hi", Toast.LENGTH_SHORT).show();
                    Log.d("message",e.getMessage());
                    Log.i("message",e.getMessage());
                    Log.e("message",e.getMessage());
                    Toast.makeText(TriggerActivity.this, "Exception:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                Toast.makeText(TriggerActivity.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error",error.getMessage());
            }
        });
        rq.add(jsonObjectRequest);
    }
    private void sendResult(String st1)
    {
        url = "https://script.google.com/macros/s/AKfycbwlWuG87ix4ogFHjFc7An7j4fm2YlMWIoakZB2hMzXB7zYsRcZr0IblkE5MQizpmzd7jA/exec?action=doResult&MResult="+ st1;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(TriggerActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.e("Response",response);
                hud.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                Toast.makeText(TriggerActivity.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(stringRequest);
    }
    class MyClass extends RecyclerView.Adapter<MyClass.NewHolder>
    {
        private ArrayList<UserModel> userModels;
        public MyClass(ArrayList<UserModel> userModels){
            this.userModels = userModels;
        }
        @NonNull
        @Override
        public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TriggerActivity.this).inflate(R.layout.trigger_design, parent, false);
            return new NewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position) {
            UserModel userModel = userTModalArrayList.get(position);

            // on the below line we are setting data to our text view.
            holder.studentId.setText(userModel.getStudentId());
            holder.studentName.setText(userModel.getStudentName());
            holder.marks.setText(userModel.getmarks());
            holder.results.setText(userModel.getResult());


        }

        @Override
        public int getItemCount() {
            return userTModalArrayList.size();

        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        class NewHolder extends RecyclerView.ViewHolder
        {
            private TextView studentId, studentName, marks,results;


            public NewHolder(@NonNull View itemView) {
                super(itemView);
                studentId = itemView.findViewById(R.id.txtId);
                studentName = itemView.findViewById(R.id.txtName);
                marks = itemView.findViewById(R.id.txtMarks);
                results = itemView.findViewById(R.id.txtResult);

            }
        }
    }
    public String value(String marks)
    {
        int i = Integer.parseInt(marks);
        if (i<40){
            color=true;
            return   "Fail";
        }
        else{
            color=false;
            return  "Pass";
        }
    }

}