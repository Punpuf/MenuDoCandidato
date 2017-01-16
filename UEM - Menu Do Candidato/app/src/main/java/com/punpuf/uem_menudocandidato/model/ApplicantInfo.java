package com.punpuf.uem_menudocandidato.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplicantInfo implements Parcelable{

    public final String applicantName;
    public final String applicantBirthDate;
    public final String applicantRegistrationNumber;
    public final String applicantDocumentType;
    public final String applicantDocumentNumber;
    public final String applicantGender;
    public final String applicantIsLeftHanded;
    public final String applicantUsesQuota;
    
    public ApplicantInfo(Cursor cursor){
        cursor.moveToFirst();
        applicantName = cursor.getString(Contract.ApplicantInfoEntry.POSITION_NAME);
        applicantRegistrationNumber = cursor.getString(Contract.ApplicantInfoEntry.POSITION_REGISTRATION_NUMBER);
        applicantBirthDate = cursor.getString(Contract.ApplicantInfoEntry.POSITION_BIRTHDATE);
        applicantDocumentType = cursor.getString(Contract.ApplicantInfoEntry.POSITION_DOCUMENT_TYPE);
        applicantDocumentNumber = cursor.getString(Contract.ApplicantInfoEntry.POSITION_DOCUMENT_NUMBER);
        applicantGender = cursor.getString(Contract.ApplicantInfoEntry.POSITION_GENDER);
        applicantIsLeftHanded = cursor.getString(Contract.ApplicantInfoEntry.POSITION_IS_LEFT_HANDED);
        applicantUsesQuota = cursor.getString(Contract.ApplicantInfoEntry.POSITION_USES_QUOTA);
        cursor.close();
    }

    public ApplicantInfo(JSONObject jsonObject) throws JSONException {
        applicantName = Utils.getJsonString(jsonObject, "nm_candidato");
        applicantRegistrationNumber = Utils.getJsonString(jsonObject, "nu_inscricao") + "-" + Utils.getJsonString(jsonObject, "dv_inscricao");
        applicantBirthDate = Utils.getJsonString(jsonObject, "dt_nascimento");
        applicantDocumentType = Utils.getJsonString(jsonObject, "tp_documento");
        applicantDocumentNumber = Utils.getJsonString(jsonObject, "nu_documento");
        applicantGender = Utils.getJsonString(jsonObject, "nu_sexo");
        applicantIsLeftHanded = Utils.getJsonString(jsonObject, "nu_canhoto");
        applicantUsesQuota = Utils.getJsonString(jsonObject, "nu_opcao_cotas");
    }

    private ApplicantInfo(Parcel in) {
        applicantName = in.readString();
        applicantBirthDate = in.readString();
        applicantRegistrationNumber = in.readString();
        applicantDocumentType = in.readString();
        applicantDocumentNumber = in.readString();
        applicantGender = in.readString();
        applicantIsLeftHanded = in.readString();
        applicantUsesQuota = in.readString();
    }

    public static final Creator<ApplicantInfo> CREATOR = new Creator<ApplicantInfo>() {
        @Override
        public ApplicantInfo createFromParcel(Parcel in) {
            return new ApplicantInfo(in);
        }

        @Override
        public ApplicantInfo[] newArray(int size) {
            return new ApplicantInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(applicantName);
        parcel.writeString(applicantBirthDate);
        parcel.writeString(applicantRegistrationNumber);
        parcel.writeString(applicantDocumentType);
        parcel.writeString(applicantDocumentNumber);
        parcel.writeString(applicantGender);
        parcel.writeString(applicantIsLeftHanded);
        parcel.writeString(applicantUsesQuota);
    }

}
