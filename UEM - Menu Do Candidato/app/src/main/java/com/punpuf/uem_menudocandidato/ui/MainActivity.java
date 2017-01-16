package com.punpuf.uem_menudocandidato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.punpuf.uem_menudocandidato.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if(!databaseHelper.isDatabaseEmpty()){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(HomeActivity.INTENT_ORIGIN_MAIN_ACTIVITY, true);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
