package com.saloj.prosaloj.todo.API;

import com.saloj.prosaloj.todo.Model.DataModel;
import com.saloj.prosaloj.todo.Model.ResponseModel;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRequestData {

/*
    @GET("retrieve.php")
    Call<ResponseModel> ardRetrieveData();

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel> ardCreateData(
            @Field("cname") String cname,
            @Field("cemail") String cemail,
            @Field("cmobile") String cmobile
    );


*/

    @FormUrlEncoded
    @POST("task_add.php")
    Call<ResponseModel> ardTasskAddData(
            @Field("userid") int userid,
            @Field("task") String task

    );


    @FormUrlEncoded
    @POST("task_retrieve.php")
    Call<ResponseModel> ardRetrieveTaskData(@Field("userid") int userid);


    @FormUrlEncoded
    @POST("task_delete.php")
    Call<ResponseModel> ardDeleteData(
            @Field("taskid") int taskid
    );

    @FormUrlEncoded
    @POST("task_update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("taskid") int taskid,
            @Field("task") String task
    );

    @FormUrlEncoded
    @POST("user_login.php")
    Call<ResponseModel> ardUserLoginData( @Field("username") String username,
                                          @Field("password") String password);

    @FormUrlEncoded
    @POST("user_register.php")
    Call<ResponseModel> ardUserRegData(
                                        @Field("name") String name,
                                        @Field("username") String username,
                                        @Field("password") String password,
                                        @Field("mobileno") String mobileno
                                        );

    @FormUrlEncoded
    @POST("user_update.php")
    Call<ResponseModel> ardUserUpdateData( @Field("userid") int userid,
                                           @Field("name") String name,
                                           @Field("username") String username
                                                        );
    @Multipart
    @POST("upload_image.php")
    Call<ResponseModel> ardUploadImage(
            @Query("userid") int userid,
            @Part MultipartBody.Part image_location
    );

    @GET("download_image.php")
    Call<ResponseModel> ardDownloadImage( @Query("userid") int userid );
}
