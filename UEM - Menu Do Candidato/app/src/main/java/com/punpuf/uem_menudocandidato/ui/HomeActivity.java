package com.punpuf.uem_menudocandidato.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.punpuf.uem_menudocandidato.R;
import com.punpuf.uem_menudocandidato.Utils;
import com.punpuf.uem_menudocandidato.data.Contract;
import com.punpuf.uem_menudocandidato.model.ApplicantInfo;
import com.punpuf.uem_menudocandidato.model.Exam;
import com.punpuf.uem_menudocandidato.model.ExamLocation;
import com.punpuf.uem_menudocandidato.model.Miscellaneous;
import com.punpuf.uem_menudocandidato.model.Payment;
import com.punpuf.uem_menudocandidato.model.Score;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_READ = 12;
    static final String INTENT_ORIGIN_MAIN_ACTIVITY = "origin_main_activity";
    static final String INTENT_ORIGIN_LOGIN_ACTIVITY = "origin_login_activity";
    static final String INTENT_ORIGIN_HOME_ACTIVITY = "origin_home_activity";

    private GoogleApiClient mGoogleApiClient;
    private CredentialRequest mCredentialRequest;
    private RecyclerViewAdapter recyclerViewAdapter;

    @BindView(R.id.home_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.home_recycler_view)
    RecyclerView recyclerView;

    private ApplicantInfo applicantInfo;
    private Payment payment;
    private Exam exam;
    private ExamLocation examLocation;
    private Score score;
    private Miscellaneous miscellaneous;

    private final String KEY_APPLICANT = "applicant";
    private final String KEY_PAYMENT = "payment";
    private final String KEY_EXAM = "exam";
    private final String KEY_EXAM_LOCATION = "exam_location";
    private final String KEY_SCORE = "score";
    private final String KEY_MISCELLANEOUS = "miscellaneous";

    private boolean isResolvingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        //Google API Client Builder
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        mCredentialRequest = new CredentialRequest.Builder()
                .setPasswordLoginSupported(true)
                .build();

        recyclerViewAdapter = new RecyclerViewAdapter(this, null, null);
        recyclerView.setAdapter(recyclerViewAdapter);

        int columnNumber = getResources().getInteger(R.integer.column_number);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(columnNumber, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);


        //gets data
        if(savedInstanceState != null){
            applicantInfo = savedInstanceState.getParcelable(KEY_APPLICANT);
            payment = savedInstanceState.getParcelable(KEY_PAYMENT);
            exam = savedInstanceState.getParcelable(KEY_EXAM);
            examLocation = savedInstanceState.getParcelable(KEY_EXAM_LOCATION);
            score = savedInstanceState.getParcelable(KEY_SCORE);
            miscellaneous = savedInstanceState.getParcelable(KEY_MISCELLANEOUS);
        }
        else {
            fetchLocalData();

            //if user didn't come from login activity updates data
            Intent intent = getIntent();
            if (!intent.getBooleanExtra(INTENT_ORIGIN_LOGIN_ACTIVITY, false)) {
                Timber.d("did not come from login activity");
                swipeRefresh.setRefreshing(false);
                fetchNewData();
            }
        }
        populateAdapter();

        //Swipe Refresh Layout
        int[] colorScheme = {ContextCompat.getColor(this, R.color.colorAccent)};
        swipeRefresh.setColorSchemeColors(colorScheme);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNewData();
                swipeRefresh.setRefreshing(true);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_APPLICANT, applicantInfo);
        outState.putParcelable(KEY_PAYMENT, payment);
        outState.putParcelable(KEY_EXAM, exam);
        outState.putParcelable(KEY_EXAM_LOCATION, examLocation);
        outState.putParcelable(KEY_SCORE, score);
        outState.putParcelable(KEY_MISCELLANEOUS, miscellaneous);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_menu_logout:
                logout();
                break;
            case R.id.home_menu_add_to_calendar:
                addEventToCalendar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchLocalData(){
        applicantInfo = new ApplicantInfo(
                getContentResolver().query(
                        Contract.ApplicantInfoEntry.CONTENT_URI,
                        Contract.ApplicantInfoEntry.PROJECTION,
                        null,
                        null,
                        null)
        );

        payment = new Payment(
                getContentResolver().query(
                        Contract.PaymentEntry.CONTENT_URI,
                        Contract.PaymentEntry.PROJECTION,
                        null,
                        null,
                        null)
        );

        exam = new Exam(
                getContentResolver().query(
                        Contract.ExamEntry.CONTENT_URI,
                        Contract.ExamEntry.PROJECTION,
                        null,
                        null,
                        null)
        );

        examLocation = new ExamLocation(
                getContentResolver().query(
                        Contract.ExamLocationEntry.CONTENT_URI,
                        Contract.ExamLocationEntry.PROJECTION,
                        null,
                        null,
                        null)
        );

        score = new Score(
                getContentResolver().query(
                        Contract.ScoreEntry.CONTENT_URI,
                        Contract.ScoreEntry.PROJECTION,
                        null,
                        null,
                        null)
        );

        miscellaneous = new Miscellaneous(
                getContentResolver().query(
                        Contract.MiscellaneousEntry.CONTENT_URI,
                        Contract.MiscellaneousEntry.PROJECTION,
                        null,
                        null,
                        null)
        );
    }

    /**
     * Disables auto sign in with credentials api
     * Deletes db's info
     * sends user to LoginActivity
     */
    private void logout(){
        if(mGoogleApiClient.isConnected()) {
            Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient);
        }
        deleteAllTables();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(INTENT_ORIGIN_HOME_ACTIVITY, true);
        startActivity(intent);
    }

    /**
     * Extracts date and time from event date
     * And sends intent for it to be added to calendar
     */
    private void addEventToCalendar(){
        try {
            String originalExamDate = exam.examDate1;

            String date = originalExamDate.replaceAll("PROVA 1: ", "").replaceAll(" [^>]*", "");
            int day = Integer.valueOf(date.substring(0, 2));
            int month = Integer.valueOf(date.substring(3, 5));
            int year = Integer.valueOf(date.substring(6));

            String time = originalExamDate.replaceAll("PROVA 1:[^>]* das ", "");

            int beginTimeHour = Integer.valueOf(time.substring(0, 2));
            int beginTimeMinute = Integer.valueOf(time.substring(3, 5));
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(year, month - 1, day, beginTimeHour, beginTimeMinute, 0);
            long beginTimeMillis = beginTime.getTimeInMillis();

            int endTimeHour = Integer.valueOf(time.substring(9, 11));
            int endTimeMinute = Integer.valueOf(time.substring(12, 14));
            Calendar endTime = Calendar.getInstance();
            endTime.set(year, month - 1, day, endTimeHour, endTimeMinute, 0);
            long endTimeMillis = endTime.getTimeInMillis();


            Timber.d("time:" + time + ", end hour:" + endTimeHour + ", end minute:" + endTimeMinute + ", time in millis:" + endTimeMillis);
            Timber.d("time:" + time + ", begin hour:" + beginTimeHour + ", time in millis:" + beginTimeMillis);
            Timber.d("date:" + date + ",  day:" + day + ", month:" + month + ", year:" + year + ",");

            String location = examLocation.examLocationHeadquarters + ", " + examLocation.examLocationCity;


            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, exam.examName)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTimeMillis)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis);
            //The vestibular repeats 3 times
            if(exam.examType.equals("VES")) {
                intent.putExtra(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=3");
            }
            if(intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
            else Toast.makeText(this, R.string.home_toast_add_to_calendar_error, Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.home_toast_add_to_calendar_error, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Populates all of the CardViews that contain data
     */
    private void populateAdapter() {

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> contentList = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean(getString(R.string.inconvenience_was_shown_key), false)){
            titleList.add(getString(R.string.inconvenience_title));
            contentList.add(getString(R.string.inconvenience_message));
            prefs.edit().putBoolean(getString(R.string.inconvenience_was_shown_key), true).apply();
        }

        String applicantInfoString =
                getString(R.string.home_field_title_applicant_name) + " " + applicantInfo.applicantName +
                        "\n" + getString(R.string.home_field_title_applicant_birthdate) + " " + applicantInfo.applicantBirthDate +
                        "\n" + getString(R.string.home_field_title_applicant_registration_number) + " " + applicantInfo.applicantRegistrationNumber +
                        "\n" + getString(R.string.home_field_title_applicant_document_type) + " " + applicantInfo.applicantDocumentType +
                        "\n" + getString(R.string.home_field_title_applicant_document_number) + " " + applicantInfo.applicantDocumentNumber +
                        "\n" + getString(R.string.home_field_title_applicant_gender) + " " + applicantInfo.applicantGender +
                        "\n" + getString(R.string.home_field_title_applicant_is_left_handed) + " " + applicantInfo.applicantIsLeftHanded +
                        "\n" + getString(R.string.home_field_title_applicant_uses_quota) + " " + applicantInfo.applicantUsesQuota;

        titleList.add(getString(R.string.home_card_title_applicants_info));
        contentList.add(applicantInfoString);


        String paymentInfoString = getString(R.string.home_field_title_payment_status) + " " +
                payment.paymentStatus +
                "\n" + getString(R.string.home_field_title_payment_date) + " " +
                payment.paymentDate;

        titleList.add(getString(R.string.home_card_title_payment_info));
        contentList.add(paymentInfoString);
        //buttonList.add(null);


        String examType = exam.examType;
        String examCompetition = exam.examCompetition;
        examCompetition = examCompetition.replaceAll("\n\n", "\n");
        examCompetition = examCompetition.replaceAll("Candidato/Vagas: \n", "Candidato/Vagas: ");
        examCompetition = examCompetition.replaceAll(" \n", ", ");
        examCompetition = examCompetition.replaceAll("Cosista", "Cotista");
        String examInfoString = getString(R.string.home_field_title_exam_name) + " " + exam.examName;
        if (examType.equals("PAS")) {
            examInfoString +=
                    "\n" + getString(R.string.home_field_title_exam_phase) + " " + exam.examPhase +
                            "\n" + getString(R.string.home_field_title_exam_date) + " " + exam.examDate1.replaceAll("PROVA 1:", "");
            if (exam.examPhase.equals("3")) {
                examInfoString +=
                        "\n" + getString(R.string.home_field_title_exam_discipline_1) + " " + exam.examDiscipline1 +
                                "\n" + getString(R.string.home_field_title_exam_discipline_2) + " " + exam.examDiscipline2 +
                                "\n" + getString(R.string.home_field_title_exam_course) + " " + exam.examCourse;
            }
        } else {
            examInfoString +=
                    "\n" + getString(R.string.home_field_title_exam_date_1) + " " + exam.examDate1.replaceAll("PROVA 1:", "") +
                            "\n" + getString(R.string.home_field_title_exam_date_2) + " " + exam.examDate2.replaceAll(" PROVA 2:", "") +
                            "\n" + getString(R.string.home_field_title_exam_date_3) + " " + exam.examDate3.replaceAll(" PROVA 3:", "") +
                            "\n" + getString(R.string.home_field_title_exam_discipline_1) + " " + exam.examDiscipline1 +
                            "\n" + getString(R.string.home_field_title_exam_discipline_2) + " " + exam.examDiscipline2 +
                            "\n" + getString(R.string.home_field_title_exam_course) + " " + exam.examCourse;
        }
        examInfoString += "\n" + getString(R.string.home_field_title_exam_exam_foreign_language) + " " + exam.examForeignLanguage;
        if (!exam.examCompetition.equals("")) {
            examInfoString += "\n" + getString(R.string.home_field_title_exam_competition) + " " + examCompetition;
        }

        titleList.add(getString(R.string.home_card_title_exam_info));
        contentList.add(examInfoString);


        if (miscellaneous.miscellaneousInfoAvailabilityExamLocation.equals("S")
                || !examLocation.examLocationCity.equals("-")) {
            String examLocationInfoString = getString(R.string.home_field_title_exam_location_headquarters) + " " + examLocation.examLocationHeadquarters +
                    "\n" + getString(R.string.home_field_title_exam_location_city) + " " + examLocation.examLocationCity +
                    "\n" + getString(R.string.home_field_title_exam_location_street) + " " + examLocation.examLocationStreet +
                    "\n" + getString(R.string.home_field_title_exam_location_block) + " " + examLocation.examLocationBlock +
                    "\n" + getString(R.string.home_field_title_exam_location_room) + " " + examLocation.examLocationRoom;

            titleList.add(getString(R.string.home_card_title_exam_location_info));
            contentList.add(examLocationInfoString);

        }
        Timber.d("CD COMPOSING is " + miscellaneous.miscellaneousCdComposing);


        if (miscellaneous.miscellaneousInfoAvailabilityPerformance.equals("S")) {
            String pointsInfoString = "";
            if (exam.examType.equals("PAS")) {
                pointsInfoString += getString(R.string.home_field_title_points_total_stage_1) + " " + score.scoreTotalStage1;
                if (exam.examPhase.equals("2")) {
                    pointsInfoString += "\n" + getString(R.string.home_field_title_points_total_stage_2) + " " + score.scoreTotalStage2;
                } else if (exam.examPhase.equals("3")) {
                    pointsInfoString += "\n" + getString(R.string.home_field_title_points_total_stage_2) + " " + score.scoreTotalStage2 + "\n" + getString(R.string.home_field_title_points_total_stage_final) + " " + score.scoreTotalStageFinal;
                }
            } else {
                pointsInfoString +=
                        getString(R.string.home_field_title_points_total_stage_1) + " " + score.scoreTotalStage1 +
                                "\n" + getString(R.string.home_field_title_points_total_stage_2) + " " + score.scoreTotalStage2 +
                                "\n" + getString(R.string.home_field_title_points_total_stage_final) + " " + score.scoreTotalStageFinal;
            }

            pointsInfoString +=
                    "\n" + getString(R.string.home_field_title_points_score_general_knowledge) + " " + score.scoreGeneralKnowledge +
                            "\n" + getString(R.string.home_field_title_points_score_portuguese) + " " + score.scorePortuguese +
                            "\n" + getString(R.string.home_field_title_points_score_foreign_languages) + " " + score.scoreForeignLanguage +
                            "\n" + getString(R.string.home_field_title_points_score_composing) + " " + score.scoreComposing +
                            "\n" + getString(R.string.home_field_title_points_score_discipline_1, exam.examDiscipline1) + " " + score.scoreDiscipline1 +
                            "\n" + getString(R.string.home_field_title_points_score_discipline_2, exam.examDiscipline2) + " " + score.scoreDiscipline2;
            if (exam.examPhase.equals("3") || exam.examType.equals("VES")) {
                pointsInfoString +=
                        "\n" + getString(R.string.home_field_title_points_classification) + " " + score.scoreClassification +
                                "\n" + getString(R.string.home_field_title_points_approval_result) + " " + score.scoreApprovalResult;
            }

            titleList.add(getString(R.string.home_card_title_points_info));
            contentList.add(pointsInfoString);
        }

        recyclerViewAdapter.updateData(titleList, contentList);
        swipeRefresh.setRefreshing(false);
    }


    /**
     * Requests user's credentials
     */
    private void fetchNewData(){
        Auth.CredentialsApi.request(mGoogleApiClient, mCredentialRequest).setResultCallback(
                new ResultCallback<CredentialRequestResult>() {
                    @Override
                    public void onResult(@NonNull CredentialRequestResult credentialRequestResult) {
                        if(credentialRequestResult.getStatus().isSuccess()){
                            Credential credential = credentialRequestResult.getCredential();
                            signInWithPassword(credential.getId(), credential.getPassword());
                        }
                        else { resolveResult(credentialRequestResult.getStatus()); }
                    }
                }
        );
    }

    /**
     * Opens dialog so user can select account
     * @param status Credential Request Status
     */
    private void resolveResult(Status status) {
        if(isResolvingResult) return;
        if(status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED){
            try {
                isResolvingResult = true;
                status.startResolutionForResult(this, RC_READ);
            } catch (IntentSender.SendIntentException e) { e.printStackTrace(); }
        }
        else{
            Timber.d("resolve result, status is " + status);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) { startActivity(new Intent(getApplicationContext(), LoginActivity.class)); }
            };
            showSnackbar(getString(R.string.home_snackbar_unable_to_get_credentials), getString(R.string.home_snackbar_action_login_again), listener);
        }
    }

    /**
     * Result of dialog for the user to select account
     * @param requestCode Intent unique request code type
     * @param resultCode Whether or not approved
     * @param data Intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_READ){
            if(resultCode == RESULT_OK){
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                signInWithPassword(credential.getId(), credential.getPassword());
            }
            else {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { fetchNewData(); }
                };
                showSnackbar(getString(R.string.home_snackbar_unable_to_get_credentials), getString(R.string.home_snackbar_action_try_again), listener);
            }
        }
        isResolvingResult = false;
    }

    /**
     * Fetches data from server
     * Converts to UTF-8
     * @param registrationNumber User's registration number
     * @param password User's password
     */
    private void signInWithPassword(String registrationNumber, String password){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Uri candidatoUrl = Uri.parse("http://www.npd.uem.br/cvu/can_info.jsp").buildUpon()
                .appendQueryParameter("ins", Utils.getEncryptedRegistrationNum(registrationNumber))
                .appendQueryParameter("pas", password)
                .build();

        Request<JSONObject> candidatoRequest = new Request<JSONObject>(Request.Method.GET, candidatoUrl.toString(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { fetchNewData(); }
                };
                showSnackbar(getString(R.string.home_snackbar_an_error_occurred), getString(R.string.home_snackbar_action_try_again), clickListener);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                try {
                    //converts charset
                    String jsonString = new String(response.data, "ISO-8859-1");
                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                }
                catch (UnsupportedEncodingException | JSONException e) { e.printStackTrace(); }
                return null;
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                try {
                    if(Utils.getJsonString(response, "valido").equals("S")){
                        deleteAllTables();
                        storeJson(response);
                        populateAdapter();
                    }
                    else{
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) { startActivity(new Intent(getApplicationContext(), LoginActivity.class)); }
                        };
                        showSnackbar(getString(R.string.home_snackbar_invalid_credentials), getString(R.string.home_snackbar_action_login_again), listener);
                    }
                }
                catch (JSONException e) { e.printStackTrace(); }
            }
        };
        requestQueue.add(candidatoRequest);
    }

    /**
     * Displays a snackbar
     * @param message Snackbar's message
     * @param actionMessage Snackbar's action btn label
     * @param listener onClickListener for snackbar's btn
     */
    private void showSnackbar(String message, String actionMessage, View.OnClickListener listener){
        Snackbar snackbar = Snackbar.make(swipeRefresh, message, Snackbar.LENGTH_LONG);
        if(!actionMessage.equals("")){ snackbar.setAction(actionMessage, listener); }
        snackbar.show();
        swipeRefresh.setRefreshing(false);
    }

    /**
     * Deletes all db's tables
     */
    private void deleteAllTables(){
        getContentResolver().delete(Contract.ApplicantInfoEntry.CONTENT_URI, null, null);
        getContentResolver().delete(Contract.PaymentEntry.CONTENT_URI, null, null);
        getContentResolver().delete(Contract.ExamEntry.CONTENT_URI, null, null);
        getContentResolver().delete(Contract.ExamLocationEntry.CONTENT_URI, null, null);
        getContentResolver().delete(Contract.ScoreEntry.CONTENT_URI, null, null);
        getContentResolver().delete(Contract.MiscellaneousEntry.CONTENT_URI, null, null);
    }

    /**
     * Separates and Organizes the information into objects
     * Stores those objects to the db
     * @param jsonObject Json response from server
     */
    private void storeJson(JSONObject jsonObject){
        try {
            ApplicantInfo applicantInfo = new ApplicantInfo(jsonObject);
            Payment payment = new Payment(jsonObject);
            Exam exam = new Exam(jsonObject);
            ExamLocation examLocation = new ExamLocation(jsonObject);
            Score score = new Score(jsonObject);
            Miscellaneous miscellaneous = new Miscellaneous(jsonObject);

            getContentResolver().insert(
                    Contract.ApplicantInfoEntry.CONTENT_URI,
                    Utils.getContentValues(applicantInfo)
            );

            getContentResolver().insert(
                    Contract.PaymentEntry.CONTENT_URI,
                    Utils.getContentValues(payment)
            );

            getContentResolver().insert(
                    Contract.ExamEntry.CONTENT_URI,
                    Utils.getContentValues(exam)
            );

            getContentResolver().insert(
                    Contract.ExamLocationEntry.CONTENT_URI,
                    Utils.getContentValues(examLocation)
            );

            getContentResolver().insert(
                    Contract.ScoreEntry.CONTENT_URI,
                    Utils.getContentValues(score)
            );

            getContentResolver().insert(
                    Contract.MiscellaneousEntry.CONTENT_URI,
                    Utils.getContentValues(miscellaneous)
            );

        }
        catch (JSONException e) { e.printStackTrace(); }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

}
