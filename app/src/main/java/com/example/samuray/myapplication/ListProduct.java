package com.example.samuray.myapplication;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samuray.myapplication.model.ProductModel;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListProduct extends Fragment{


    private ArrayList<Integer> idProduct = new ArrayList<>();
    private ArrayList<String> titleProduct = new ArrayList<>();
    private ArrayList<String> descProduct = new ArrayList<>();
    private ArrayList<Integer> priceProduct = new ArrayList<>();
    private ArrayList<String> slugProduct = new ArrayList<>();
    private ArrayList<String> imageProduct = new ArrayList<>();

    private RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_list_product, container, false);


        recyclerView = rootView.findViewById(R.id.recycler_list_product);


        return rootView;


    }


    private void showListProduct() {

        final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Upload...");
        mProgressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi= retrofit.create(PostApi.class);



        Call<List<ProductModel>> call = postApi.getProductList();

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if(response.isSuccessful()){

                    if (response.body() != null) {


                        List<ProductModel> productList = response.body();

                        for(ProductModel productItem:productList){

                            Integer id_product = productItem.getId();
                            idProduct.add(id_product);

                            String str_title = productItem.getTitle();
                            titleProduct.add(str_title);

                            String str_desc = productItem.getDesc();
                            descProduct.add(str_desc);


                            Integer price_product = productItem.getPrice();
                            priceProduct.add(price_product);

                            String str_slug = productItem.getSlug();
                            slugProduct.add(str_slug);

                            String str_image = productItem.getImage();
                            imageProduct.add(str_image);




                        }

                        initRecyclerView();
                    }

                }else {
                    Log.d("fail", "fail");
                }

            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Log.d("fail", t.getMessage() == null ? "" : t.getMessage());
            }

        });

    }


    private void initRecyclerView(){
        RecyclerListProduct adapter = new RecyclerListProduct(getActivity(), idProduct, titleProduct, descProduct, priceProduct, slugProduct, imageProduct);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void updateGruzList() {
        idProduct.clear();
        titleProduct.clear();
        descProduct.clear();
        priceProduct.clear();
        slugProduct.clear();
        imageProduct.clear();

        RecyclerListProduct adapter = new RecyclerListProduct(getActivity(), idProduct, titleProduct, descProduct, priceProduct, slugProduct, imageProduct);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        if ( InternetUtil.isInternetOnline(getActivity()) ){
            showListProduct();
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        updateGruzList();

    }










}