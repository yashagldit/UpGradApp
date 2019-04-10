package com.upgrad.internshipapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yash Agarwal on 25-05-2018.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private ArrayList<Questions> tags;
    private Context context;
    private int mExpandedPosition= -1;
    private RecyclerView recyclerView;
    TagsHelper tagsHelper;
    ArrayList<String> sel;


    public QuestionsAdapter(ArrayList<Questions> tags, Context context, RecyclerView recyclerView) {
        this.tags = tags;
        this.recyclerView=recyclerView;
        this.context=context;
        tagsHelper=new TagsHelper(context);
        sel=new ArrayList<>();
    }

    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quesitem,parent,false);
        return new QuestionsAdapter.ViewHolder(v);
    }
    QuestionsAdapter.ViewHolder viewHolder;
    @Override
    public void onBindViewHolder(final QuestionsAdapter.ViewHolder holder, final int position) {
        viewHolder = holder;
        final Questions ques = tags.get(position);

        holder.title.setText(ques.getTitle());
        holder.own.setText(ques.getOname());
        holder.tags.setText(ques.getTags());



    }


    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title,own,tags,dat;
        public ImageView oimg;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tags=(TextView)itemView.findViewById(R.id.qtags);
            dat=(TextView)itemView.findViewById(R.id.qdate);
            own=(TextView)itemView.findViewById(R.id.oname);
            title=(TextView)itemView.findViewById(R.id.quesTitle);
            oimg=(ImageView) itemView.findViewById(R.id.opic);

            layout=(LinearLayout) itemView.findViewById(R.id.tag_lay);


        }
    }
    public int getSelCount(){
        return sel.size();
    }

}
