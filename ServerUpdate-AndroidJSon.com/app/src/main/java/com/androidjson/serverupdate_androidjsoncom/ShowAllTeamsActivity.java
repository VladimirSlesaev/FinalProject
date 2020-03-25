package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
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


public class ShowAllTeamsActivity extends AppCompatActivity {

    ListView TeamListView;
    ProgressBar progressBar;
    List<String> IdList = new ArrayList<>();
    String EmailHolder,TempItem,TempItem2,UserHolder,TrainerNameHolder;
    Boolean checkEmptyList = true; // for check if list is empty
    int i; // global variable for for


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_all_teams);

        TeamListView = (ListView) findViewById(R.id.listview1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        UserHolder = getIntent().getStringExtra("User2");// Trainer User (Mail)
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        TempItem2 = getIntent().getStringExtra("ListViewValue2");// Team Id


        // SharedPreferencesClass sharedPreferences = new SharedPreferencesClass(this);

        // if (EmailHolder!=null&&!EmailHolder.isEmpty()&&sharedPreferences.getMyEmail().equals("emptyValue")) {
        // sharedPreferences.setMyEmail(EmailHolder);
        phpMethod(UserHolder);
        //    Log.d("z", "onCreate: 1");
        // }
        //  else if(!sharedPreferences.getMyEmail().equals("emptyValue"))//   new GetHttpResponse(ShowAllTeamsActivity.this).execute();
        //  {
        //    phpMethod(sharedPreferences.getMyEmail());
        //     Log.d("z", "onCreate: 2");

        //  }

//Receiving the ListView Clicked item value send by previous activity.

        //Calling method to filter Object Record and open selected record.
        //Adding ListView Item click Listener.

    }
    public void onBackPressed()
    {

    }

    public void btnAddNewTeam(View view) {
        Intent intent = new Intent(ShowAllTeamsActivity.this, AddTeamActivity.class);
        intent.putExtra("User", EmailHolder);
        intent.putExtra("User2", UserHolder);
        intent.putExtra("ListViewValue", TempItem);
        startActivity(intent);
        finish();

    }


    public void phpMethod(String user) {
        BackgroundTaskMakeNotice backgroundTaskMakeNotice = new BackgroundTaskMakeNotice();
        backgroundTaskMakeNotice.execute(user);
        if (checkEmptyList){
            TeamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // TODO Auto-generated method stub
                    Intent intent = new Intent(ShowAllTeamsActivity.this, ShowSingleRecordActivity.class);
                    // Sending ListView clicked value using intent.
                    TempItem2= IdList.get(position).toString();// New

                    intent.putExtra("ListViewValue2",TempItem2);// New
                    intent.putExtra("User2", UserHolder);
                    intent.putExtra("ListViewValue", TempItem);
                    intent.putExtra("User", EmailHolder);

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
            HttpUrl = "https://netanel7.com//trainer.php";

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

                    for ( i = 0; i < jsonArray.length(); i++) {
                        object = new Object();

                        jsonObject = jsonArray.getJSONObject(i);

                        // Adding object Id TO IdList Array.
                        IdList.add(jsonObject.getString("id").toString());
//                        TeamIdHolder=jsonObject.getString("id").toString();
                        //Adding object Name.
                        object.ObjectName = jsonObject.getString("name").toString();

                        //if(EmailHolder.equals("vladi@gmail.com"))
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


            if(i==0) // if i = 0 so list is empty
                checkEmptyList=false;

            if (checkEmptyList)
            {
                progressBar.setVisibility(View.GONE);

                TeamListView.setVisibility(View.VISIBLE);

                ListAdapterClass adapter = new ListAdapterClass(teamtList, ShowAllTeamsActivity.this);

                TeamListView.setAdapter(adapter);
            }

        }

    }


    public void btnBack(View view) {
        Intent intent = new Intent(ShowAllTeamsActivity.this, ShowSingleTrainerActivity.class);
        // Sending ListView clicked value using intent.
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("trainerName", TrainerNameHolder);// TRAINER NAME
        startActivity(intent);
        //Finishing current activity after open next activity.
        finish();

    }


}


