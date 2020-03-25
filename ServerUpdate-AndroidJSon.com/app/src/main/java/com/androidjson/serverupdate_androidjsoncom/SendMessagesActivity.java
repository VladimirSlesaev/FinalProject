package com.androidjson.serverupdate_androidjsoncom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class SendMessagesActivity extends AppCompatActivity {

    EditText txtMessage;
    ArrayList<String> phones = new ArrayList<String>();
    String TempItem,EmailHolder;
    Button ButtonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messages);

        txtMessage = (EditText) findViewById(R.id.txtMessage);

        TempItem = getIntent().getStringExtra("ListViewValue");
        EmailHolder = getIntent().getStringExtra("User");

        ButtonBack=(Button)findViewById(R.id.btnBack);

        ButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendMessagesActivity.this, ShowSingleRecordTrainerActivity.class);
                intent.putExtra("ListViewValue",TempItem);
                intent.putExtra("User", EmailHolder);
                startActivity(intent);
                finish();

            }
        });

    }

    public void onBackPressed()
    {

    }

    public void btnSms(View view) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            phpMethod(TempItem);
            MyMessage();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }


    }

    private void MyMessage() {
        String phoneNumber;
        String message = txtMessage.getText().toString().trim();

        for (int i = 0; i < phones.size(); i++) {

            phoneNumber = phones.get(i);
            if (!txtMessage.getText().toString().equals("")) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please Enter Message", Toast.LENGTH_SHORT).show();

            }
        }


    }


    public void phpMethod(String team_id) {
        BackgroundTaskMakeNotice backgroundTaskMakeNotice = new BackgroundTaskMakeNotice();
        backgroundTaskMakeNotice.execute(team_id);
    }


    class BackgroundTaskMakeNotice extends AsyncTask<String, Void, String> {

        String HttpUrl, team_id;
        String line = "";

        JSONArray jsonArray = null;

        @Override
        protected void onPreExecute() {
                HttpUrl ="https://netanel7.com//sendSms.php";

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                team_id = args[0];
                Log.d("nati", team_id);
                URL url = new URL(HttpUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("TeamID", "UTF-8") + "=" + URLEncoder.encode(team_id, "UTF-8");
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
                        phones.add(jsonObject.getString("Phone").toString());

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
