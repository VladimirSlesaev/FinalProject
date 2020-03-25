package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
public class UpdateAdminActivity extends AppCompatActivity {

    Button btnUpdate,BackButton;
    String IdHolder,UserNameHolder,SaveIdHolder,SaveUserNameHolder;
    EditText  User,EditID;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://netanel7.com//updateAdmin.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_admin);


         User = (EditText) findViewById(R.id.editUser);
         EditID= (EditText) findViewById(R.id.editId);

        SaveIdHolder=IdHolder = getIntent().getStringExtra("Pass");
        SaveUserNameHolder=UserNameHolder = getIntent().getStringExtra("User");

        User.setText(UserNameHolder);
        EditID.setText(IdHolder);
        // update edit text after editing the user

        btnUpdate=(Button)findViewById(R.id.UpdateButton);
        BackButton=(Button)findViewById(R.id.btnBack);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Getting data from EditText after button click.
                GetDataFromEditText();

                if(IdHolder.isEmpty()||UserNameHolder.isEmpty())
                {
                    IdHolder=SaveIdHolder; // Back To Original Id For Back In pages
                    UserNameHolder=SaveUserNameHolder;
                    Toast.makeText(UpdateAdminActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
                else {
                    // Sending team Name, user to method to update on server.
                    AdminRecordUpdate(IdHolder,UserNameHolder);

                }


            }
        });



        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpdateAdminActivity.this, ShowAdminActivity.class);
                intent.putExtra("User",UserNameHolder);
                intent.putExtra("Pass",IdHolder);
                startActivity(intent);
                finish();

            }
        });

    }

    public void onBackPressed()
    {

    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText() {

        UserNameHolder = User.getText().toString();
        IdHolder = EditID.getText().toString();
    }


    public void AdminRecordUpdate(final String TrainerId,final String UserName) {

        class AdminRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateAdminActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateAdminActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override // SENDING TO PHP
            protected String doInBackground(String... params) {

                hashMap.put("Trainer_Id", params[0]);
                hashMap.put("User_Name", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        AdminRecordUpdateClass adminRecordUpdateClass = new AdminRecordUpdateClass();

        adminRecordUpdateClass.execute(TrainerId,UserName);
    }
}
