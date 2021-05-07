package com.saloj.prosaloj.todo.Model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Keep
public class DataModel implements Serializable {

    @SerializedName("userid")
    @Expose
    private int userid;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("mobileno")
    @Expose
    private String mobileno;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("image_location")
    @Expose
    private String image_location;

    @SerializedName("created_at")
    @Expose
    private String created_at;


    @SerializedName("task")
    @Expose
    private String task;


    @SerializedName("taskid")
    @Expose
    private int taskid;


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTask() {
        return task;
    }


    public void setTask(String task) {
        this.task = task;
    }
}
