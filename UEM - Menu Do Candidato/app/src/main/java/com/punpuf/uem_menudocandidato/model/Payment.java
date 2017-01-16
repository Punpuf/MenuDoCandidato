package com.punpuf.uem_menudocandidato.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment implements Parcelable{

    public final String paymentStatus;
    public final String paymentDate;

    public Payment(Cursor cursor){
        cursor.moveToFirst();
        paymentStatus = cursor.getString(Contract.PaymentEntry.POSITION_STATUS);
        paymentDate = cursor.getString(Contract.PaymentEntry.POSITION_DATE);
        cursor.close();
    }

    public Payment(JSONObject jsonObject) throws JSONException {
        paymentStatus = Utils.getJsonString(jsonObject, "st_pagamento");
        paymentDate = Utils.getJsonString(jsonObject, "dt_pagamento");
    }

    private Payment(Parcel in) {
        paymentStatus = in.readString();
        paymentDate = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(paymentStatus);
        parcel.writeString(paymentDate);
    }
}
