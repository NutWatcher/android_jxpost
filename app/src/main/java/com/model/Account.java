package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Account implements Parcelable {
    private String customerName = ""; //开户姓名
    private String extUserId = "";   //账号
    private String balance = "";   //余额
    private String baseBalance = "";   //日均余额
    private String type = "";    //账户类型（活期，定期）
    private String clearFlag = "";  //清户标志
    private String openDt = "";

    public Account() {

    }

    public void setData(JSONObject jsonObject) throws JSONException {
        this.customerName = jsonObject.getString("customerName") != null ? jsonObject.getString("customerName") : "";
        this.extUserId = jsonObject.getString("extUserId") != null ? jsonObject.getString("extUserId") : "";
        this.balance = jsonObject.getString("balance") != null ? jsonObject.getString("balance") : "";
        this.baseBalance = jsonObject.getString("baseBalance") != null ? jsonObject.getString("baseBalance") : "";
        this.openDt = jsonObject.getString("openDt") != null ? jsonObject.getString("openDt") : "";
        this.clearFlag = jsonObject.getString("clearFlag") != null ? jsonObject.getString("clearFlag") : "";
        this.type = jsonObject.getString("type") != null ? jsonObject.getString("type") : "";
        /*String type_t = jsonObject.getString("type");
        switch (type_t) {
            case "1":
                this.type = "活期";
                break;
            case "2":
                this.type = "定期";
                break;
            default:
                this.type = "未知";
                break;
        }*/
    }

    public String getOpenDt() {
        return openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getExtUserId() {
        return extUserId;
    }

    public void setExtUserId(String extUserId) {
        this.extUserId = extUserId;
    }

    public String getBaseBalance() {
        return baseBalance;
    }

    public void setBaseBalance(String baseBalance) {
        this.baseBalance = baseBalance;
    }

    public String getClearFlag() {
        return clearFlag;
    }

    public void setClearFlag(String clearFlag) {
        this.clearFlag = clearFlag;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerName);
        dest.writeString(extUserId);
        dest.writeString(balance);
        dest.writeString(baseBalance);
        dest.writeString(type);
        dest.writeString(openDt);
        dest.writeString(clearFlag);
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    private Account(Parcel in) {
        customerName = in.readString(); //开户姓名
        extUserId = in.readString();   //账号
        balance = in.readString();   //余额
        baseBalance = in.readString();   //rijun余额
        type = in.readString();    //账户类型（活期，定期）
        openDt = in.readString();
        clearFlag = in.readString();  //清户标志
    }

}
