package com.example.tanmay.elabs_attendance_geofencing;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by PRANSHOO VERMA on 31-03-2017.
 */

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    Context ctx;
    private List<cardView> albumList;
ImageView image;


    public adapter(Context ctx,List<cardView> albumList) {
        this.ctx = ctx;
        this.albumList=albumList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);

            img=(ImageView) itemView.findViewById(R.id.image_view_recycle);
            txt= (TextView) itemView.findViewById(R.id.text_recycle);
        }
    }
    @Override
    public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.recycle_cards,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final adapter.ViewHolder holder, final int position) {
        cardView card=albumList.get(position);

        image=(ImageView) holder.itemView.findViewById(R.id.image_view_recycle);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent(ctx, MapsActivity.class);
                if(position==0)
                {
                    i.putExtra("mainSubject","Android");
                    v.getContext().startActivity(i);
                }
                if(position==1)
                {
                    i.putExtra("mainSubject","MATLAB");
                    v.getContext().startActivity(i);
                }
                if(position==2)
                {
                    i.putExtra("mainSubject","Embedded");
                    v.getContext().startActivity(i);
                }
                if(position==3)
                {
                    i.putExtra("mainSubject","Communication");
                    v.getContext().startActivity(i);
                }
            }
        });
        Glide.with(ctx).load(card.getThumbnail()).into(holder.img);
        holder.txt.setText(card.getName());

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
