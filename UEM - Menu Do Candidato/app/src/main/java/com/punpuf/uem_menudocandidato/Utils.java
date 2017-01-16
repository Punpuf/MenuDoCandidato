package com.punpuf.uem_menudocandidato;

import android.content.ContentValues;

import com.punpuf.uem_menudocandidato.data.Contract;
import com.punpuf.uem_menudocandidato.model.ApplicantInfo;
import com.punpuf.uem_menudocandidato.model.Exam;
import com.punpuf.uem_menudocandidato.model.ExamLocation;
import com.punpuf.uem_menudocandidato.model.Miscellaneous;
import com.punpuf.uem_menudocandidato.model.Payment;
import com.punpuf.uem_menudocandidato.model.Score;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

public class Utils {
    
    public static ContentValues getContentValues(ApplicantInfo applicantInfo){
        ContentValues values = new ContentValues();
        values.put(Contract.ApplicantInfoEntry.APPLICANT_NAME, applicantInfo.applicantName);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_REGISTRATION_NUMBER, applicantInfo.applicantRegistrationNumber);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_BIRTHDATE, applicantInfo.applicantBirthDate);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_DOCUMENT_TYPE, applicantInfo.applicantDocumentType);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_DOCUMENT_NUMBER, applicantInfo.applicantDocumentNumber);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_GENDER, applicantInfo.applicantGender);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_IS_LEFT_HANDED, applicantInfo.applicantIsLeftHanded);
        values.put(Contract.ApplicantInfoEntry.APPLICANT_USES_QUOTA, applicantInfo.applicantUsesQuota);
        return values;
    }

    public static ContentValues getContentValues(Payment payment){
        ContentValues values = new ContentValues();
        values.put(Contract.PaymentEntry.PAYMENT_STATUS, payment.paymentStatus);
        values.put(Contract.PaymentEntry.PAYMENT_DATE, payment.paymentDate);
        return values;
    }

    public static ContentValues getContentValues(Exam exam){
        ContentValues values = new ContentValues();
        values.put(Contract.ExamEntry.EXAM_NAME, exam.examName);
        values.put(Contract.ExamEntry.EXAM_TYPE, exam.examType);
        values.put(Contract.ExamEntry.EXAM_PHASE, exam.examPhase);
        values.put(Contract.ExamEntry.EXAM_DATE_1, exam.examDate1);
        values.put(Contract.ExamEntry.EXAM_DATE_2, exam.examDate2);
        values.put(Contract.ExamEntry.EXAM_DATE_3, exam.examDate3);
        values.put(Contract.ExamEntry.EXAM_DISCIPLINE_1, exam.examDiscipline1);
        values.put(Contract.ExamEntry.EXAM_DISCIPLINE_2, exam.examDiscipline2);
        values.put(Contract.ExamEntry.EXAM_COURSE, exam.examCourse);
        values.put(Contract.ExamEntry.EXAM_FOREIGN_LANGUAGE, exam.examForeignLanguage);
        values.put(Contract.ExamEntry.EXAM_COMPETITION, exam.examCompetition);
        return values;
    }

    public static ContentValues getContentValues(ExamLocation examLocation){
        ContentValues values = new ContentValues();
        values.put(Contract.ExamLocationEntry.EXAM_LOCATION_HEADQUARTERS,
                examLocation.examLocationHeadquarters);
        values.put(Contract.ExamLocationEntry.EXAM_LOCATION_CITY,
                examLocation.examLocationCity);
        values.put(Contract.ExamLocationEntry.EXAM_LOCATION_STREET,
                examLocation.examLocationStreet);
        values.put(Contract.ExamLocationEntry.EXAM_LOCATION_BLOCK,
                examLocation.examLocationBlock);
        values.put(Contract.ExamLocationEntry.EXAM_LOCATION_ROOM,
                examLocation.examLocationRoom);
        values.put(Contract.ExamLocationEntry.EXAM_LOCATION_MAP_URL,
                examLocation.examLocationMapUrl);
        return values;
        
    }

    public static ContentValues getContentValues(Score score){
        ContentValues values = new ContentValues();
        values.put(Contract.ScoreEntry.SCORE_TOTAL_STAGE_1, score.scoreTotalStage1);
        values.put(Contract.ScoreEntry.SCORE_TOTAL_STAGE_2, score.scoreTotalStage2);
        values.put(Contract.ScoreEntry.SCORE_TOTAL_STAGE_FINAL, score.scoreTotalStageFinal);
        values.put(Contract.ScoreEntry.SCORE_GENERAL_KNOWLEDGE, score.scoreGeneralKnowledge);
        values.put(Contract.ScoreEntry.SCORE_PORTUGUESE, score.scorePortuguese);
        values.put(Contract.ScoreEntry.SCORE_FOREIGN_LANGUAGE, score.scoreForeignLanguage);
        values.put(Contract.ScoreEntry.SCORE_COMPOSING, score.scoreComposing);
        values.put(Contract.ScoreEntry.SCORE_DISCIPLINE_1, score.scoreDiscipline1);
        values.put(Contract.ScoreEntry.SCORE_DISCIPLINE_2, score.scoreDiscipline2);
        values.put(Contract.ScoreEntry.SCORE_CLASSIFICATION, score.scoreClassification);
        values.put(Contract.ScoreEntry.SCORE_APPROVAL_RESULT, score.scoreApprovalResult);
        return values;
    }

    public static ContentValues getContentValues(Miscellaneous miscellaneous){
        ContentValues values = new ContentValues();
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_COMPETITION,
                miscellaneous.miscellaneousInfoAvailabilityCompetition);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_EXAM_LOCATION,
                miscellaneous.miscellaneousInfoAvailabilityExamLocation);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_COMPOSING,
                miscellaneous.miscellaneousInfoAvailabilityComposing);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_PERFORMANCE,
                miscellaneous.miscellaneousInfoAvailabilityPerformance);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_COMPOSING_GENRE_1,
                miscellaneous.miscellaneousComposingGenre1);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_COMPOSING_GENRE_2,
                miscellaneous.miscellaneousComposingGenre2);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_CD_EVENT,
                miscellaneous.miscellaneousCdEvent);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_CD_COMPOSING,
                miscellaneous.miscellaneousCdComposing);
        values.put(Contract.MiscellaneousEntry.MISCELLANEOUS_CD_COURSE,
                miscellaneous.miscellaneousCdCourse);
        return values;
    }

    public static String getJsonString(JSONObject json, String value) throws JSONException {
        return unEncryptJsonValue(json.getString(getEncryptedJsonKey(value)));
    }

    private static String getEncryptedJsonKey(String originalValue){
        String encryptionKey = " 'HIJKLMNOPQRSTUVWXYZ\u00c7\u00c1\u00c9\u00d3\u00cd\u00da\u00c3\u00d51234567890</abcdefghijklmnopqrstuvwxyz\u00e7\u00e9\u00e1\u00ed\u00fa\u00f3\u00e3\u00f5ABCDEFG.;:?,\u00ba]}\u00a7[{\u00aa!@#$%&*()_+-=\\/|'\">";
        int encryptionKeyLength = encryptionKey.length();

        char[] originalValueCharArray = originalValue.toCharArray();
        String encryptedJsonKey = "";

        for(char currentChar : originalValueCharArray){
            int encryptedCharPos = encryptionKey.indexOf(currentChar) + 3;
            if(encryptedCharPos >= encryptionKeyLength) encryptedCharPos -= encryptionKeyLength;
            encryptedJsonKey += encryptionKey.charAt(encryptedCharPos);
        }
        Timber.d("original value is " + originalValue + ", and the encrypted json key is " + encryptedJsonKey);
        return encryptedJsonKey;
    }


    private static String unEncryptJsonValue(String encryptedValue){
        String encryptionKey = " '-</abcdefghijklmnopqrstuvwxyz\u00e7\u00e9\u00e1\u00ed\u00fa\u00f3\u00e3\u00f5ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00c7\u00c1\u00c9\u00d3\u00cd\u00da\u00c3\u00d51234567890.;:?,\u00ba]}\u00a7[{\u00aa!@#$%&*()_+-=\\/|'\">";
        int encryptionKeyLength = encryptionKey.length();

        char[] encryptedValueCharArray = encryptedValue.toCharArray();
        String unencryptedJsonValue = "";

        for(char currentChar : encryptedValueCharArray){
            int unencryptedCharPos = encryptionKey.indexOf(currentChar) - 5;
            if(unencryptedCharPos < 0) unencryptedCharPos += encryptionKeyLength;
            unencryptedJsonValue += encryptionKey.charAt(unencryptedCharPos);
        }

        return unencryptedJsonValue;
    }

    public static String getEncryptedRegistrationNum(String registrationNum){
            String encryptionKey = " '-</abcdefghijklmnopqrstuvwxyz\u00e7\u00e9\u00e1\u00ed\u00fa\u00f3\u00e3\u00f5ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00c7\u00c1\u00c9\u00d3\u00cd\u00da\u00c3\u00d51234567890.;:?,\u00ba]}\u00a7[{\u00aa!@#$%&*()_+-=\\/|'\">";
        int encryptionKeyLength = encryptionKey.length();

        char[] registrationNumCharArray = registrationNum.toCharArray();
        String encryptedRegistrationNum = "";

        for(char currentChar : registrationNumCharArray){
            int encryptedCharPos = encryptionKey.indexOf(currentChar) + 5;
            if(encryptedCharPos >= encryptionKeyLength) encryptedCharPos -= encryptionKeyLength;
            encryptedRegistrationNum += encryptionKey.charAt(encryptedCharPos);
        }
        Timber.d("encryptedRegistrationNum is " + encryptedRegistrationNum);
        return encryptedRegistrationNum;
    }


    
}
