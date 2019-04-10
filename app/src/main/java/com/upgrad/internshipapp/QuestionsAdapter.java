package com.upgrad.internshipapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Yash Agarwal on 25-05-2018.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private ArrayList<Questions> tags;
    private Context context;
    private int mExpandedPosition= -1;
    private RecyclerView recyclerView;
    ArrayList<String> sel;
    QuesHelper quesHelper;


    public QuestionsAdapter(ArrayList<Questions> tags, Context context, RecyclerView recyclerView) {
        this.tags = tags;
        this.recyclerView=recyclerView;
        this.context=context;
        sel=new ArrayList<>();
        quesHelper=new QuesHelper(context);
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
        holder.dat.setText(getConvertedTime(Long.parseLong(ques.getDat()+"000")));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Questions questions=tags.get(position);
                String url=questions.getLink();
                customtab(context,url);
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                quesHelper.addTag(ques);
                Toast.makeText(context,"Added to Offline DataBase",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        if(ques.getOimg().contains("http")){
            Picasso p = Picasso.with(context);
            p.setIndicatorsEnabled(false);
            p.load(ques.getOimg()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.stackoverflow_logo).into(holder.oimg, new Callback() {
                @Override
                public void onSuccess() {
                    //Bitmap b=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    //getDominantColor(b);
                    //Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT);
                }

                @Override
                public void onError() {
                    Picasso pi = Picasso.with(context);
                    pi.setIndicatorsEnabled(false);
                    pi.load(ques.getOimg()).placeholder(R.drawable.stackoverflow_logo).into(holder.oimg);
                }
            });
        }


    }
    void customtab(Context context,String url){

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_back));
        builder.enableUrlBarHiding();
        builder.setShowTitle(true);
        customTabsIntent.intent.setPackage("com.android.chrome");
        try {
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public String getConvertedTime(long milli) {
        return new SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.US).format(milli);
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

            layout=(LinearLayout) itemView.findViewById(R.id.quesitemlay);


        }
    }
    public int getSelCount(){
        return sel.size();
    }

}
