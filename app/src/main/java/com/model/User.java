package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * gg
 * Created by 扬洋 on 2015/8/2.
 */
public class User implements Parcelable {
    private String name = "";
    private String department = "";
    private String userId = "";
    private String label = "";
    private String score = ""; //当前日平均
    private String expectScoer = ""; //预期余额
    private String money = ""; //还需要存入

    public User() {

    }

    public void setLogUserData(JSONObject jsonObject) throws JSONException {

        this.label = jsonObject.getString("label");
        this.userId = jsonObject.getString("value");
        this.name = this.label.split("=")[0];
        this.department = this.label.split("=")[1];
    }

    public void setData(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.department = jsonObject.getString("department");
        this.score = jsonObject.getString("score");
        this.expectScoer = jsonObject.getString("expectScoer");
        this.money = jsonObject.getString("money");
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getExpectScoer() {
        return expectScoer;
    }

    public void setExpectScoer(String expectScoer) {
        this.expectScoer = expectScoer;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(department);
        dest.writeString(userId);
        dest.writeString(label);
        dest.writeString(score);
        dest.writeString(expectScoer);
        dest.writeString(money);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        name = in.readString();
        department = in.readString();
        userId = in.readString();
        label = in.readString();
        score = in.readString();
        expectScoer = in.readString();
        money = in.readString();
    }
}

