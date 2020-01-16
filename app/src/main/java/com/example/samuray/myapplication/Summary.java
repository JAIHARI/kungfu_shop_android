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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuray.myapplication.HelpFuncFile.SharedDataGetSet;
import com.example.samuray.myapplication.model.ProductItemModel;
import com.example.samuray.myapplication.model.ProductModel;
import com.example.samuray.myapplication.model.SummaryModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Summary extends Fragment implements View.OnClickListener {


    private ArrayList<Integer> idProduct = new ArrayList<>();
    private ArrayList<String> titleProduct = new ArrayList<>();
    private ArrayList<String> descProduct = new ArrayList<>();
    private ArrayList<Integer> priceProduct = new ArrayList<>();
    private ArrayList<String> imageProduct = new ArrayList<>();

    private RecyclerView recyclerView;

    TextView EdTotal;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.summary, container, false);


        recyclerView = rootView.findViewById(R.id.recycler_list_product);

        Button ClearBtn = (Button) rootView.findViewById(R.id.clear_btn);

        EdTotal =  (TextView) rootView.findViewById(R.id.lt_summary_total);

        ClearBtn.setOnClickListener(this);

        return rootView;


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_btn:
                ClearButtonClick();
                break;
        }
    }


    public void ClearButtonClick()
    {
        GetClear();
        updateSummaryList();

    }

    private void GetClear()
    {

        final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Upload...");
        mProgressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        PostApi postApi= retrofit.create(PostApi.class);

        String token_ex_ap = SharedDataGetSet.getMySavedToken(getActivity());

        Call<ResponseBody> call = postApi.getClearCart(token_ex_ap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if(response.isSuccessful()){

                    if (response.body() != null) {

                        EdTotal.setText("");

                        String message = "Clear";

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    }

                }else {

                    try {
                        Integer responce_code = response.code();
                        String err_message = responce_code.toString()+ "_" + response.errorBody().string();
                        Toast.makeText(getContext(), err_message , Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Log.d("fail", "fail");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fail", t.getMessage() == null ? "" : t.getMessage());

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

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

        String token_ex_ap = SharedDataGetSet.getMySavedToken(getActivity());

        Call<SummaryModel> call = postApi.getSummaryList(token_ex_ap);

        call.enqueue(new Callback<SummaryModel>() {
            @Override
            public void onResponse(Call<SummaryModel> call, Response<SummaryModel> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if(response.isSuccessful()){

                    if (response.body() != null) {


                        SummaryModel summary_res = response.body();

                        List <ProductItemModel> productCart = summary_res.getCart();

                        Integer int_total = summary_res.getTotal();
                        String str_total_for_view = int_total.toString() + "$";
                        EdTotal.setText(str_total_for_view);


                        for(ProductItemModel productItem:productCart){

                            ProductModel product = productItem.getProduct();


                            Integer id_product = product.getId();
                            idProduct.add(id_product);

                            String str_title = product.getTitle();
                            titleProduct.add(str_title);

                            String str_desc = product.getDesc();
                            descProduct.add(str_desc);


                            Integer price_product = product.getPrice();
                            priceProduct.add(price_product);



                        }

                        initRecyclerView();
                    }

                }else {
                    Log.d("fail", "fail");
                }

            }

            @Override
            public void onFailure(Call<SummaryModel> call, Throwable t) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Log.d("fail", t.getMessage() == null ? "" : t.getMessage());
            }

        });

    }


    private void initRecyclerView(){
        RecyclerSummary adapter = new RecyclerSummary(getActivity(), idProduct, titleProduct, descProduct, priceProduct, descProduct);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void updateSummaryList() {
        idProduct.clear();
        titleProduct.clear();
        descProduct.clear();
        priceProduct.clear();
        imageProduct.clear();


        RecyclerSummary adapter = new RecyclerSummary(getActivity(), idProduct, titleProduct, descProduct, priceProduct, descProduct);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        if ( InternetUtil.isInternetOnline(getActivity()) ){
            showListProduct();
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        updateSummaryList();

    }










}