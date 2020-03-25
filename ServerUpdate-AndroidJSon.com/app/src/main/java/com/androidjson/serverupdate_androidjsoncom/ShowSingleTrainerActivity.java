package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowSingleTrainerActivity extends AppCompatActivity {

    String TempItem;
    String TrainerNameHolder, UserHolder,EmailHolder,TrainerNameHolder2;
    TextView NAME, USER;
    ProgressDialog pDialog;
    ArrayList<String> teamsId = new ArrayList<String>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://netanel7.com//TrainersOpen.php";
    String HttpURLDelete = "https://netanel7.com//deleteTrainer.php";
    String ParseResult;
    HashMap<String, String> ResultHash = new HashMap<>();
    String FinalJSonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_trainer);

        TempItem = getIntent().getStringExtra("ListViewValue");//Trainer Id // User Pass (ID)
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        UserHolder = getIntent().getStringExtra("User2");// Trainer User (Mail)


        NAME = (TextView) findViewById(R.id.textName);
        USER = (TextView) findViewById(R.id.textUser);

        HttpWebCall(TempItem);



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

                pDialog = ProgressDialog.show(ShowSingleTrainerActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleTrainerActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("TrainerID", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }

    public void btnBack(View view) {
        Intent intent = new Intent(ShowSingleTrainerActivity.this,ShowAllTrainersActivity.class);

         intent.putExtra("User", EmailHolder);

        startActivity(intent);

        // Finishing current activity after opening next activity.
        finish();


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
                            TrainerNameHolder = jsonObject.getString("Trainer_Name").toString();
                            UserHolder = jsonObject.getString("User_Name").toString();


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
            NAME.setText(TrainerNameHolder);
            USER.setText(UserHolder);


        }
    }


    public void UpdateTrainer(View view) {

        Intent intent = new Intent(ShowSingleTrainerActivity.this, UpdateTrainerActivity.class);

        // Sending team Id, Name, User to next UpdateActivity.
        intent.putExtra("Id", TempItem);
        intent.putExtra("Trainer_Name", TrainerNameHolder);
        intent.putExtra("User_Name", UserHolder);
        intent.putExtra("User",EmailHolder);

        startActivity(intent);

        // Finishing current activity after opening next activity.
        finish();
    }


    public void DeleteTrainer(View view) {

        HttpWebCallDelete(UserHolder);
        Intent intent = new Intent(ShowSingleTrainerActivity.this, ShowAllTrainersActivity.class);
        intent.putExtra("User",UserHolder);
        startActivity(intent);
        finish();

    }


    //Method to show current record Current Selected Record
    public void HttpWebCallDelete(final String PreviousListViewClickedItem) {

        class HttpWebCallFunction extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowSingleTrainerActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleTrainerActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("UserName", params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURLDelete);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }



    public void ShowInterface(View view) {
        Intent intent = new Intent(ShowSingleTrainerActivity.this, ShowAllTeamsActivity.class);

        intent.putExtra("User2",UserHolder);
        intent.putExtra("ListViewValue",TempItem);
        intent.putExtra("User",EmailHolder);
        startActivity(intent);
        finish();


    }



}
