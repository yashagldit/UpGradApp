package com.upgrad.internshipapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class QuestionsFragmentOffline extends Fragment {

    TagsHelper tagsHelper;
    QuestionsAdapter questionsAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Questions> questions;

    private OnFragmentInteractionListener mListener;

    public QuestionsFragmentOffline() {
        // Required empty public constructor
    }


    public static QuestionsFragmentOffline newInstance(String tags1, String type1) {
        QuestionsFragmentOffline fragment = new QuestionsFragmentOffline();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        //tags=sharedPreferenceUtils1.getStringValue("tag","");
       // Toast.makeText(getContext(),"Tag - "+tags,Toast.LENGTH_SHORT).show();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                display();


            }
        });


        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                display();


            }
        });
        display();
    }


    public void display(){
        //events=eventsHelper.getAllEvents();

        QuesHelper quesHelper=new QuesHelper(getContext());
        questions=quesHelper.getAllQues();
        questionsAdapter=new QuestionsAdapter(questions,getActivity(),recyclerView);
        recyclerView.setAdapter(questionsAdapter);
        refreshLayout.setRefreshing(false);
    }

}
