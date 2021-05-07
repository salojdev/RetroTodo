package com.saloj.prosaloj.todo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.saloj.prosaloj.todo.API.APIRequestData;
import com.saloj.prosaloj.todo.API.RetroServer;
import com.saloj.prosaloj.todo.Common.AppPreference;
import com.saloj.prosaloj.todo.Model.DataModel;
import com.saloj.prosaloj.todo.Model.ResponseModel;
import com.saloj.prosaloj.todo.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText name_edt,uname_edt,mobile_edt,pwd_edt,confpwd_edt;
    private Button signup_btn;
    private String str_name,str_uname,str_mobile,str_pwd,str_cpwd;
    private ProgressDialog progressDialog;
    private Context ctx = RegisterActivity.this;

    private AppPreference appPre;
  private List<DataModel> dataModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        appPre = new AppPreference(ctx);
        progressDialog = new ProgressDialog(ctx);

        name_edt = findViewById(R.id.edt_name);
        uname_edt = findViewById(R.id.edt_uname);
        mobile_edt = findViewById(R.id.edt_mobile);
        pwd_edt = findViewById(R.id.edt_pwd);
        confpwd_edt = findViewById(R.id.edt_cpwd);

        signup_btn = findViewById(R.id.btn_signup);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name = name_edt.getText().toString();
                str_uname = uname_edt.getText().toString();
                str_mobile = mobile_edt.getText().toString();
                str_pwd = pwd_edt.getText().toString();
                str_cpwd = confpwd_edt.getText().toString();

                progressDialog.setTitle("Signup");
                progressDialog.setMessage("Please wait...");
                progressDialog.setIcon(R.drawable.person);
                progressDialog.setCancelable(true);
                progressDialog.show();

                userRegister();
             //   startActivity(new Intent(ctx,MainActivity.class));
            }
        });

    }

    private void userRegister() {

        final APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> registerData = ardData.ardUserRegData(str_name,str_uname,str_pwd,str_mobile);
        registerData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();

                if (response.isSuccessful() && success == 1){

                    progressDialog.dismiss();

                    dataModel = response.body().getData();

                    for(int i=0;i<dataModel.size();i++){

                        String userid = String.valueOf(dataModel.get(i).getUserid());
                        String username =  dataModel.get(i).getUsername();

                        appPre.setStatus("1");
                        appPre.setUserID(userid);
                        appPre.setUsername(username);
                        appPre.setMobileNumber(str_mobile);

                        Log.e("response","userid :"+ dataModel.get(i).getUserid()); // like this you can access other values in the items list

                        Toast.makeText(ctx, "userid :"+ dataModel.get(i).getUserid(), Toast.LENGTH_SHORT).show();

                    }
                    Log.e("response", new Gson().toJson(response.body()));
                  //  Toast.makeText(ctx, "Login :"+ message +"response", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ctx,MainActivity.class));
                    finish();


                }else if (response.isSuccessful() &&  success == 0){

                    Log.e("response", new Gson().toJson(response.body()));
                    Toast.makeText(ctx, "Register :" + message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Retro Server not Respond.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}