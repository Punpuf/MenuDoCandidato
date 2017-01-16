package com.punpuf.uem_menudocandidato.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class ExamLocation implements Parcelable {

    public final String examLocationHeadquarters;
    public final String examLocationCity;
    public final String examLocationStreet;
    public final String examLocationBlock;
    public final String examLocationRoom;
    public final String examLocationMapUrl;

    public ExamLocation(Cursor cursor){
        cursor.moveToFirst();
        examLocationHeadquarters = cursor.getString(Contract.ExamLocationEntry.POSITION_HEADQUARTERS);
        examLocationCity = cursor.getString(Contract.ExamLocationEntry.POSITION_CITY);
        examLocationStreet = cursor.getString(Contract.ExamLocationEntry.POSITION_STREET);
        examLocationBlock = cursor.getString(Contract.ExamLocationEntry.POSITION_BLOCK);
        examLocationRoom = cursor.getString(Contract.ExamLocationEntry.POSITION_ROOM);
        examLocationMapUrl = cursor.getString(Contract.ExamLocationEntry.POSITION_MAP_URL);
        cursor.close();
    }

    public ExamLocation(JSONObject jsonObject) throws JSONException {
        examLocationHeadquarters = Utils.getJsonString(jsonObject, "nm_sede_ext");
        examLocationCity = Utils.getJsonString(jsonObject, "en_cidade");
        examLocationStreet = Utils.getJsonString(jsonObject, "en_logradouro");
        examLocationBlock = Utils.getJsonString(jsonObject, "nm_bloco");
        examLocationRoom = Utils.getJsonString(jsonObject, "nu_sala");
        examLocationMapUrl = Utils.getJsonString(jsonObject, "en_url_mapa");
    }

    private ExamLocation(Parcel in) {
        examLocationHeadquarters = in.readString();
        examLocationCity = in.readString();
        examLocationStreet = in.readString();
        examLocationBlock = in.readString();
        examLocationRoom = in.readString();
        examLocationMapUrl = in.readString();
    }

    public static final Creator<ExamLocation> CREATOR = new Creator<ExamLocation>() {
        @Override
        public ExamLocation createFromParcel(Parcel in) {
            return new ExamLocation(in);
        }

        @Override
        public ExamLocation[] newArray(int size) {
            return new ExamLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(examLocationHeadquarters);
        parcel.writeString(examLocationCity);
        parcel.writeString(examLocationStreet);
        parcel.writeString(examLocationBlock);
        parcel.writeString(examLocationRoom);
        parcel.writeString(examLocationMapUrl);
    }
}
