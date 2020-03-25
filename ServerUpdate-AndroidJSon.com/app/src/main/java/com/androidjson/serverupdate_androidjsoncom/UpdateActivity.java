package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateActivity extends AppCompatActivity {

    String HttpURL = "https://netanel7.com//updateTeam.php";
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText Name, User;
    Button UpdateTeam;
    String IdHolder;
    String TempItem, EmailHolder, TempItem2, UserHolder, NameHolder, SaveNameHolder;
    List<String> IdList = new ArrayList<>();
    boolean existChar; // check if char exists
    String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z";
    String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Name = (EditText) findViewById(R.id.editName);
        User = (EditText) findViewById(R.id.editUser);


        UpdateTeam = (Button) findViewById(R.id.UpdateButton);

        // Receive team ID, Name , User Name Send by previous ShowSingleRecordActivity.

        IdHolder = getIntent().getStringExtra("Id");

        SaveNameHolder=NameHolder = getIntent().getStringExtra("Name"); // Team Name
        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        EmailHolder = getIntent().getStringExtra("User"); //   Admin User (Mail)
        TempItem2 = getIntent().getStringExtra("ListViewValue2"); // Team Id
        UserHolder = getIntent().getStringExtra("User2"); // Trainer User (Mail)

        // Setting Received Object Name, User Name to EditText.
        Name.setText(NameHolder);
        User.setText(UserHolder);
        parts=str.split(" ");


        // Adding click listener to update button .
        UpdateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                checkNameHasChar(NameHolder);

                if(existChar==false||UserHolder.isEmpty())
                {
                    NameHolder=SaveNameHolder;
                    Toast.makeText(UpdateActivity.this, "Please fill name of Object.", Toast.LENGTH_LONG).show();

                }

                else
                {

                    // Sending team Name, user to method to update on server.
                    TeamRecordUpdate(TempItem2, NameHolder, UserHolder);
                    phpMethod(UserHolder); // Method that selsect New Trainer Name by The User_Name (Mail)
                }



            }
        });


    }



    // func that check if Name of team has a char
    public void checkNameHasChar(String teamName)
    {

        existChar=false;// initialization

        for (int j = 0; j < parts.length; j++)
        {
            if (teamName.contains(parts[j]))
                existChar=true; // if team name has a 1 char atleast
        }

    }



    public void onBackPressed()
    {

    }

    public void previous(View view) {
        Intent intent = new Intent(UpdateActivity.this, ShowSingleRecordActivity.class);

        // Sending team Id, Name, User to next UpdateActivity.

        intent.putExtra("Name", NameHolder); //  Team Name
        intent.putExtra("ListViewValue2", TempItem2);// Team Id
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)

        startActivity(intent);

        // Finishing current activity after opening next activity.
        finish();


    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText() {

        NameHolder = Name.getText().toString();
        UserHolder = User.getText().toString();


    }


    // Method to Update Object Record.
    public void TeamRecordUpdate(final String Id, final String Name, final String User) {

        class StudentRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("Id", params[0]);

                hashMap.put("Name", params[1]);

                hashMap.put("User", params[2]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(Id, Name, User);
    }


    public void phpMethod(String user) {
        BackgroundTaskMakeNotice backgroundTaskMakeNotice = new BackgroundTaskMakeNotice();
        backgroundTaskMakeNotice.execute(user);
    }


    class BackgroundTaskMakeNotice extends AsyncTask<String, Void, String> {

        String HttpUrl, user;
        String line = "";

        JSONArray jsonArray = null;

        @Override
        protected void onPreExecute() {
            HttpUrl = "https://netanel7.com//findTrainerNameByUser.php";

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                user = args[0];
                Log.d("nati", user);
                URL url = new URL(HttpUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader x = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuffer sb = new StringBuffer("");

                String l = "";
                //String nl=System.getProperty("line.separator");
                while ((l = x.readLine()) != null) {
                    sb.append(l + "\n");
                }
                line = sb.toString();
                Log.d("lineCheck", line);
                inputStream.close();
                x.close();


                try {
                    jsonArray = new JSONArray(line);

                    JSONObject jsonObject;


                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        // Adding object Id TO IdList Array.
                        IdList.add(jsonObject.getString("Id").toString());
                        TempItem= IdList.get(i).toString();

                    }
                } catch (JSONException e) {
                    Log.d("NotHasSeenOfferCatch", e.getMessage().toString());

                    //  Toast.makeText(mCtx, "נפילההה", Toast.LENGTH_SHORT).show();

                }

                inputStream.close();
                httpURLConnection.disconnect();


                return "Success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Something Wrong";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {


        }

    }


}