package com.androidjson.serverupdate_androidjsoncom;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

/**
 *
 * Created by Vladimir Slesarev
 */
public class MainActivity extends AppCompatActivity {
    SharedPreferences shrd;
    SharedPreferences.Editor editor;

    EditText Email, Password;
    Button LogIn;
    String PasswordHolder, EmailHolder;
    String finalResult,userrole;
    String HttpURL = "https://netanel7.com//userLogin.php";
    String HttpURL2 = "https://netanel7.com//adminLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Spinner spinnerloginas;
    List<String> type_person;


    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        shrd = getSharedPreferences("save_info", Context.MODE_PRIVATE);
//        editor = shrd.edit();

       //final SharedPreferencesClass sharedPreferencesClass=new SharedPreferencesClass(MainActivity.this);

        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        spinnerloginas=(Spinner)findViewById(R.id.spinnerloginas);



        type_person = new ArrayList<String>();
        type_person.add("Admin");
        type_person.add("Trainer");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, type_person);
        // attaching data adapter to spinner
        spinnerloginas.setAdapter(dataAdapter);


        spinnerloginas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                ((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                userrole =(String) spinnerloginas.getSelectedItem();

            }



            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        LogIn = (Button)findViewById(R.id.login);

         LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                  //  if(sharedPreferencesClass.getMyEmail().equals("emptyValue"))
                    if(userrole.equals("Trainer"))
                    UserLoginFunction(EmailHolder, PasswordHolder);
                    else {
                        AdminLoginFunction(EmailHolder, PasswordHolder);
                    }
                }
                else {

                    Toast.makeText(MainActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    @Override
    public void onBackPressed()
    {

    }

// Check if text is not empty
    public void CheckEditTextIsEmptyOrNot(){

        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    // func for Trainer Login
    public void UserLoginFunction(final String email, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                        Intent intent = new Intent(MainActivity.this, ShowAllTeamsTrainerActivity.class);
                        intent.putExtra("User",EmailHolder);
                        startActivity(intent);
                        finish();



                }
                else{

                    Toast.makeText(MainActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override// Sending To php code
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email,password);
    }




// Func for Admin Login
    public void AdminLoginFunction(final String email, final String password){

        class AdminLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){


                    Intent intent = new Intent(MainActivity.this, ShowAdminActivity.class);

                    // intent.putExtra(UserEmail,email);

                    intent.putExtra("User",EmailHolder);
                    intent.putExtra("Pass",PasswordHolder);

                    startActivity(intent);
                    finish();




                }
                else{

                    Toast.makeText(MainActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL2);

                return finalResult;
            }
        }

        AdminLoginClass adminLoginClass = new AdminLoginClass();

        adminLoginClass.execute(email,password);
    }





}