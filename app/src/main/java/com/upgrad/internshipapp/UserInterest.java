package com.upgrad.internshipapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInterest extends AppCompatActivity {
    String JSON_STRING;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TagsAdapter tagsAdapter;
    TagsHelper tagsHelper;
    String json;
    ArrayList<Tags> arrayList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interest);
        tagsHelper=new TagsHelper(UserInterest.this);
        arrayList=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(UserInterest.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        load();
    }
    public void load(){
        try {
                final ProgressDialog prd=new ProgressDialog(UserInterest.this);
                prd.setMessage("Loading Tags");
                prd.show();
                String url = "https://api.stackexchange.com/2.2/tags?order=desc&sort=popular&site=stackoverflow";
                // mProgressBar.setVisibility(View.VISIBLE);
                Map<String, String> params = new HashMap<String, String>();


                CustomRequest jsObjRequest = new CustomRequest(Request.Method.GET, url, params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response: ", response);
                            json = response;
                            json();
                            prd.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        //Log.d("Response: ", response.toString());
                        try {
                            Toast.makeText(UserInterest.this, "Connectivity Error!! Please Try Again", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                          prd.dismiss();
                    }
                });

                AppController.getInstance().addToRequestQueue(jsObjRequest);
                //

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(UserInterest.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
        }
    }
    public void json(){

        //Log.v("String","fdd"+res);
        int count=0;
        try {
            tagsHelper.drop();
            jsonObject = new JSONObject(json);
            jsonArray=jsonObject.getJSONArray("items");
             while (count<jsonArray.length() ){
                JSONObject JO= jsonArray.getJSONObject(count);
                String name=JO.getString("name");
                String count1=JO.getString("count");

                Tags event=new Tags(name,count1,"NO");
                arrayList.add(event);
                tagsHelper.addTag(event);
                count++;
            }
            //display();
            tagsAdapter=new TagsAdapter(arrayList,UserInterest.this,recyclerView);
            recyclerView.setAdapter(tagsAdapter);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void submit(View view){

        Log.v("Selected" ,tagsAdapter.getSelCount()+"");
        ArrayList<Tags> arrayList1=tagsHelper.getAllTags();
        for(int i=0; i<arrayList1.size();i++) {
            if(arrayList1.get(i).getSelected().equals("YES"))
            Log.v("Items - ", arrayList1.get(i).getName());
        }
    }

}
