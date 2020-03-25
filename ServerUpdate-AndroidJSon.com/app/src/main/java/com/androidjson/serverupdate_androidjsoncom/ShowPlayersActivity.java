package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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


public class ShowPlayersActivity extends AppCompatActivity {
    ListView PlayerListView;
    ProgressBar progressBar;
    List<String> IdList = new ArrayList<>();
    String PlayerId, PlayerName, PhoneNumber,TempItem,EmailHolder,TempItem2,UserHolder;
    Boolean checkEmptyList = true; // for check if list is empty
    int i; // global variable for for

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_players);


        PlayerListView = (ListView) findViewById(R.id.listview1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        EmailHolder = getIntent().getStringExtra("User");
        UserHolder = getIntent().getStringExtra("User2");
        TempItem2= getIntent().getStringExtra("ListViewValue2");
        TempItem= getIntent().getStringExtra("ListViewValue");

        phpMethod(TempItem2);


    }
    public void onBackPressed()
    {

    }

    public void phpMethod(String user) {
        BackgroundTaskMakeNotice backgroundTaskMakeNotice = new BackgroundTaskMakeNotice();
        backgroundTaskMakeNotice.execute(user);

        if (checkEmptyList) {

            PlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // TODO Auto-generated method stub
                    Intent intent = new Intent(ShowPlayersActivity.this, ShowPlayerInfoActivity.class);
                    // Sending ListView clicked value using intent.
                    intent.putExtra("ListViewValue3", IdList.get(position).toString());//  Player Id
                    intent.putExtra("ListViewValue",TempItem);// User Pass (ID)
                    intent.putExtra("User", EmailHolder);//   Admin User (Mail)
                    intent.putExtra("User2", UserHolder);// Trainer User (Mail)
                    intent.putExtra("ListViewValue2", TempItem2);// Team Id

                    startActivity(intent);
                    //Finishing current activity after open next activity.
                    finish();

                }
            });
        }
    }

    public void previous(View view) {


        Intent intent = new Intent(ShowPlayersActivity.this,ShowSingleRecordActivity.class);

        // Sending team Id.

        intent.putExtra("ListViewValue",TempItem);// User Pass (ID)
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue2", TempItem2);// Team Id

        startActivity(intent);

        // Finishing current activity after opening next activity.
        finish();

    }


    class BackgroundTaskMakeNotice extends AsyncTask<String, Void, String> {

        String HttpUrl, team_id;
        String line = "";

        JSONArray jsonArray = null;

        List<Object> objectList;

        @Override
        protected void onPreExecute() {
            HttpUrl = "https://netanel7.com//showPlayers.php";

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

                    Object object;

                    objectList = new ArrayList<Object>();


                        for ( i = 0; i < jsonArray.length(); i++) {
                             object = new Object();

                            jsonObject = jsonArray.getJSONObject(i);
                            // Adding object Id TO IdList Array.
                            IdList.add(jsonObject.getString("Player_Id").toString());
                            if(i==0){
                            PlayerId = jsonObject.getString("Player_Id").toString();
                            PlayerName = jsonObject.getString("Player_Name").toString();
                            PhoneNumber = jsonObject.getString("Phone").toString();
                            }
                            //Adding object Name.
                            object.ObjectName = jsonObject.getString("Player_Name").toString();
                            object.ObjectName += "  ";
                            object.ObjectName += jsonObject.getString("Player_Id").toString();



                            //if(EmailHolder.equals("vladi@gmail.com"))
                            objectList.add(object);

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

            if (checkEmptyList) {
                progressBar.setVisibility(View.GONE);

                PlayerListView.setVisibility(View.VISIBLE);

                ListAdapterClass adapter = new ListAdapterClass(objectList, ShowPlayersActivity.this);

                PlayerListView.setAdapter(adapter);

            }


        }

    }
}
