package com.upgrad.internshipapp;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yash Agarwal on 25-05-2018.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private ArrayList<Tags> tags;
    private Context context;
    private int mExpandedPosition= -1;
    private RecyclerView recyclerView;
    TagsHelper tagsHelper;
    ArrayList<String> sel;


    public TagsAdapter(ArrayList<Tags> tags, Context context, RecyclerView recyclerView) {
        this.tags = tags;
        this.recyclerView=recyclerView;
        this.context=context;
        tagsHelper=new TagsHelper(context);
        sel=new ArrayList<>();
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tagitem,parent,false);
        return new TagsAdapter.ViewHolder(v);
    }
    TagsAdapter.ViewHolder viewHolder;
    @Override
    public void onBindViewHolder(final TagsAdapter.ViewHolder holder, final int position) {
        viewHolder = holder;
        final Tags tag = tags.get(position);

        holder.tag.setText(tag.getName());

        if(sel.contains(tag.getName())){
            holder.tag.setBackgroundColor(Color.BLUE);
        }
        else{
            holder.tag.setBackgroundColor(Color.WHITE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=holder.getAdapterPosition();
                Tags tag1=tags.get(pos);
                if(sel.contains(tag1.getName())){
                    holder.tag.setBackgroundColor(Color.WHITE);
                    tagsHelper.setUnSelected(tag1);
                    sel.remove(tag.getName());
                }
                else {
                    holder.tag.setBackgroundColor(Color.BLUE);
                    tagsHelper.setSelected(tag1);
                    sel.add(tag.getName());
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tag;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tag=(TextView)itemView.findViewById(R.id.tag_text);
            layout=(LinearLayout) itemView.findViewById(R.id.tag_lay);


        }
    }
    public int getSelCount(){
        return sel.size();
    }

}
