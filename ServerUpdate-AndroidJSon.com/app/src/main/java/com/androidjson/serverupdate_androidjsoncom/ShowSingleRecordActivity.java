package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class ShowSingleRecordActivity extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
        ProgressDialog pDialog;

    // Http Url For Filter team Data from Id Sent from previous activity.
    String HttpURL = "https://netanel7.com//TeamOpen.php";

    // Http URL for delete Already Open Object Record.
    String HttpUrlDeleteRecord = "https://netanel7.com//deleteTeam.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;
    TextView NAME,USER;
    String NameHolder, UserHolder,TeamIdHolder,EmailHolder,TempItem2,TrainerNameHolder;
    Button UpdateButton, DeleteButton, BackButton, PlayersButton;
    String TempItem,date;
    ProgressDialog progressDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_record)  ;

        NAME = (TextView)findViewById(R.id.textName);
        USER = (TextView)findViewById(R.id.UserName);
        BackButton = (Button)findViewById(R.id.buttonBack);
        UpdateButton = (Button)findViewById(R.id.buttonUpdate);
        DeleteButton = (Button)findViewById(R.id.buttonDelete);
        PlayersButton = (Button)findViewById(R.id.buttonPlayers);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        TempItem2 = getIntent().getStringExtra("ListViewValue2");// Team Id
        UserHolder = getIntent().getStringExtra("User2");// Trainer User (Mail)


        NameHolder = getIntent().getStringExtra("Name");//  Team Name

        //Calling method to filter Object Record and open selected record.
        HttpWebCall(TempItem2);




        PlayersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowSingleRecordActivity.this,ShowPlayersActivity.class);

                intent.putExtra("ListViewValue",TempItem);// User Pass (ID)
                intent.putExtra("User", EmailHolder);//   Admin User (Mail)
                intent.putExtra("User2", UserHolder);// Trainer User (Mail)
                intent.putExtra("ListViewValue2", TempItem2);// Team Id
                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });











        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowSingleRecordActivity.this,UpdateActivity.class);

                // Sending team Id, Name, User to next UpdateActivity.

                intent.putExtra("Name", NameHolder);
                intent.putExtra("ListViewValue2",TempItem2);// New
                intent.putExtra("User2", UserHolder);
                intent.putExtra("ListViewValue", TempItem);
                intent.putExtra("User", EmailHolder);

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });



        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling Object delete method to delete current record using Object ID.
                TeamDelete(TempItem2);
                Intent intent = new Intent(ShowSingleRecordActivity.this,ShowAllTeamsActivity.class);

                // Sending team Id, Name, User to next UpdateActivity.
                intent.putExtra("User", EmailHolder);//   Admin User (Mail)
                intent.putExtra("User2", UserHolder);// Trainer User (Mail)
                intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
                intent.putExtra("ListViewValue2", TempItem2);// Team Id


                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });





    }



    public void onBackPressed()
    {

    }


    public void btnAddPlayer(View view) {

        Intent intent = new Intent(ShowSingleRecordActivity.this, PlayerRegisterActivity.class);
        intent.putExtra("ListViewValue",TempItem);// User Pass (ID)
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue2", TempItem2);// Team Id
        startActivity(intent);
        finish();

    }


    public void btnShowPresence(View view) {


        Intent intent = new Intent(ShowSingleRecordActivity.this,ShowPresenceActivity.class);

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





    public void previous(View view) {
        Intent intent = new Intent(ShowSingleRecordActivity.this,ShowAllTeamsActivity.class);

        // Sending team Id.
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("ListViewValue2", TempItem2);// Team Id

        startActivity(intent);
        // Finishing current activity after opening next activity.
        finish();
        startActivity(intent);
        // Finishing current activity after opening next activity.
        finish();
    }

    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowSingleRecordActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleRecordActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("TeamID",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Object Name, User  into Variables.
                            NameHolder = jsonObject.getString("name").toString() ;
                            UserHolder = jsonObject.getString("user").toString() ;


                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            // Setting team Name, User into TextView after done all process .
            NAME.setText(NameHolder);
            USER.setText(UserHolder);


        }
    }


    public void btnSelectDate(View view) {

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
            Toast.makeText(ShowSingleRecordActivity.this, str1, Toast.LENGTH_LONG).show();


        }
    }


    // Parsing Complete JSON Object.




    // Method to Delete Object Record
    public void TeamDelete(final String TeamID) {

        class TeamDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(ShowSingleRecordActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(ShowSingleRecordActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending team id.
                hashMap.put("TeamID", params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        TeamDeleteClass teamDeleteClass = new TeamDeleteClass();

        teamDeleteClass.execute(TeamID);
    }

}