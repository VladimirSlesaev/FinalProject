package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowAdminActivity extends AppCompatActivity {

    Button btnShowTrainer,btnNewTrainer,btnEditDetails,btnBack;
    String EmailHolder,IdHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_admin);

        EmailHolder = getIntent().getStringExtra("User");
        IdHolder = getIntent().getStringExtra("Pass");

        btnShowTrainer=(Button)findViewById(R.id.buttonShowTrainers);
        btnNewTrainer=(Button)findViewById(R.id.btnAddTrainer);
        btnEditDetails=(Button)findViewById(R.id.editUserName);
        btnBack=(Button)findViewById(R.id.buttonBack);


        btnShowTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAdminActivity.this, ShowAllTrainersActivity.class);
                intent.putExtra("User",EmailHolder);
                intent.putExtra("Pass",IdHolder); //--
                startActivity(intent);
                finish();

            }
        });



        btnNewTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowAdminActivity.this, TrainerRegisterActivity.class);
                intent.putExtra("User",EmailHolder);
                intent.putExtra("Pass",IdHolder);
                startActivity(intent);
                finish();

            }
        });


        btnEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowAdminActivity.this, UpdateAdminActivity.class);
                intent.putExtra("User",EmailHolder);
                intent.putExtra("Pass",IdHolder);
                startActivity(intent);
                finish();

            }
        });






        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAdminActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });


    }
    public void onBackPressed()
    {

    }

}
