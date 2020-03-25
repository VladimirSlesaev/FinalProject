package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import java.util.List;

public class ShowAllTrainersActivity extends AppCompatActivity {
    ListView TrainerListView;
    ProgressBar progressBar;
    List<String> IdList = new ArrayList<>();
    String EmailHolder;
    Button btnBack;
    Boolean checkEmptyList = true; // for check if list is empty
    int i; // global variable for for
    private String IdHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_trainers);

        TrainerListView = (ListView) findViewById(R.id.listview1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnBack=(Button)findViewById(R.id.buttonBack);
        EmailHolder = getIntent().getStringExtra("User");
        IdHolder = getIntent().getStringExtra("Pass");

        phpMethod(EmailHolder);




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowAllTrainersActivity.this, ShowAdminActivity.class);
                // Sending ListView clicked value using intent.
                intent.putExtra("User", EmailHolder);
                intent.putExtra("Pass", IdHolder);

                startActivity(intent);
                //Finishing current activity after open next activity.
                finish();



            }
        });

    }
    public void onBackPressed()
    {

    }

    public void phpMethod(String user) {
        BackgroundTaskMakeNotice backgroundTaskMakeNotice = new BackgroundTaskMakeNotice();
        backgroundTaskMakeNotice.execute(user);

        if (checkEmptyList) {
            TrainerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // TODO Auto-generated method stub
                    Intent intent = new Intent(ShowAllTrainersActivity.this, ShowSingleTrainerActivity.class);
                    // Sending ListView clicked value using intent.
                    intent.putExtra("ListViewValue", IdList.get(position).toString());
                    intent.putExtra("User",EmailHolder);
                    startActivity(intent);
                    //Finishing current activity after open next activity.
                    finish();

                }
            });
        }
    }


    class BackgroundTaskMakeNotice extends AsyncTask<String, Void, String> {

        String HttpUrl, user;
        String line = "";

        JSONArray jsonArray = null;

        List<Object> teamtList;

        @Override
        protected void onPreExecute() {
            HttpUrl = "https://netanel7.com//showAllTrainers.php";

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
                String data_string = URLEncoder.encode("User", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
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

                    Object object;

                    teamtList = new ArrayList<Object>();

                    for (i = 0; i < jsonArray.length(); i++) {
                        object = new Object();

                        jsonObject = jsonArray.getJSONObject(i);

                        // Adding object Id TO IdList Array.
                        IdList.add(jsonObject.getString("Id").toString());

                        //Adding object Name.
                        object.ObjectName = jsonObject.getString("Trainer_Name").toString();

                        teamtList.add(object);

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

            if (i == 0) // if i = 0 so list is empty
                checkEmptyList = false;
            if (checkEmptyList) {
                progressBar.setVisibility(View.GONE);
                TrainerListView.setVisibility(View.VISIBLE);
                ListAdapterClass adapter = new ListAdapterClass(teamtList, ShowAllTrainersActivity.this);
                TrainerListView.setAdapter(adapter);
            }
        }

    }


}
