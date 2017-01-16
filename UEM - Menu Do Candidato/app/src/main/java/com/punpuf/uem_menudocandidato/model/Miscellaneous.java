package com.punpuf.uem_menudocandidato.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class Miscellaneous implements Parcelable{

    public final String miscellaneousInfoAvailabilityCompetition;
    public final String miscellaneousInfoAvailabilityExamLocation;
    public final String miscellaneousInfoAvailabilityComposing;
    public final String miscellaneousInfoAvailabilityPerformance;
    public final String miscellaneousComposingGenre1;
    public final String miscellaneousComposingGenre2;
    public final String miscellaneousCdEvent;
    public final String miscellaneousCdComposing;
    public final String miscellaneousCdCourse;
    
    public Miscellaneous(Cursor cursor){
        cursor.moveToFirst();
        miscellaneousInfoAvailabilityCompetition =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_AVAILABILITY_COMPETITION);
        miscellaneousInfoAvailabilityExamLocation =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_AVAILABILITY_EXAM_LOCATION);
        miscellaneousInfoAvailabilityComposing =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_AVAILABILITY_COMPOSING);
        miscellaneousInfoAvailabilityPerformance =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_AVAILABILITY_PERFORMANCE);
        miscellaneousComposingGenre1 =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_COMPOSING_GENRE_1);
        miscellaneousComposingGenre2 =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_COMPOSING_GENRE_2);
        miscellaneousCdEvent =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_CD_EVENT);
        miscellaneousCdComposing =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_CD_COMPOSING);
        miscellaneousCdCourse =
                cursor.getString(Contract.MiscellaneousEntry.POSITION_CD_COURSE);
        cursor.close();
    }

    public Miscellaneous(JSONObject jsonObject) throws JSONException {
        miscellaneousInfoAvailabilityCompetition = Utils.getJsonString(jsonObject, "btfc");
        miscellaneousInfoAvailabilityExamLocation = Utils.getJsonString(jsonObject, "btlp");
        miscellaneousInfoAvailabilityComposing = Utils.getJsonString(jsonObject, "btred");
        miscellaneousInfoAvailabilityPerformance = Utils.getJsonString(jsonObject, "btdp");
        miscellaneousComposingGenre1 = Utils.getJsonString(jsonObject, "genero1");
        miscellaneousComposingGenre2 = Utils.getJsonString(jsonObject, "genero2");
        miscellaneousCdEvent = Utils.getJsonString(jsonObject, "cd_evento");
        miscellaneousCdComposing = Utils.getJsonString(jsonObject, "cd_discursiva");
        miscellaneousCdCourse = Utils.getJsonString(jsonObject, "cd_curso");
    }


    private Miscellaneous(Parcel in) {
        miscellaneousInfoAvailabilityCompetition = in.readString();
        miscellaneousInfoAvailabilityExamLocation = in.readString();
        miscellaneousInfoAvailabilityComposing = in.readString();
        miscellaneousInfoAvailabilityPerformance = in.readString();
        miscellaneousComposingGenre1 = in.readString();
        miscellaneousComposingGenre2 = in.readString();
        miscellaneousCdEvent = in.readString();
        miscellaneousCdComposing = in.readString();
        miscellaneousCdCourse = in.readString();
    }

    public static final Creator<Miscellaneous> CREATOR = new Creator<Miscellaneous>() {
        @Override
        public Miscellaneous createFromParcel(Parcel in) {
            return new Miscellaneous(in);
        }

        @Override
        public Miscellaneous[] newArray(int size) {
            return new Miscellaneous[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(miscellaneousInfoAvailabilityCompetition);
        parcel.writeString(miscellaneousInfoAvailabilityExamLocation);
        parcel.writeString(miscellaneousInfoAvailabilityComposing);
        parcel.writeString(miscellaneousInfoAvailabilityPerformance);
        parcel.writeString(miscellaneousComposingGenre1);
        parcel.writeString(miscellaneousComposingGenre2);
        parcel.writeString(miscellaneousCdEvent);
        parcel.writeString(miscellaneousCdComposing);
        parcel.writeString(miscellaneousCdCourse);
    }
}
