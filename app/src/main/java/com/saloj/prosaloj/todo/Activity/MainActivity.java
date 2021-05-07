package com.saloj.prosaloj.todo.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.saloj.prosaloj.todo.API.APIRequestData;
import com.saloj.prosaloj.todo.API.RetroServer;
import com.saloj.prosaloj.todo.Adapter.MyListener;
import com.saloj.prosaloj.todo.Adapter.TaskAdapter;
import com.saloj.prosaloj.todo.Common.AppPreference;
import com.saloj.prosaloj.todo.Model.DataModel;
import com.saloj.prosaloj.todo.Model.ResponseModel;
import com.saloj.prosaloj.todo.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PickiTCallbacks {

    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView name_tv, mob_tv;
    private Bitmap bitmap;
    private FloatingActionButton fb_btn;
    private Context ctx = MainActivity.this;
    String strTask, strName, strUsername;
    //int taskid;
    private RecyclerView rvData;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> dataList = new ArrayList<>();

    //    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeData;

    private AppPreference appPre;

    private MyListener mi;
    private String encoadeImage;

    private static final int PERMISSION_REQUEST_CODE = 111;

    private static final int CAMERA_PICTURE = 1;
    private static final int GALLERY_PICTURE = 2;

    File fileImageCam1, filePath;
    String file1Str;
    Uri fileUri;
    PickiT pickiT;
     CircleImageView edtprofile;
    BottomSheetDialog bottomSheetDialogCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickiT = new PickiT(MainActivity.this, this, MainActivity.this);

        appPre = new AppPreference(ctx);

        name_tv = findViewById(R.id.tv_name);
        mob_tv = findViewById(R.id.tv_mob);
        edtprofile = findViewById(R.id.iv_user);
        fb_btn = findViewById(R.id.btn_fb);
        swipeData = findViewById(R.id.swp_data);

        edtprofile.setImageResource(R.drawable.ic_user);

        edtprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraBottomSheet();
            }
        });
        name_tv.setText(appPre.getUsename());
        mob_tv.setText(appPre.getMobileNumber());

        rvData = findViewById(R.id.recy_taskdata);
        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        downloadImage();

        swipeData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeData.setRefreshing(true);
                retrieveTaskData();
                downloadImage();
                swipeData.setRefreshing(false);

            }
        });

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                View promptsView = getLayoutInflater().inflate(R.layout.task_add_prompt, null);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.edt_task);
                Button okBtn = (Button) promptsView.findViewById(R.id.btnOk);
                ImageView cancelBtn = (ImageView) promptsView.findViewById(R.id.btnCancel);

                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                // set dialog message
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get user input and set it to result
                        strTask = userInput.getText().toString();

                        addTask();
                       /* list.add(0,name);
                        //Collections.reverse(list);
                        // Collections.sort(list,Collections.reverseOrder());
                        //  Collections.sort(list);
                        listItem.setAdapter(adapter);
                        adapter.notifyDataSetChanged();*/
                        alertDialog.dismiss();

                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                // set dialog in bottom
                Window dialogWindow = alertDialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                // show it
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }


    private void openCameraBottomSheet() {

        bottomSheetDialogCamera = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(this)
                .inflate(R.layout.camera_layout,
                        (ConstraintLayout) findViewById(R.id.profile_action));
        bottomSheetView.setOnClickListener(null);

        ImageView camera = (ImageView) bottomSheetView.findViewById(R.id.cam);
        ImageView gallery = (ImageView) bottomSheetView.findViewById(R.id.gal);

        camera.setOnClickListener(v -> {
            picCameraImage();
            bottomSheetDialogCamera.cancel();
        });

        gallery.setOnClickListener(v -> {
            picGalleryImage();
            bottomSheetDialogCamera.cancel();
        });

        bottomSheetDialogCamera.setContentView(bottomSheetView);
        bottomSheetDialogCamera.show();
    }

    private void downloadImage() {

         APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
         Call<ResponseModel> retImage = ardData.ardDownloadImage(Integer.parseInt(appPre.getUserID()));
         retImage.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();

                Log.d(TAG, "response  downloadImage :" + message);
                if (response.isSuccessful() && success == 1) {

                    dataList = response.body().getData();

                    Log.d(TAG, "response  :" + dataList);
                    for (int i = 0; i < dataList.size(); i++) {

                        String id = String.valueOf(dataList.get(i).getId());
                        String userid = String.valueOf(dataList.get(i).getUserid());
                        String image_location = dataList.get(i).getImage_location();

                        Log.d(TAG, "response img :" + image_location);
                       // profile_images/image_userid_1.jpg
                        Picasso.get().load("http://todo.orgfree.com/Todo/"+image_location).into(edtprofile);

                    }

                    // adapter = new TaskAdapter(ctx, dataList,mi);
                    //rvData.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                } else if (response.isSuccessful() && success == 0) {
                    Log.d(TAG, "response retrievefailed :" + message);
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Toast.makeText(ctx, "Please Check Internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTask() {
        final APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> addData = ardData.ardTasskAddData(Integer.parseInt(appPre.getUserID()), strTask);
        addData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();
                Log.d(TAG, "response  addTask :" + message);
                if (response.isSuccessful() && success == 1) {
                    Toast.makeText(ctx, " Task Added" + message, Toast.LENGTH_SHORT).show();
                    retrieveTaskData();
                } else if (response.isSuccessful() && success == 0) {
                    Log.e("response", "addfailed :" + message);
                    Toast.makeText(ctx, "addTask :" + message, Toast.LENGTH_SHORT).show();

                }
                //   Toast.makeText(AddActivity.this, "Success"+ success +" | Message :" +message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Please Check Internet." + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTaskData();
    }

    private void retrieveTaskData() {
//        progressBar.setVisibility(View.VISIBLE);
        final APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);

        Call<ResponseModel> retrData = ardData.ardRetrieveTaskData(Integer.parseInt(appPre.getUserID()));

        retrData.enqueue(new Callback<ResponseModel>() {

            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();
                Log.d(TAG, "onResponse retrieveTaskData: "+message);
                if (response.isSuccessful() && success == 1) {
                    dataList = response.body().getData();

                    for (int i = 0; i < dataList.size(); i++) {

                        String taskid = String.valueOf(dataList.get(i).getTaskid());
                        String userid = String.valueOf(dataList.get(i).getUserid());
                        String task = dataList.get(i).getTask();
                        String created_at = dataList.get(i).getCreated_at();


                        //    Log.e("response ", "List 1: " + taskid + "2 : " + userid + "3 :" + task + "4 :" + created_at);
                        Log.e("response", "success :" + message);
                    }

                    adapter = new TaskAdapter(ctx, dataList, mi);
                    rvData.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (response.isSuccessful() && success == 0) {
                    Log.d(TAG, "response retrievefailed :" + message);
                    Toast.makeText(ctx, "RetrieveTask :" + message, Toast.LENGTH_SHORT).show();

                }
                //progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Please Check Internet.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "response :" + t.getMessage());
                // progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                appPre.setStatus("0");
                //    appPre.setMobileNumber("");
                startActivity(new Intent(ctx, LoginActivity.class));
                finish();
                return true;
            case R.id.action_edit:

                userProfile();

                return true;

            case R.id.action_refresh:
                swipeData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeData.setRefreshing(true);
                        retrieveTaskData();
                        downloadImage();
                        swipeData.setRefreshing(false);

                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void userProfile() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ctx);
        // get item_add_prompt.xml view
        View promptsView = getLayoutInflater().inflate(R.layout.user_update_prompt, null);

        final EditText name_edtp = (EditText) promptsView
                .findViewById(R.id.edt_name);
        final EditText username_edtp = (EditText) promptsView
                .findViewById(R.id.edt_username);
        Button okBtn = (Button) promptsView.findViewById(R.id.btnOk);
        ImageView cancelBtn = (ImageView) promptsView.findViewById(R.id.btnCancel);

        // set item_add_prompt.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        // set dialog message
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user input and set it to result
                strName = name_edtp.getText().toString();
                strUsername = username_edtp.getText().toString();

                userData();

                alertDialog.dismiss();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // set dialog in bottom
        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        // show it
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void userData() {

        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> userUpdData = ardData.ardUserUpdateData(Integer.parseInt(appPre.getUserID()), strName, strUsername);
        userUpdData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();

                Log.d(TAG, "onResponse userData:"+message);
                if (response.isSuccessful() && success == 1) {

                    appPre.setMobileNumber(strName);
                    appPre.setUsername(strUsername);

                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                } else if (response.isSuccessful() && success == 0) {
                    Log.e(TAG, "response updatefailed :" + message);
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                }
                //  Toast.makeText(ctx, "DeleteTask :"+message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Please Check Internet.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void picCameraImage() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(Manifest.permission.CAMERA)) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PICTURE);
            } else {
                requestPermission(Manifest.permission.CAMERA);
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_PICTURE);
        }
    }

    private void picGalleryImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, GALLERY_PICTURE);

            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        } else {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, GALLERY_PICTURE);
        }
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    picCameraImage();
                    picGalleryImage();

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && requestCode == CAMERA_PICTURE) {
                        if (data == null) {
                            return;
                        }
                        fileUri= data.getData();
                        Bitmap thumb = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        File destination = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

                        if (destination.exists()) {
                            destination.delete();
                        }
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(destination);
                            out.write(bytes.toByteArray());
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (destination != null) {

                            fileImageCam1 = new File(destination.getPath());

                        }

                        edtprofile.setImageBitmap(thumb);
                        file1Str = destination.getPath();
                        UploadTask uploadTask = new UploadTask();
                        uploadTask.execute(new String[]{file1Str});
                    }
                    break;

                case 2:
                    if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {

                        if (data == null) {
                            return;
                        }
                        fileUri= data.getData();

                        pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                    }
                    break;

            }
        }
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("true")) {

                Toast.makeText(MainActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please Upload File", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            if (uploadFile(strings[0])) {

                return "true";
            } else {
                return "failed";
            }

        }
    }

    private boolean uploadFile(String path1) {
        try {
            filePath = new File(path1);

            Log.d("YYYYYYY", "uploadFile1: "+filePath);
            APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);

            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), filePath);
            Log.d("YYYYYYY", "uploadFile2: "+requestFile);
            MultipartBody.Part fileToUpload  = MultipartBody.Part.createFormData("image_location",filePath.getName(),requestFile);

            Log.d("YYYYYYY", "uploadFile3: "+fileToUpload);

            Log.d("MMMMMMMMMMM", "uploadFile: "+Integer.parseInt(appPre.getUserID())+"file "+fileToUpload);
            Call<ResponseModel> addImage = ardData.ardUploadImage(Integer.parseInt(appPre.getUserID()),fileToUpload);
            addImage.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    Log.d(TAG, "onResponse: uploadFile"+message);

                    if (response.isSuccessful() && success == 1) {

                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                    } else if (response.isSuccessful() && success == 0) {

                        Log.e(TAG, "uploadfailed :" + message);

                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    t.printStackTrace();
                    Toast.makeText(ctx, "Please Check Internet.", Toast.LENGTH_SHORT).show();

                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void PickiTonUriReturned() {
    }

    @Override
    public void PickiTonStartListener() {
    }

    @Override
    public void PickiTonProgressUpdate(int progress) {
    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        edtprofile.setImageBitmap(bitmap);
        file1Str = path;
        Log.d(TAG, "onActivityResult: gal" + file1Str);
        UploadTask uploadTask = new UploadTask();
        uploadTask.execute(new String[]{file1Str});

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pickiT.deleteTemporaryFile(this);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            pickiT.deleteTemporaryFile(this);
        }
    }


}
