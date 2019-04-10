package com.upgrad.internshipapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class QuestionsFragment extends Fragment {

    TagsHelper tagsHelper;
    QuestionsAdapter questionsAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Questions> questions;

    private OnFragmentInteractionListener mListener;

    public QuestionsFragment() {
        // Required empty public constructor
    }


    public static QuestionsFragment newInstance(String tags,String type) {
        QuestionsFragment fragment = new QuestionsFragment();

        Bundle args = new Bundle();
        args.putString("tags", tags);
        args.putString("type", type);

        fragment.setArguments(args);
        return fragment;
    }
    String url="";
    String tags,type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            tags = getArguments().getString("tags");
            type=getArguments().getString("type");
            url="https://api.stackexchange.com/2.2/questions?order=desc&sort="+type+"&tagged="+tags+"&site=stackoverflow";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        //
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    TextView empty;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        refreshLayout=(SwipeRefreshLayout) v.findViewById(R.id.refresh);
        //but=(ImageView) findViewById(R.id.feedsendButton);
        // msg=(EditText) findViewById(R.id.messageEditText);
        recyclerView=(RecyclerView) v.findViewById(R.id.eventRecycler);

        questions=new ArrayList<>();
        tagsHelper=new TagsHelper(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SharedPreferenceUtils sharedPreferenceUtils1=new SharedPreferenceUtils(getContext(),"ques");
        tags=sharedPreferenceUtils1.getStringValue("tag","");
        Toast.makeText(getContext(),"Tag - "+tags,Toast.LENGTH_SHORT).show();
        url="https://api.stackexchange.com/2.2/questions?order=desc&sort="+type+"&tagged="+tags+"&site=stackoverflow";
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                display();
                load();

            }
        });


        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                display();
                load();

            }
        });
        display();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void display(){
        //events=eventsHelper.getAllEvents();
        Log.v("Ada dis called",questions.size()+"");
        questionsAdapter=new QuestionsAdapter(questions,getActivity(),recyclerView);
        recyclerView.setAdapter(questionsAdapter);
    }
    public void load(){

        if(isNetworkAvailable()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
           // String url = "https://api.stackexchange.com/2.2/questions?order=desc&sort=activity&tagged=python&site=stackoverflow";

            Map<String, String> params = new HashMap<String, String>();

            CustomRequest jsObjRequest = new CustomRequest(Request.Method.GET, url, params, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("Response: ", response);
                        json(response);
                        //prd.dismiss();
                        refreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            });

            AppController.getInstance().addToRequestQueue(jsObjRequest);
            //
        }
        else{
//            mProgressBar.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),"Network Unavailable",Toast.LENGTH_SHORT).show();
        }
    }
    JSONObject jsonObject;
    JSONArray jsonArray;
    public void json(String res){
        //Log.v("String","fdd"+res);
        questions.clear();
        try {
            //.drop();
            int count=0;
            jsonObject = new JSONObject(res);
            jsonArray=jsonObject.getJSONArray("items");
            // Log.v("JSONARRAY",jsonArray.length()+"HJG"+jsonArray);
            while (count<jsonArray.length()){
                //   Log.v("PREPAP","sdjfhjhbd");
                String own="",pic="";
                JSONObject JO= jsonArray.getJSONObject(count);
//                JSONObject jj=jsonObject.getJSONObject("items");
//                JSONArray owner=jj.getJSONArray("owner");
//                for(int j=0;j<owner.length();j++){
//                    JSONObject Own= owner.getJSONObject(j);
//                    own=Own.getString("display_name");
//                    pic=Own.getString("profile_image");
//                }
                String id=JO.getString("question_id");
                String title=JO.getString("title");
                String link=JO.getString("link");

                String dtime=JO.getString("creation_date");
                String tags=JO.getString("tags");



                Questions question=new Questions(id,title,link,dtime,own,pic,tags);
                //eventsHelper.addEvent(event);
                questions.add(question);

                count++;
            }
            display();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
