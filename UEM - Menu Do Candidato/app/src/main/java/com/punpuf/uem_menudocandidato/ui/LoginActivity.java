package com.punpuf.uem_menudocandidato.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
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

import timber.log.Timber;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private static final int RC_SAVE = 12;

    private GoogleApiClient mGoogleApiClient;
    private CredentialRequest mCredentialRequest;

    private ProgressBar progressBar;
    private TextInputLayout numberInputLayout;
    private TextInputLayout passwordInputLayout;

    private boolean isResolvingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Make forgot credentials tv clickable
        TextView forgotCredentialsBtn = (TextView) findViewById(R.id.login_forgot_pass_tv);
        forgotCredentialsBtn.setMovementMethod(LinkMovementMethod.getInstance());

        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        numberInputLayout = (TextInputLayout) findViewById(R.id.login_input_registration);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.login_input_password);

        Button loginBtn = (Button) findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApplicantsNumber();
                getApplicantsPassword();
                if(getApplicantsNumber() != null && getApplicantsPassword() != null){
                    signInWithPassword(false, getApplicantsNumber(), getApplicantsPassword());
                }
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mCredentialRequest = new CredentialRequest.Builder()
                .setPasswordLoginSupported(true)
                .build();

        Intent intent = getIntent();


        if(!intent.getBooleanExtra(HomeActivity.INTENT_ORIGIN_HOME_ACTIVITY, false)){
            Timber.d("Did not come from home activity");
            requestUserCredentials();
        }


    }
    /**
     * gets applicant's number
     * @return number if valid
     */
    private String getApplicantsNumber(){
        if(numberInputLayout.getEditText() != null) {
            String registrationNumberCredential = numberInputLayout.getEditText().getText().toString();
            if(registrationNumberCredential.length() != 6 &&
                    registrationNumberCredential.length() != 10){
                numberInputLayout.setError(getString(R.string.login_error_invalid_number));
            }
            else{
                numberInputLayout.setErrorEnabled(false);
                return registrationNumberCredential;
            }
        }
        return null;
    }

    /**
     * Gets applicant's password
     * @return password if valid
     */
    private String getApplicantsPassword(){
        if(passwordInputLayout.getEditText() != null){
            String passwordCredential = passwordInputLayout.getEditText().getText().toString();
            if(passwordCredential.length() == 0){
                passwordInputLayout.setError(getString(R.string.login_error_invalid_password));
            }
            else{
                passwordInputLayout.setErrorEnabled(false);
                return passwordCredential;
            }
        }
        return null;
    }

    /**
     * Requests Saved Credentials
     */
    private void requestUserCredentials(){
        Auth.CredentialsApi.request(mGoogleApiClient, mCredentialRequest).setResultCallback(
                new ResultCallback<CredentialRequestResult>() {
                    @Override
                    public void onResult(@NonNull CredentialRequestResult credentialRequestResult) {
                        if(credentialRequestResult.getStatus().isSuccess()){
                            onCredentialRetrieved(credentialRequestResult.getCredential());
                            Timber.d("requestUserCredentials --> successful credential request ");
                        }
                        else { resolveResult(credentialRequestResult.getStatus()); }
                    }
                }
        );
    }

    private void onCredentialRetrieved(Credential credential){
        signInWithPassword(true, credential.getId(), credential.getPassword());
    }

    /**
     * Requests permission to save if credential is new or if multiple users
     * @param status of the credential save attempt
     */
    private void resolveResult(Status status) {
        //prevents multiple resolution and stacked dialogs
        if(isResolvingResult) return;
        if(status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED){
            try {
                Timber.d("resolveResult --> status:" + status);
                status.startResolutionForResult(this, RC_SAVE);
                isResolvingResult = true;
            } catch (IntentSender.SendIntentException e) {  e.printStackTrace(); }
        }
    }

    /**
     * 
     * @param requestCode intent unique request code type
     * @param resultCode whether or not approved
     * @param data intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("onActivityResult --> successful credential request result ok ?" + (resultCode == RESULT_OK));
        if(requestCode == RC_SAVE){
            if(resultCode == RESULT_OK){
                Timber.d("onActivityResult --> result OK  ");
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                onCredentialRetrieved(credential);
            }
        }
        isResolvingResult = false;
    }

    /**
     * Fetches data from UEM's servers using applicant's credentials
     * @param usedSmartLock whether Smart Lock was used or not
     * @param registrationNumber applicants registration number
     * @param password applicants registration password
     */
    private void signInWithPassword(final boolean usedSmartLock, String registrationNumber, String password){
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Uri candidatoUrl = Uri.parse("http://www.npd.uem.br/cvu/can_info.jsp").buildUpon()
                .appendQueryParameter("ins", Utils.getEncryptedRegistrationNum(registrationNumber))
                .appendQueryParameter("pas", password)
                .build();

        Uri boletoUrl = Uri.parse("http://www.npd.uem.br/cvu/boletojson.jsp").buildUpon()
                .appendQueryParameter("ins", registrationNumber)
                .appendQueryParameter("pas", password)
                .build();

        JsonObjectRequest boletoRequest = new JsonObjectRequest(Request.Method.GET, boletoUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        try { preferences.edit().putString(getString(R.string.pref_boleto_link), response.getString("url")).apply(); }
                        catch (JSONException e) { e.printStackTrace(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Request<JSONObject> candidatoRequest = new Request<JSONObject>(Request.Method.GET, candidatoUrl.toString(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.login_constraint), R.string.login_network_request_error, Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(GONE);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                try {
                    //converts charset from latin-1 to UTF-8
                    String jsonString = new String(response.data, "ISO-8859-1");
                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                }
                catch (UnsupportedEncodingException | JSONException e) { e.printStackTrace(); }
                return null;
            }

            //response is delivered to main thread
            @Override
            protected void deliverResponse(JSONObject response) {
                try {
                    Timber.d("get json string from utils: " + Utils.getJsonString(response, "valido"));
                    if(Utils.getJsonString(response, "valido").equals("S")){
                        progressBar.setVisibility(GONE);
                        storeJson(response);
                        onSuccessfulSignIn(usedSmartLock);
                    }
                    else{
                        Snackbar.make(findViewById(R.id.login_constraint), R.string.login_error_invalid_credentials, Snackbar.LENGTH_LONG).show();
                        progressBar.setVisibility(GONE);
                    }
                }
                catch (JSONException e) { e.printStackTrace(); }
            }
        };
        requestQueue.add(boletoRequest);
        requestQueue.add(candidatoRequest);
    }


    /**
     * Json response is processed and saved to database
     * @param jsonObject network json response
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

    private void onSuccessfulSignIn(boolean usedSmartLock){
        if(!usedSmartLock){ storeUsersCredentials(); }
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.INTENT_ORIGIN_LOGIN_ACTIVITY, true);
        startActivity(intent);
    }

    /**
     * Starts process to store user's credentials
     */
    private void storeUsersCredentials(){
        if(numberInputLayout.getEditText() != null && passwordInputLayout.getEditText() != null) {
            String registrationNumber = numberInputLayout.getEditText().getText().toString();
            String password = passwordInputLayout.getEditText().getText().toString();
            Credential credential = new Credential.Builder(registrationNumber)
                    .setPassword(password)
                    .build();
            if(mGoogleApiClient.isConnected()) {
                Auth.CredentialsApi.save(mGoogleApiClient, credential).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                resolveResult(status);
                            }
                        }
                );
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

}
