package com.androidjson.serverupdate_androidjsoncom;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 *
 * Created by Vladimir Slesarev
 */
public class ShowSingleRecordTrainerActivity extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter team Data from Id Sent from previous activity.
    String HttpURL = "https://netanel7.com//TeamOpen.php";


    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String FinalJSonObject;
    TextView NAME, USER;
    String NameHolder, UserHolder, TeamIdHolder,EmailHolder;
    Button UpdateButton, DeleteButton, BackButton, PlayersButton;
    String TempItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_record_trainer);

        NAME = (TextView) findViewById(R.id.textName);
        USER = (TextView) findViewById(R.id.UserName);
        BackButton = (Button) findViewById(R.id.buttonBack);
        UpdateButton = (Button) findViewById(R.id.buttonUpdate);
        DeleteButton = (Button) findViewById(R.id.buttonDelete);
        PlayersButton = (Button) findViewById(R.id.buttonPlayers);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");
        EmailHolder = getIntent().getStringExtra("User");

        //Calling method to filter Object Record and open selected record.
        HttpWebCall(TempItem);


        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowSingleRecordTrainerActivity.this, ShowAllTeamsTrainerActivity.class);
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
    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowSingleRecordTrainerActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleRecordTrainerActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("TeamID", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }

    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
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

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Object Name, User  into Variables.
                            NameHolder = jsonObject.getString("name").toString();
                            UserHolder = jsonObject.getString("user").toString();


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

            // Setting team Name, User into TextView after done all process .
            NAME.setText(NameHolder);
            USER.setText(UserHolder);


        }
    }


    public void btnAttendance(View view) {

        Intent intent = new Intent(ShowSingleRecordTrainerActivity.this, AttendanceReportActivity.class);
        intent.putExtra("ListViewValue",TempItem);
        intent.putExtra("User", EmailHolder);

        startActivity(intent);
    }

    public void btnMessage(View view) {

        Intent intent = new Intent(ShowSingleRecordTrainerActivity.this, SendMessagesActivity.class);
        intent.putExtra("ListViewValue",TempItem);
        intent.putExtra("User", EmailHolder);
        startActivity(intent);
        finish();



    }


}