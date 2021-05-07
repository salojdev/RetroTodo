package com.saloj.prosaloj.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.saloj.prosaloj.todo.API.APIRequestData;
import com.saloj.prosaloj.todo.API.RetroServer;
import com.saloj.prosaloj.todo.Common.AppPreference;
import com.saloj.prosaloj.todo.Model.DataModel;
import com.saloj.prosaloj.todo.Model.ResponseModel;
import com.saloj.prosaloj.todo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
private TextInputEditText uname_edt,pwd_edt;
private Button signin_btn,signup_btn;
private String str_uname,str_pwd;
private ProgressDialog progressDialog;
private Context ctx = LoginActivity.this;

  private List<DataModel> dataModel = new ArrayList<>();

  private AppPreference appPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appPre = new AppPreference(ctx);
        progressDialog = new ProgressDialog(ctx);

        if(appPre.getStatus()!=null && appPre.getStatus().equals("1")){
            startActivity(new Intent(ctx, MainActivity.class));
            finish();
        }

        uname_edt = findViewById(R.id.edt_uname);
        pwd_edt = findViewById(R.id.edt_pwd);

        signin_btn = findViewById(R.id.btn_signin);
        signup_btn = findViewById(R.id.btn_signup);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_uname = uname_edt.getText().toString();
                str_pwd = pwd_edt.getText().toString();



                if (TextUtils.isEmpty(str_uname)){
                    uname_edt.setError("please enter username");
                    uname_edt.requestFocus();
                }else if (TextUtils.isEmpty(str_pwd)){
                    pwd_edt.setError("please enter password");
                    pwd_edt.requestFocus();
                }
                else {
                    progressDialog.setTitle("Authenticate");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setIcon(R.drawable.person);
                    progressDialog.setCancelable(true);
                progressDialog.show();

checkLogin();
                }
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(ctx,RegisterActivity.class));
            }
        });
    }

    private void checkLogin() {
         APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> loginData = ardData.ardUserLoginData(str_uname,str_pwd);
        loginData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();

                int success = response.body().getSuccess();
                String message = response.body().getMessage();


                if (response.isSuccessful() && success == 1){
                    dataModel = response.body().getData();
                    for(int i=0;i<dataModel.size();i++){

                        String userid = String.valueOf(dataModel.get(i).getUserid());
                        String mobileno =  dataModel.get(i).getMobileno();
                        String username =  dataModel.get(i).getUsername();

                        appPre.setStatus("1");
                        appPre.setUserID(userid);
                        appPre.setMobileNumber(mobileno);
                        appPre.setUsername(username);

                        Log.e("response","userid :"+ dataModel.get(i).getUserid()+"\n username :" + dataModel.get(i).getName()); // like this you can access other values in the items list

                        Toast.makeText(ctx, "userid :"+ dataModel.get(i).getUserid()+"\n username :" + dataModel.get(i).getName(), Toast.LENGTH_SHORT).show();

                    }
                      Log.e("response", new Gson().toJson(response.body()));
                        Toast.makeText(ctx, "Login :"+ message +"response", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(ctx,MainActivity.class));
                        finish();


                }else if (response.isSuccessful() &&  success == 0){
                    Log.e("response", new Gson().toJson(response.body()));
                    Toast.makeText(ctx, "Login :"+message, Toast.LENGTH_SHORT).show();

                }

                //   Toast.makeText(AddActivity.this, "Success"+ success +" | Message :" +message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Retro Server not Respond.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

}