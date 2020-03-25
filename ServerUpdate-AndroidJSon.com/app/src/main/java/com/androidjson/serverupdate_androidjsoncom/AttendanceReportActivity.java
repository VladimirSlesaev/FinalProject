package com.androidjson.serverupdate_androidjsoncom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

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

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 *
 * Created by Vladimir Slesarev
 */
public class AttendanceReportActivity extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    String TempItem,EmailHolder;


    ArrayList<String> selectedItems = new ArrayList<>();
    ArrayList<String>names=new ArrayList<>();
    ArrayList<String>names2;
    ListView chl;
    ArrayAdapter<String> adapter;
    Button ButtonBack;
    String date="Empty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");
        EmailHolder = getIntent().getStringExtra("User");
        ButtonBack=(Button)findViewById(R.id.buttonBack);



        chl = (ListView) findViewById(R.id.checkable_list);
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        phpMethod(TempItem);


        adapter = new ArrayAdapter<String>(this, R.layout.rowlayout, R.id.txt_lan,names);

        chl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem);// unChecked Item
                } else
                {
                    selectedItems.add(selectedItem);
                }
            }
        });





        ButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceReportActivity.this, ShowSingleRecordTrainerActivity.class);
                // Sending ListView clicked value using intent.
                intent.putExtra("ListViewValue",TempItem);
                intent.putExtra("User", EmailHolder);
                startActivity(intent);
                //Finishing current activity after open next activity.
                finish();
            }
        });


    }


    public void onBackPressed()
    {

    }

    public void presenceButton(View view) {

        if(date.equals("Empty"))
        {
            Toast.makeText(this,"Please Select Date ",Toast.LENGTH_LONG).show();
        }
        else {
            names2=new ArrayList<>();

            PresenceDeleteTask presenceDeleteTask = new PresenceDeleteTask(getApplicationContext());
            presenceDeleteTask.execute("del", TempItem, date);

            for (int j=0;j<selectedItems.size();j++){

                names2.add(selectedItems.get(j));// Adding to names2 what we checked
            }

            for(int i=0;i<names2.size();i++)
            {
                String str=names2.get(i); // All The String
                String [] parts=str.split(" ");// Divided
                String playerName=parts[0]+" "+parts[1];
                String playeIrd=parts[2];



                String type = "reg";
                PresenceRegisterTask presenceRegisterTask = new PresenceRegisterTask(getApplicationContext());
                presenceRegisterTask.execute(type, TempItem, playerName, date, playeIrd);

            }
        }





    }



// Save the selected date int string
    public void dateButton(View view) {

        Calendar systemCalender = Calendar.getInstance();
        int year = systemCalender.get(Calendar.YEAR);
        int month = systemCalender.get(Calendar.MONTH);
        int day = systemCalender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new SetDate(), year, month, day);
        datePickerDialog.show();

    }

    public class SetDate implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;

            String str1 = "You selected :" + dayOfMonth + "/" + monthOfYear + "/" + year;
            date = dayOfMonth + "/" + monthOfYear + "/" + year;
            Toast.makeText(AttendanceReportActivity.this, str1, Toast.LENGTH_LONG).show();


        }
    }


    public void phpMethod(String user) {
        BackgroundTaskMakeNotice backgroundTaskMakeNotice = new BackgroundTaskMakeNotice();
        backgroundTaskMakeNotice.execute(user);

    }


    class BackgroundTaskMakeNotice extends AsyncTask<String, Void, String> {

        String HttpUrl, user;
        String line = "";

        JSONArray jsonArray = null;

        List<Object> teamtList;

        @Override
        protected void onPreExecute() {
            HttpUrl = "https://netanel7.com//showPlayers.php";

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
                String data_string = URLEncoder.encode("TeamID", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
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

                        String NameId;

                        NameId=jsonObject.getString("Player_Name").toString();
                        NameId+=" ";
                        NameId+=jsonObject.getString("Player_Id").toString();
                        names.add(NameId);

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

//            ListView chl = (ListView) findViewById(R.id.checkable_list);
//            chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            chl.setAdapter(adapter);

        }

    }









}
