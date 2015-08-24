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
    private String departmentName;
    private String departmentId;
    private String finish;
    private String rank;

    public Department() {

    }

    public void setData(JSONObject jsonObject) throws JSONException {
        this.departmentName = jsonObject.getString("departmentName") != null ? jsonObject.getString("departmentName") : "";
        this.departmentId = jsonObject.getString("departmentId") != null ? jsonObject.getString("departmentId") : "";
        this.finish = jsonObject.getString("finish") != null ? jsonObject.getString("finish") : "";
    }

    public String getdepartmentName() {
        return departmentName;
    }

    public void setdepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    public String getdepartmentId() {
        return departmentId;
    }

    public void setdepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getfinish() {
        return finish;
    }

    public void setfinish(String finish) {
        this.finish = finish;
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
        dest.writeString(departmentName);
        dest.writeString(departmentId);
        dest.writeString(finish);
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
        departmentName = in.readString();
        departmentId = in.readString();
        finish = in.readString();
        rank = in.readString();
    }
}
