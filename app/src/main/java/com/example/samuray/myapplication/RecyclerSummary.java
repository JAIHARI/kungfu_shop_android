package com.example.samuray.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerSummary extends RecyclerView.Adapter<RecyclerSummary.ViewHolder>{

    private ArrayList<Integer> mIdProduct = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    private ArrayList<Integer> mPrice = new ArrayList<>();
    private ArrayList<String> mSlug = new ArrayList<>();



    private Context mContext;


    public RecyclerSummary(Context context, ArrayList<Integer> idShowPost, ArrayList<String> Title, ArrayList<String> Desc, ArrayList<Integer> Price, ArrayList<String> Slug) {
        mIdProduct = idShowPost;
        mTitle = Title;
        mDesc = Desc;
        mPrice = Price;
        mSlug = Slug;

        mContext = context;
    }

    @Override
    public RecyclerSummary.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_product, parent, false);
        RecyclerSummary.ViewHolder holder = new RecyclerSummary.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerSummary.ViewHolder holder, final int position) {

        holder.TTitle.setText(String.valueOf(mTitle.get(position)));
        holder.TDesc.setText(String.valueOf(mDesc.get(position)));

        Integer int_price = mPrice.get(position);
        String str_price  = int_price.toString() + "$";
        holder.TPrice.setText(str_price);




        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("key_slug",mSlug.get(position));
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new DetailProduct();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, myFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mIdProduct.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView TTitle;
        TextView TDesc;
        TextView TPrice;


        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            TTitle = itemView.findViewById(R.id.title);
            TDesc = itemView.findViewById(R.id.desc);
            TPrice = itemView.findViewById(R.id.price);


            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


}