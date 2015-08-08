package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * jj
 * Created by 扬洋 on 2015/8/8.
 */
public class Department implements Parcelable {
    private String branchName;
    private String id;
    private String branchId;
    private String balance;
    private String rank;

    public Department() {

    }

    public void setData(JSONObject jsonObject) throws JSONException {
        this.branchName = jsonObject.getString("branchName") != null ? jsonObject.getString("branchName") : "";
        this.id = jsonObject.getString("id") != null ? jsonObject.getString("id") : "";
        this.branchId = jsonObject.getString("branchId") != null ? jsonObject.getString("branchId") : "";
        this.balance = jsonObject.getString("balance") != null ? jsonObject.getString("balance") : "";
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(branchName);
        dest.writeString(id);
        dest.writeString(branchId);
        dest.writeString(balance);
        dest.writeString(rank);
    }

    public static final Parcelable.Creator<Department> CREATOR = new Parcelable.Creator<Department>() {
        public Department createFromParcel(Parcel in) {
            return new Department(in);
        }

        public Department[] newArray(int size) {
            return new Department[size];
        }
    };

    public Department(Parcel in) {
        branchName = in.readString();
        id = in.readString();
        branchId = in.readString();
        balance = in.readString();
        rank = in.readString();
    }
}
