package com.example.roadpetrol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Model>userArrayList;

    public void setFilteredList(ArrayList<Model> filteredList){
        this.userArrayList=filteredList;
        notifyDataSetChanged();
    }

    public MyAdapter(Context context, ArrayList<Model> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Model user=userArrayList.get(position);
        holder.txtdisc.setText(user.desc);
        holder.txtlocation.setText(user.address);
        holder.datetime.setText(user.datetime);
        holder.itemView.setBackgroundColor(Color.WHITE);
        Glide.with(holder.displayimage.getContext()).load(user.getImage()).into(holder.displayimage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),displayDetails.class);
                intent.putExtra("address",user.address);
                intent.putExtra("image",(user.getImage()));
                intent.putExtra("latitude",user.latitude);
                intent.putExtra("longitude",user.longitude);
                intent.putExtra("currentuserID",user.currentuserID);
                intent.putExtra("docID",user.docID);
                intent.putExtra("datetime",user.datetime);
                intent.putExtra("description",user.getDesc());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtlocation,txtdisc,datetime;
        ImageView displayimage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdisc=itemView.findViewById(R.id.txt_desc);
            displayimage=itemView.findViewById(R.id.display_image);
            txtlocation=itemView.findViewById(R.id.txt_location);
            datetime=itemView.findViewById(R.id.txt_datetime);

        }
    }
}
