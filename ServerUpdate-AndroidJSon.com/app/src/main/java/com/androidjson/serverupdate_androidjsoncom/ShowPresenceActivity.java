package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowPresenceActivity extends AppCompatActivity {
    ListView TeamListView;
    ProgressBar progressBar;
    List<String> IdList = new ArrayList<>();
    String date, DateHolder,EmailHolder,UserHolder,TempItem,TempItem2;

    String HttpURL = "https://netanel7.com//showPresence.php";
    String FinalJSonObject;
    ProgressDialog pDialog;
    HashMap<String, String> ResultHash = new HashMap<>();
    String ParseResult;
    HttpParse httpParse = new HttpParse();
    Boolean checkEmptyList = true; // for check if list is empty
    int i; // global variable for for

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_presence);

        TeamListView = (ListView) findViewById(R.id.listview1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DateHolder= getIntent().getStringExtra("Date"); // Get Date
        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        TempItem2 = getIntent().getStringExtra("ListViewValue2");// Team Id
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        UserHolder = getIntent().getStringExtra("User2");// Trainer User (Mail)
        date= getIntent().getStringExtra("Date");


        HttpWebCall(TempItem2,DateHolder);

    }


    public void onBackPressed()
    {

    }

    //Method to show current record Current Selected Record
    public void HttpWebCall(final String TeamId,final  String Date) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = ProgressDialog.show(ShowPresenceActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                pDialog.dismiss();
                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;
                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowPresenceActivity.this).execute();
            }
            @Override
            protected String doInBackground(String... params) {
                ResultHash.put("TEAM_ID", params[0]);
                ResultHash.put("DATE", params[1]);
                ParseResult = httpParse.postRequest(ResultHash, HttpURL);
                return ParseResult;
            }
        }
        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();
        httpWebCallFunction.execute(TeamId,Date);
    }

    public void btnBack(View view) {

        Intent intent = new Intent(ShowPresenceActivity.this,ShowSingleRecordActivity.class);

        // Sending team Id.
        intent.putExtra("ListViewValue", TempItem);
        intent.putExtra("User", EmailHolder);
        intent.putExtra("User2", UserHolder);
        intent.putExtra("ListViewValue2", TempItem2);
        intent.putExtra("Date", date);


        startActivity(intent);

        // Finishing current activity after opening next activity.
        finish();
    }


    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        List<Object> teamtList;
        public Context context;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (FinalJSonObject != null) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        Object object;
                        teamtList = new ArrayList<Object>();

                        for (i = 0; i < jsonArray.length(); i++) {
                            object =new Object();

                            jsonObject = jsonArray.getJSONObject(i);
                            IdList.add(jsonObject.getString("Presence_Id").toString());

                            object.ObjectName=jsonObject.getString("Player_Name").toString();

                            teamtList.add(object);

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(i==0) // if i = 0 so list is empty
                checkEmptyList=false;

            if (checkEmptyList)
            {
                progressBar.setVisibility(View.GONE);

                TeamListView.setVisibility(View.VISIBLE);

                ListAdapterClass adapter = new ListAdapterClass(teamtList, ShowPresenceActivity.this);

                TeamListView.setAdapter(adapter);
            }
        }
    }




}
