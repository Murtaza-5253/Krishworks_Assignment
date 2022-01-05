package com.igc.krishworksinternship;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SheetData extends AppCompatActivity
{
    RecyclerView rcvData;
    RequestQueue rq;
    String url;
    private ArrayList<UserModel> userModalArrayList;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_data);
        rq = Volley.newRequestQueue(this);
        rcvData = findViewById(R.id.rcvData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvData.setLayoutManager(layoutManager);
        userModalArrayList = new ArrayList<>();
        //sendData();
        accData();
    }
    private void accData()
    {
        hud = KProgressHUD.create(SheetData.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait");
        hud.show();
        url="https://script.google.com/macros/s/AKfycbyEAGMEWCpnTGTrZzzmPrsTA8ViXuxRKSlSkK0M5hDZMScSiuBfTBYEmf3xMUtaECtKhQ/exec?action=fetchStudent";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray entryArray = response.getJSONArray("items");
//                    Toast.makeText(SheetData.this, entryArray.length(), Toast.LENGTH_SHORT).show();
                    for(int i=0; i<entryArray.length(); i++){
                        JSONObject entryObj = entryArray.getJSONObject(i);
                        String studentId = entryObj.getString("Student_Id");
                        String studentName = entryObj.getString("Student_Name");
                        String marks = entryObj.getString("Marks");
                        userModalArrayList.add(new UserModel(studentId, studentName, marks));

                        // passing array list to our adapter class.

                    }
                    MyClass mc = new MyClass(userModalArrayList);
                    rcvData.setAdapter(mc);
                    hud.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    hud.dismiss();
                    Toast.makeText(SheetData.this, "hi", Toast.LENGTH_SHORT).show();
                    Log.d("message",e.getMessage());
                    Log.i("message",e.getMessage());
                    Log.e("message",e.getMessage());
                    Toast.makeText(SheetData.this, "Exception:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SheetData.this, "Error:- "+error.getMessage(), Toast.LENGTH_SHORT).show();
                hud.dismiss();
                Log.e("error",error.getMessage());
            }
        });
        rq.add(jsonObjectRequest);
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
            View view = LayoutInflater.from(SheetData.this).inflate(R.layout.data_design, parent, false);
            return new NewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull NewHolder holder, int position) {
            UserModel userModel = userModalArrayList.get(position);

            // on the below line we are setting data to our text view.
            holder.studentId.setText(userModel.getStudentId());
            holder.studentName.setText(userModel.getStudentName());
            holder.marks.setText(userModel.getmarks());

        }

        @Override
        public int getItemCount() {
           return userModalArrayList.size();

        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        class NewHolder extends RecyclerView.ViewHolder
        {
            private TextView studentId, studentName, marks;


            public NewHolder(@NonNull View itemView) {
                super(itemView);
                studentId = itemView.findViewById(R.id.txtId);
                studentName = itemView.findViewById(R.id.txtName);
                marks = itemView.findViewById(R.id.txtMarks);

            }
        }
    }
}