package com.punpuf.uem_menudocandidato.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

@SuppressWarnings("all")
public final class Contract {

    public static final String CONTENT_AUTHORITY = "com.punpuf.uem_menudocandidato.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_APPLICANT_INFO = "applicant";
    public static final String PATH_PAYMENT = "payment";
    public static final String PATH_EXAM = "exam";
    public static final String PATH_EXAM_LOCATION = "exam_location";
    public static final String PATH_SCORE = "score";
    public static final String PATH_MISCELLANEOUS = "miscellaneous";



    public static abstract class ApplicantInfoEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_APPLICANT_INFO).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_APPLICANT_INFO;

        public static final String TABLE_NAME = "applicant";
        public static final String APPLICANT_NAME = "applicant_name";
        public static final String APPLICANT_REGISTRATION_NUMBER = "applicant_registration_number";
        public static final String APPLICANT_BIRTHDATE = "applicant_birth_date";
        public static final String APPLICANT_DOCUMENT_TYPE = "applicant_document_type";
        public static final String APPLICANT_DOCUMENT_NUMBER = "applicant_document_number";
        public static final String APPLICANT_GENDER = "applicant_gender";
        public static final String APPLICANT_IS_LEFT_HANDED = "applicant_is_left_handed";
        public static final String APPLICANT_USES_QUOTA = "applicant_uses_quota";

        public static final int POSITION_NAME = 0;
        public static final int POSITION_REGISTRATION_NUMBER = 1;
        public static final int POSITION_BIRTHDATE = 2;
        public static final int POSITION_DOCUMENT_TYPE = 3;
        public static final int POSITION_DOCUMENT_NUMBER = 4;
        public static final int POSITION_GENDER = 5;
        public static final int POSITION_IS_LEFT_HANDED = 6;
        public static final int POSITION_USES_QUOTA = 7;

        public static final String[] PROJECTION = {
                APPLICANT_NAME,
                APPLICANT_REGISTRATION_NUMBER,
                APPLICANT_BIRTHDATE,
                APPLICANT_DOCUMENT_TYPE,
                APPLICANT_DOCUMENT_NUMBER,
                APPLICANT_GENDER,
                APPLICANT_IS_LEFT_HANDED,
                APPLICANT_USES_QUOTA
        };

    }


    public static abstract class PaymentEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_PAYMENT).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_PAYMENT;

        public static final String TABLE_NAME = "payment";
        public static final String PAYMENT_STATUS = "payment_status";
        public static final String PAYMENT_DATE = "payment_date";

        public static final int POSITION_STATUS = 0;
        public static final int POSITION_DATE = 1;

        public static final String[] PROJECTION = {
                PAYMENT_STATUS,
                PAYMENT_DATE
        };
    }


    public static abstract class ExamEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_EXAM).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_EXAM;

        public static final String TABLE_NAME = "exam";
        public static final String EXAM_NAME = "exam_name";
        public static final String EXAM_TYPE = "exam_type";
        public static final String EXAM_PHASE = "exam_phase";
        public static final String EXAM_DATE_1 = "exam_date_1";
        public static final String EXAM_DATE_2 = "exam_date_2";
        public static final String EXAM_DATE_3 = "exam_date_3";
        public static final String EXAM_DISCIPLINE_1 = "exam_discipline_1";
        public static final String EXAM_DISCIPLINE_2 = "exam_discipline_2";
        public static final String EXAM_COURSE = "exam_course";
        public static final String EXAM_FOREIGN_LANGUAGE = "exam_foreign_language";
        public static final String EXAM_COMPETITION = "exam_competition";

        public static final int POSITION_NAME = 0;
        public static final int POSITION_TYPE = 1;
        public static final int POSITION_PHASE = 2;
        public static final int POSITION_DATE_1 = 3;
        public static final int POSITION_DATE_2 = 4;
        public static final int POSITION_DATE_3 = 5;
        public static final int POSITION_DISCIPLINE_1 = 6;
        public static final int POSITION_DISCIPLINE_2 = 7;
        public static final int POSITION_COURSE = 8;
        public static final int POSITION_FOREIGN_LANGUAGE = 9;
        public static final int POSITION_COMPETITION = 10;

        public static final String[] PROJECTION = {
                EXAM_NAME,
                EXAM_TYPE,
                EXAM_PHASE,
                EXAM_DATE_1,
                EXAM_DATE_2,
                EXAM_DATE_3,
                EXAM_DISCIPLINE_1,
                EXAM_DISCIPLINE_2,
                EXAM_COURSE,
                EXAM_FOREIGN_LANGUAGE,
                EXAM_COMPETITION
        };
    }



    public static abstract class ExamLocationEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_EXAM_LOCATION).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_EXAM_LOCATION;

        public static final String TABLE_NAME = "exam_location";
        public static final String EXAM_LOCATION_HEADQUARTERS = "exam_location_headquarters";
        public static final String EXAM_LOCATION_CITY = "exam_location_city";
        public static final String EXAM_LOCATION_STREET = "exam_location_street";
        public static final String EXAM_LOCATION_BLOCK = "exam_location_block";
        public static final String EXAM_LOCATION_ROOM = "exam_location_room";
        public static final String EXAM_LOCATION_MAP_URL = "exam_location_map_url";

        public static final int POSITION_HEADQUARTERS = 0;
        public static final int POSITION_CITY = 1;
        public static final int POSITION_STREET = 2;
        public static final int POSITION_BLOCK = 3;
        public static final int POSITION_ROOM = 4;
        public static final int POSITION_MAP_URL = 5;

        public static final String[] PROJECTION = {
                EXAM_LOCATION_HEADQUARTERS,
                EXAM_LOCATION_CITY,
                EXAM_LOCATION_STREET,
                EXAM_LOCATION_BLOCK,
                EXAM_LOCATION_ROOM,
                EXAM_LOCATION_MAP_URL
        };

    }


    public static abstract class ScoreEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_SCORE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_SCORE;

        public static final String TABLE_NAME = "score";
        public static final String SCORE_TOTAL_STAGE_1 = "score_total_stage_1";
        public static final String SCORE_TOTAL_STAGE_2 = "score_total_stage_2";
        public static final String SCORE_TOTAL_STAGE_FINAL = "score_total_stage_final";
        public static final String SCORE_GENERAL_KNOWLEDGE = "score_general_knowledge";
        public static final String SCORE_PORTUGUESE = "score_portuguese";
        public static final String SCORE_FOREIGN_LANGUAGE = "score_foreign_language";
        public static final String SCORE_COMPOSING = "score_composing";
        public static final String SCORE_DISCIPLINE_1 = "score_discipline_1";
        public static final String SCORE_DISCIPLINE_2 = "score_discipline_2";
        public static final String SCORE_CLASSIFICATION = "score_classification";
        public static final String SCORE_APPROVAL_RESULT = "score_approval_result";

        public static final int POSITION_TOTAL_STAGE_1 = 0;
        public static final int POSITION_TOTAL_STAGE_2 = 1;
        public static final int POSITION_TOTAL_STAGE_FINAL = 2;
        public static final int POSITION_GENERAL_KNOWLEDGE = 3;
        public static final int POSITION_PORTUGUESE = 4;
        public static final int POSITION_FOREIGN_LANGUAGE = 5;
        public static final int POSITION_COMPOSING = 6;
        public static final int POSITION_DISCIPLINE_1 = 7;
        public static final int POSITION_DISCIPLINE_2 = 8;
        public static final int POSITION_CLASSIFICATION = 9;
        public static final int POSITION_APPROVAL_RESULT = 10;

        public static final String[] PROJECTION = {
                SCORE_TOTAL_STAGE_1,
                SCORE_TOTAL_STAGE_2,
                SCORE_TOTAL_STAGE_FINAL,
                SCORE_GENERAL_KNOWLEDGE,
                SCORE_PORTUGUESE,
                SCORE_FOREIGN_LANGUAGE,
                SCORE_COMPOSING,
                SCORE_DISCIPLINE_1,
                SCORE_DISCIPLINE_2,
                SCORE_CLASSIFICATION,
                SCORE_APPROVAL_RESULT
        };

    }


    public static abstract class MiscellaneousEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_MISCELLANEOUS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_MISCELLANEOUS;

        public static final String TABLE_NAME = "miscellaneous";
        public static final String MISCELLANEOUS_AVAILABILITY_COMPETITION = "miscellaneous_availability_competition";
        public static final String MISCELLANEOUS_AVAILABILITY_EXAM_LOCATION = "miscellaneous_availability_exam_location";
        public static final String MISCELLANEOUS_AVAILABILITY_COMPOSING = "miscellaneous_availability_composing";
        public static final String MISCELLANEOUS_AVAILABILITY_PERFORMANCE = "miscellaneous_availability_performance";
        public static final String MISCELLANEOUS_COMPOSING_GENRE_1 = "miscellaneous_genre_1";
        public static final String MISCELLANEOUS_COMPOSING_GENRE_2 = "miscellaneous_genre_2";
        public static final String MISCELLANEOUS_CD_EVENT = "miscellaneous_cd_event";
        public static final String MISCELLANEOUS_CD_COMPOSING = "miscellaneous_cd_composing";
        public static final String MISCELLANEOUS_CD_COURSE = "miscellaneous_cd_course";

        public static final int POSITION_AVAILABILITY_COMPETITION = 0;
        public static final int POSITION_AVAILABILITY_EXAM_LOCATION = 1;
        public static final int POSITION_AVAILABILITY_COMPOSING = 2;
        public static final int POSITION_AVAILABILITY_PERFORMANCE = 3;
        public static final int POSITION_COMPOSING_GENRE_1 = 4;
        public static final int POSITION_COMPOSING_GENRE_2 = 5;
        public static final int POSITION_CD_EVENT = 6;
        public static final int POSITION_CD_COMPOSING = 7;
        public static final int POSITION_CD_COURSE = 8;

        public static final String[] PROJECTION = {
                MISCELLANEOUS_AVAILABILITY_COMPETITION,
                MISCELLANEOUS_AVAILABILITY_EXAM_LOCATION,
                MISCELLANEOUS_AVAILABILITY_COMPOSING,
                MISCELLANEOUS_AVAILABILITY_PERFORMANCE,
                MISCELLANEOUS_COMPOSING_GENRE_1,
                MISCELLANEOUS_COMPOSING_GENRE_2,
                MISCELLANEOUS_CD_EVENT,
                MISCELLANEOUS_CD_COMPOSING,
                MISCELLANEOUS_CD_COURSE
        };

    }


}
