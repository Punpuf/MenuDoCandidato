package com.punpuf.uem_menudocandidato.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class Score implements Parcelable{

    public final String scoreTotalStage1;
    public final String scoreTotalStage2;
    public final String scoreTotalStageFinal;
    public final String scoreGeneralKnowledge;
    public final String scorePortuguese;
    public final String scoreForeignLanguage;
    public final String scoreComposing;
    public final String scoreDiscipline1;
    public final String scoreDiscipline2;
    public final String scoreClassification;
    public final String scoreApprovalResult;

    public Score(Cursor cursor){
        cursor.moveToFirst();
        scoreTotalStage1 = cursor.getString(Contract.ScoreEntry.POSITION_TOTAL_STAGE_1);
        scoreTotalStage2 = cursor.getString(Contract.ScoreEntry.POSITION_TOTAL_STAGE_2);
        scoreTotalStageFinal = cursor.getString(Contract.ScoreEntry.POSITION_TOTAL_STAGE_FINAL);
        scoreGeneralKnowledge = cursor.getString(Contract.ScoreEntry.POSITION_GENERAL_KNOWLEDGE);
        scorePortuguese = cursor.getString(Contract.ScoreEntry.POSITION_PORTUGUESE);
        scoreForeignLanguage = cursor.getString(Contract.ScoreEntry.POSITION_FOREIGN_LANGUAGE);
        scoreComposing = cursor.getString(Contract.ScoreEntry.POSITION_COMPOSING);
        scoreDiscipline1 = cursor.getString(Contract.ScoreEntry.POSITION_DISCIPLINE_1);
        scoreDiscipline2 = cursor.getString(Contract.ScoreEntry.POSITION_DISCIPLINE_2);
        scoreClassification = cursor.getString(Contract.ScoreEntry.POSITION_CLASSIFICATION);
        scoreApprovalResult = cursor.getString(Contract.ScoreEntry.POSITION_APPROVAL_RESULT);
        cursor.close();
    }

    public Score(JSONObject jsonObject) throws JSONException {
        scoreTotalStage1 = Utils.getJsonString(jsonObject, "tt_pontos_etapa1");
        scoreTotalStage2 = Utils.getJsonString(jsonObject, "tt_pontos_etapa2");
        scoreTotalStageFinal = Utils.getJsonString(jsonObject, "tt_escore_final");
        scoreGeneralKnowledge = Utils.getJsonString(jsonObject, "nu_pontos_cg");
        scorePortuguese = Utils.getJsonString(jsonObject, "nu_pontos_por");
        scoreForeignLanguage = Utils.getJsonString(jsonObject, "nu_pontos_le");
        scoreComposing = Utils.getJsonString(jsonObject, "nu_pontos_red");
        scoreDiscipline1 = Utils.getJsonString(jsonObject, "nu_pontos_ce1");
        scoreDiscipline2 = Utils.getJsonString(jsonObject, "nu_pontos_ce2");
        scoreClassification = Utils.getJsonString(jsonObject, "nu_classificacao");
        scoreApprovalResult = Utils.getJsonString(jsonObject, "st_final");
    }

    private Score(Parcel in) {
        scoreTotalStage1 = in.readString();
        scoreTotalStage2 = in.readString();
        scoreTotalStageFinal = in.readString();
        scoreGeneralKnowledge = in.readString();
        scorePortuguese = in.readString();
        scoreForeignLanguage = in.readString();
        scoreComposing = in.readString();
        scoreDiscipline1 = in.readString();
        scoreDiscipline2 = in.readString();
        scoreClassification = in.readString();
        scoreApprovalResult = in.readString();
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(scoreTotalStage1);
        parcel.writeString(scoreTotalStage2);
        parcel.writeString(scoreTotalStageFinal);
        parcel.writeString(scoreGeneralKnowledge);
        parcel.writeString(scorePortuguese);
        parcel.writeString(scoreForeignLanguage);
        parcel.writeString(scoreComposing);
        parcel.writeString(scoreDiscipline1);
        parcel.writeString(scoreDiscipline2);
        parcel.writeString(scoreClassification);
        parcel.writeString(scoreApprovalResult);
    }
}
