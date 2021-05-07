package com.saloj.prosaloj.todo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saloj.prosaloj.todo.Common.AppPreference;
import com.saloj.prosaloj.todo.Common.NetworkDetector;
import com.saloj.prosaloj.todo.R;

public class SplashActivity extends AppCompatActivity {
    ImageView img;
    ProgressBar progressBar;

    Context ctx = SplashActivity.this;
    NetworkDetector networkDet;
    Animation anim1,anim2,anim3;
    AppPreference appPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        networkDet = new NetworkDetector();
        appPref = new AppPreference( SplashActivity.this );
      //  networkDet.isConnectingToInternet(SplashActivity.this);
        progressBar = new ProgressBar(SplashActivity.this);

        img = findViewById(R.id.imgSpl);
        progressBar = findViewById(R.id.pb);
        progressBar.setMax(50);
        progressBar.setProgress(20);
        progressBar.setProgress(0); // <--
        progressBar.setMax(20);
        progressBar.setProgress(20);
        progressBar.setVisibility(View.VISIBLE);
        anim1 = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        img.setVisibility(View.VISIBLE);
        img.startAnimation(anim1);


        {
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                netFound();
                progressBar.setVisibility(View.GONE);
            }

        }, 3000 );

    }

    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(SplashActivity.this);

        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        "Retry",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                networkDet.isConnectingToInternet(SplashActivity.this);
                                dialog.cancel();
                            }
                        });

        builder
                .setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                dialog.cancel();
                                finish();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void netFound() {

        if(appPref.getStatus()!=null && appPref.getStatus().equals( "1" ) ){

                    Intent registerIntent = new Intent( SplashActivity.this, MainActivity.class );
                    startActivity( registerIntent );
                    finish();

        }
        else{
            Intent registerIntent = new Intent( SplashActivity.this, LoginActivity.class );
            startActivity( registerIntent );
            finish();
        }
    }

}