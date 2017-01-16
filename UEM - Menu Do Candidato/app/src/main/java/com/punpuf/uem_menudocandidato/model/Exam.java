package com.punpuf.uem_menudocandidato.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class Exam implements Parcelable{

    public final String examName;
    public final String examType;
    public final String examPhase;
    public final String examDate1;
    public final String examDate2;
    public final String examDate3;
    public final String examDiscipline1;
    public final String examDiscipline2;
    public final String examCourse;
    public final String examForeignLanguage;
    public final String examCompetition;
    
    public Exam(Cursor cursor){
        cursor.moveToFirst();
        examName = cursor.getString(Contract.ExamEntry.POSITION_NAME);
        examType = cursor.getString(Contract.ExamEntry.POSITION_TYPE);
        examPhase = cursor.getString(Contract.ExamEntry.POSITION_PHASE);
        examDate1 = cursor.getString(Contract.ExamEntry.POSITION_DATE_1);
        examDate2 = cursor.getString(Contract.ExamEntry.POSITION_DATE_2);
        examDate3 = cursor.getString(Contract.ExamEntry.POSITION_DATE_3);
        examDiscipline1 = cursor.getString(Contract.ExamEntry.POSITION_DISCIPLINE_1);
        examDiscipline2 = cursor.getString(Contract.ExamEntry.POSITION_DISCIPLINE_2);
        examCourse = cursor.getString(Contract.ExamEntry.POSITION_COURSE);
        examForeignLanguage = cursor.getString(Contract.ExamEntry.POSITION_FOREIGN_LANGUAGE);
        examCompetition = cursor.getString(Contract.ExamEntry.POSITION_COMPETITION);
        cursor.close();
    }

    public Exam(JSONObject jsonObject) throws JSONException {
        examName = Utils.getJsonString(jsonObject, "nm_evento");
        examType = Utils.getJsonString(jsonObject, "tp_evento");
        examPhase = Utils.getJsonString(jsonObject, "nu_etapa_atual");
        examDate1 = Utils.getJsonString(jsonObject, "de_aplicacao_p1");
        examDate2 = Utils.getJsonString(jsonObject, "de_aplicacao_p2");
        examDate3 = Utils.getJsonString(jsonObject, "de_aplicacao_p3");
        examDiscipline1 = Utils.getJsonString(jsonObject, "nm_disciplina_ce1");
        examDiscipline2 = Utils.getJsonString(jsonObject, "nm_disciplina_ce2");
        examCourse = Utils.getJsonString(jsonObject, "nm_curso");
        examForeignLanguage = Utils.getJsonString(jsonObject, "nu_lingua_estr");
        examCompetition = Utils.getJsonString(jsonObject, "concorrencia");
    }

    private Exam(Parcel in) {
        examName = in.readString();
        examType = in.readString();
        examPhase = in.readString();
        examDate1 = in.readString();
        examDate2 = in.readString();
        examDate3 = in.readString();
        examDiscipline1 = in.readString();
        examDiscipline2 = in.readString();
        examCourse = in.readString();
        examForeignLanguage = in.readString();
        examCompetition = in.readString();
    }

    public static final Creator<Exam> CREATOR = new Creator<Exam>() {
        @Override
        public Exam createFromParcel(Parcel in) {
            return new Exam(in);
        }

        @Override
        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(examName);
        parcel.writeString(examType);
        parcel.writeString(examPhase);
        parcel.writeString(examDate1);
        parcel.writeString(examDate2);
        parcel.writeString(examDate3);
        parcel.writeString(examDiscipline1);
        parcel.writeString(examDiscipline2);
        parcel.writeString(examCourse);
        parcel.writeString(examForeignLanguage);
        parcel.writeString(examCompetition);
    }
}
