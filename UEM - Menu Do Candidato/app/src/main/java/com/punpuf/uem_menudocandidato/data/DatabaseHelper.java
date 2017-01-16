package com.punpuf.uem_menudocandidato.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "applicantManager";

    private static final String CREATE_TABLE_APPLICANTS_INFO = "CREATE TABLE " +
            Contract.ApplicantInfoEntry.TABLE_NAME + "(" +
            Contract.ApplicantInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.ApplicantInfoEntry.APPLICANT_NAME + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_BIRTHDATE + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_REGISTRATION_NUMBER + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_DOCUMENT_TYPE + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_DOCUMENT_NUMBER + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_GENDER + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_IS_LEFT_HANDED + " TEXT," +
            Contract.ApplicantInfoEntry.APPLICANT_USES_QUOTA + " TEXT" +
            ")";

    private static final String CREATE_TABLE_PAYMENT = "CREATE TABLE " +
            Contract.PaymentEntry.TABLE_NAME + "(" +
            Contract.PaymentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.PaymentEntry.PAYMENT_STATUS + " TEXT," +
            Contract.PaymentEntry.PAYMENT_DATE + " TEXT" +
            ")";

    private static final String CREATE_TABLE_EXAM = "CREATE TABLE " +
            Contract.ExamEntry.TABLE_NAME + "(" +
            Contract.ExamEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.ExamEntry.EXAM_NAME + " TEXT," +
            Contract.ExamEntry.EXAM_TYPE + " TEXT," +
            Contract.ExamEntry.EXAM_PHASE + " TEXT," +
            Contract.ExamEntry.EXAM_DATE_1 + " TEXT," +
            Contract.ExamEntry.EXAM_DATE_2 + " TEXT," +
            Contract.ExamEntry.EXAM_DATE_3 + " TEXT," +
            Contract.ExamEntry.EXAM_DISCIPLINE_1 + " TEXT," +
            Contract.ExamEntry.EXAM_DISCIPLINE_2 + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_ROOM + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_BLOCK + " TEXT," +
            Contract.ExamEntry.EXAM_COURSE + " TEXT," +
            Contract.ExamEntry.EXAM_FOREIGN_LANGUAGE + " TEXT," +
            Contract.ExamEntry.EXAM_COMPETITION + " TEXT" +
            ")";

    private static final String CREATE_TABLE_EXAM_LOCATION = "CREATE TABLE " +
            Contract.ExamLocationEntry.TABLE_NAME + "(" +
            Contract.ExamLocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_HEADQUARTERS + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_CITY + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_STREET + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_BLOCK + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_ROOM + " TEXT," +
            Contract.ExamLocationEntry.EXAM_LOCATION_MAP_URL + " TEXT" +
            ")";

    private static final String CREATE_TABLE_SCORE = "CREATE TABLE " +
            Contract.ScoreEntry.TABLE_NAME + "(" +
            Contract.ScoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.ScoreEntry.SCORE_TOTAL_STAGE_1 + " TEXT," +
            Contract.ScoreEntry.SCORE_TOTAL_STAGE_2 + " TEXT," +
            Contract.ScoreEntry.SCORE_TOTAL_STAGE_FINAL + " TEXT," +
            Contract.ScoreEntry.SCORE_GENERAL_KNOWLEDGE + " TEXT," +
            Contract.ScoreEntry.SCORE_PORTUGUESE + " TEXT," +
            Contract.ScoreEntry.SCORE_FOREIGN_LANGUAGE + " TEXT," +
            Contract.ScoreEntry.SCORE_COMPOSING + " TEXT," +
            Contract.ScoreEntry.SCORE_DISCIPLINE_1 + " TEXT," +
            Contract.ScoreEntry.SCORE_DISCIPLINE_2 + " TEXT," +
            Contract.ScoreEntry.SCORE_CLASSIFICATION + " TEXT," +
            Contract.ScoreEntry.SCORE_APPROVAL_RESULT + " TEXT" +
            ")";

    private static final String CREATE_TABLE_MISCELLANEOUS = "CREATE TABLE " +
            Contract.MiscellaneousEntry.TABLE_NAME + "(" +
            Contract.MiscellaneousEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_COMPETITION + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_EXAM_LOCATION + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_COMPOSING + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_AVAILABILITY_PERFORMANCE + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_COMPOSING_GENRE_1 + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_COMPOSING_GENRE_2 + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_CD_EVENT + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_CD_COMPOSING + " TEXT," +
            Contract.MiscellaneousEntry.MISCELLANEOUS_CD_COURSE + " TEXT" +
            ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_APPLICANTS_INFO);
        database.execSQL(CREATE_TABLE_PAYMENT);
        database.execSQL(CREATE_TABLE_EXAM);
        database.execSQL(CREATE_TABLE_EXAM_LOCATION);
        database.execSQL(CREATE_TABLE_SCORE);
        database.execSQL(CREATE_TABLE_MISCELLANEOUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ApplicantInfoEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.PaymentEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ExamEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ExamLocationEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.ScoreEntry.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.MiscellaneousEntry.TABLE_NAME);
        onCreate(database);
    }

    public boolean isDatabaseEmpty(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Contract.ApplicantInfoEntry.TABLE_NAME, null);
        if(cursor.moveToFirst()){
            cursor.close();
            database.close();
            return false;
        }
        database.close();
        return true;
    }

}
