
package com.androidjson.serverupdate_androidjsoncom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

public class UpdateTrainerActivity extends AppCompatActivity {

    Button btnUpdate, btnBack;
    String IdHolder, TrainerNameHolder, UserNameHolder, EmailHolder;
    EditText FName, LName, Id;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://netanel7.com//updateTrainer.php";
    String[] parts;// divid trainer name by " "
    String FirstName, LastName,SaveIdHolder;
    boolean existCharFName,existCharLName; // check if char exists in the name
    String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z";
    String[] partsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trainer);

        FName = (EditText) findViewById(R.id.editFName);
        LName = (EditText) findViewById(R.id.editLName);
        Id = (EditText) findViewById(R.id.editUser);

        SaveIdHolder=IdHolder = getIntent().getStringExtra("Id");
        TrainerNameHolder = getIntent().getStringExtra("Trainer_Name");
        UserNameHolder = getIntent().getStringExtra("User_Name");
        EmailHolder = getIntent().getStringExtra("User");
        //  intent.putExtra("User",EmailHolder);

        parts = TrainerNameHolder.split(" ");
        partsNames=str.split(" ");

        FName.setText(parts[0]);
        LName.setText(parts[1]);
        Id.setText(IdHolder);


        btnUpdate = (Button) findViewById(R.id.UpdateButton);
        btnBack = (Button) findViewById(R.id.buttonBack);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Getting data from EditText after button click.
                GetDataFromEditText();
                checkNameHasChar(FirstName, LastName);

                if (UserNameHolder.isEmpty() || existCharFName==false ||existCharLName==false|| IdHolder.isEmpty())
                {
                    IdHolder=SaveIdHolder;// Back To Original Id For Back In pages

                    Toast.makeText(UpdateTrainerActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
                else
                    {
                        // Sending team Name, user to method to update on server.
                        TrainerNameHolder = FirstName+" "+LastName;
                        TrainerRecordUpdate(UserNameHolder, TrainerNameHolder, IdHolder);
                }


            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpdateTrainerActivity.this, ShowSingleTrainerActivity.class);
                // Sending ListView clicked value using intent.
                intent.putExtra("User", EmailHolder);
                intent.putExtra("ListViewValue", IdHolder);

                startActivity(intent);
                //Finishing current activity after open next activity.
                finish();


            }
        });
    }



    // func that check if Name of player has a char
    public void checkNameHasChar(String trainerFName,String trainerLName)
    {

        existCharFName=false;// initialization
        existCharLName=false;// initialization

        for (int j = 0; j < partsNames.length; j++)
        {
            if (trainerFName.contains(partsNames[j]))
                existCharFName=true; // if player first name has a 1 char atleast

            if (trainerLName.contains(partsNames[j]))
                existCharLName=true; // if player last name has a 1 char atleast

        }

    }


    public void onBackPressed() {

    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText() {

        FirstName = FName.getText().toString();
        LastName = LName.getText().toString();
        IdHolder = Id.getText().toString();

    }


    public void TrainerRecordUpdate(final String TrainerId, final String TrainerName, final String UserName) {

        class TrainerRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateTrainerActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateTrainerActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override // SENDING TO PHP
            protected String doInBackground(String... params) {

                hashMap.put("User_Name", params[0]);

                hashMap.put("Trainer_Name", params[1]);

                hashMap.put("Trainer_Id", params[2]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        TrainerRecordUpdateClass trainerRecordUpdateClass = new TrainerRecordUpdateClass();

        trainerRecordUpdateClass.execute(TrainerId, TrainerName, UserName);
    }

}
