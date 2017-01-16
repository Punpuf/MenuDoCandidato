package com.punpuf.uem_menudocandidato.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DatabaseProvider extends ContentProvider {

    private final UriMatcher mUriMatcher = buildUriMatcher();
    private DatabaseHelper mDatabaseHelper;

    private final int MATCHED_APPLICANT = 100;
    private final int MATCHED_PAYMENT = 200;
    private final int MATCHED_EXAM = 300;
    private final int MATCHED_EXAM_LOCATION = 400;
    private final int MATCHED_SCORE = 500;
    private final int MATCHED_MISCELLANEOUS = 600;


    private UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_APPLICANT_INFO, MATCHED_APPLICANT);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_PAYMENT, MATCHED_PAYMENT);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_EXAM, MATCHED_EXAM);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_EXAM_LOCATION, MATCHED_EXAM_LOCATION);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_SCORE, MATCHED_SCORE);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_MISCELLANEOUS, MATCHED_MISCELLANEOUS);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if(mUriMatcher != null){
            switch (mUriMatcher.match(uri)){
                case MATCHED_APPLICANT: return Contract.ApplicantInfoEntry.CONTENT_TYPE;
                case MATCHED_PAYMENT: return Contract.PaymentEntry.CONTENT_TYPE;
                case MATCHED_EXAM: return Contract.ExamEntry.CONTENT_TYPE;
                case MATCHED_EXAM_LOCATION: return Contract.ExamLocationEntry.CONTENT_TYPE;
                case MATCHED_SCORE: return Contract.ScoreEntry.CONTENT_TYPE;
                case MATCHED_MISCELLANEOUS: return Contract.MiscellaneousEntry.CONTENT_TYPE;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if(mUriMatcher != null){
            switch (mUriMatcher.match(uri)){

                case MATCHED_APPLICANT:
                    return mDatabaseHelper.getReadableDatabase().query(
                            Contract.ApplicantInfoEntry.TABLE_NAME,
                            columns,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                case MATCHED_PAYMENT:
                    return mDatabaseHelper.getReadableDatabase().query(
                            Contract.PaymentEntry.TABLE_NAME,
                            columns,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                case MATCHED_EXAM:
                    return mDatabaseHelper.getReadableDatabase().query(
                            Contract.ExamEntry.TABLE_NAME,
                            columns,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                case MATCHED_EXAM_LOCATION:
                    return mDatabaseHelper.getReadableDatabase().query(
                        Contract.ExamLocationEntry.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                case MATCHED_SCORE:
                    return mDatabaseHelper.getReadableDatabase().query(
                            Contract.ScoreEntry.TABLE_NAME,
                            columns,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                case MATCHED_MISCELLANEOUS:
                    return mDatabaseHelper.getReadableDatabase().query(
                            Contract.MiscellaneousEntry.TABLE_NAME,
                            columns,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

            }
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if(mUriMatcher != null){
            switch (mUriMatcher.match(uri)){
                case MATCHED_APPLICANT:
                    mDatabaseHelper.getWritableDatabase().insert(
                            Contract.ApplicantInfoEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
                    break;

                case MATCHED_PAYMENT:
                    mDatabaseHelper.getWritableDatabase().insert(
                            Contract.PaymentEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
                    break;

                case MATCHED_EXAM:
                    mDatabaseHelper.getWritableDatabase().insert(
                            Contract.ExamEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
                    break;

                case MATCHED_EXAM_LOCATION:
                    mDatabaseHelper.getWritableDatabase().insert(
                            Contract.ExamLocationEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
                    break;

                case MATCHED_SCORE:
                    mDatabaseHelper.getWritableDatabase().insert(
                            Contract.ScoreEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
                    break;

                case MATCHED_MISCELLANEOUS:
                    mDatabaseHelper.getWritableDatabase().insert(
                            Contract.MiscellaneousEntry.TABLE_NAME,
                            null,
                            contentValues
                    );
                    break;

            }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        if(mUriMatcher != null){
            switch (mUriMatcher.match(uri)){
                case MATCHED_APPLICANT:
                    return mDatabaseHelper.getWritableDatabase().delete(
                            Contract.ApplicantInfoEntry.TABLE_NAME,
                            where,
                            whereArgs
                    );

                case MATCHED_PAYMENT:
                    return mDatabaseHelper.getWritableDatabase().delete(
                            Contract.PaymentEntry.TABLE_NAME,
                            where,
                            whereArgs
                    );

                case MATCHED_EXAM:
                    return mDatabaseHelper.getWritableDatabase().delete(
                            Contract.ExamEntry.TABLE_NAME,
                            where,
                            whereArgs
                    );

                case MATCHED_EXAM_LOCATION:
                    return mDatabaseHelper.getWritableDatabase().delete(
                            Contract.ExamLocationEntry.TABLE_NAME,
                            where,
                            whereArgs
                    );

                case MATCHED_SCORE:
                    return mDatabaseHelper.getWritableDatabase().delete(
                            Contract.ScoreEntry.TABLE_NAME,
                            where,
                            whereArgs
                    );

                case MATCHED_MISCELLANEOUS:
                    return mDatabaseHelper.getWritableDatabase().delete(
                            Contract.MiscellaneousEntry.TABLE_NAME,
                            where,
                            whereArgs
                    );

            }
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String where, @Nullable String[] whereArgs) {

        if(mUriMatcher != null){
            switch (mUriMatcher.match(uri)){
                case MATCHED_APPLICANT:
                    return mDatabaseHelper.getWritableDatabase().update(
                            Contract.ApplicantInfoEntry.TABLE_NAME,
                            contentValues,
                            where,
                            whereArgs
                    );

                case MATCHED_PAYMENT:
                    return mDatabaseHelper.getWritableDatabase().update(
                            Contract.PaymentEntry.TABLE_NAME,
                            contentValues,
                            where,
                            whereArgs
                    );

                case MATCHED_EXAM:
                    return mDatabaseHelper.getWritableDatabase().update(
                            Contract.ExamEntry.TABLE_NAME,
                            contentValues,
                            where,
                            whereArgs
                    );

                case MATCHED_EXAM_LOCATION:
                    return mDatabaseHelper.getWritableDatabase().update(
                            Contract.ExamLocationEntry.TABLE_NAME,
                            contentValues,
                            where,
                            whereArgs
                    );

                case MATCHED_SCORE:
                    return mDatabaseHelper.getWritableDatabase().update(
                            Contract.ScoreEntry.TABLE_NAME,
                            contentValues,
                            where,
                            whereArgs
                    );

                case MATCHED_MISCELLANEOUS:
                    return mDatabaseHelper.getWritableDatabase().update(
                            Contract.MiscellaneousEntry.TABLE_NAME,
                            contentValues,
                            where,
                            whereArgs
                    );

            }
        }
        return 0;
    }

}
