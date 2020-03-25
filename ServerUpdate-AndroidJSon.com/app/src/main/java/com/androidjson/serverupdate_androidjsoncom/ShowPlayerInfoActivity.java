package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShowPlayerInfoActivity extends AppCompatActivity {

    String IdHolder,TempItem2,TempItem3;
    String HttpURL = "https://netanel7.com//PlayerOpen.php";
    String FinalJSonObject ;
    String PlayerNameHolder, PhoneHolder,EmailHolder,UserHolder;
    TextView Name,Phone;
    ProgressDialog pDialog;
    HashMap<String,String> ResultHash = new HashMap<>();
    String ParseResult ;
    HttpParse httpParse = new HttpParse();
    String TempItem;
    ProgressDialog progressDialog2;
    HashMap<String,String> hashMap = new HashMap<>();
    String finalResult ;
    String HttpUrlDeleteRecord = "https://netanel7.com//DeletePlayer.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_player_info);


        Name = (TextView)findViewById(R.id.textName);
        Phone = (TextView)findViewById(R.id.textPhoneNumber);

        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        TempItem2= getIntent().getStringExtra("ListViewValue2"); // Team Id
        TempItem3= getIntent().getStringExtra("ListViewValue3");//  Player Id
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        UserHolder= getIntent().getStringExtra("User2");// Trainer User (Mail)

        HttpWebCall(TempItem3);

    }







    public void onBackPressed()
    {

    }




    public void UpdatePlayer(View view) {
        Intent intent = new Intent(ShowPlayerInfoActivity.this,UpdatePlayerActivity.class);

        // Sending team Id, Name, User to next UpdateActivity.

        intent.putExtra("ListViewValue3", TempItem3);
        intent.putExtra("Player_Name", PlayerNameHolder);
        intent.putExtra("Phone", PhoneHolder);
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
        intent.putExtra("ListViewValue2", TempItem2); // Team Id
        intent.putExtra("ListViewValue3", TempItem3);//  Player Id


        startActivity(intent);

        // Finishing current activity after opening next activity.
        finish();
    }

    public void DeletePlayer(View view) {

        // Calling Student delete method to delete current record using Student ID.
        PlayerDelete(TempItem3);
        Intent intent = new Intent(ShowPlayerInfoActivity.this,ShowPlayersActivity.class);

        // Sending team Id, Name, User to next UpdateActivity.



        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
        intent.putExtra("ListViewValue2", TempItem2); // Team Id


        startActivity(intent);
        // Finishing current activity after opening next activity.
        finish();

    }





    public void PlayerDelete(final String PlayerID) {

        class PlayertDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(ShowPlayerInfoActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(ShowPlayerInfoActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending STUDENT id.
                hashMap.put("Player_Id", params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        PlayertDeleteClass playertDeleteClass = new PlayertDeleteClass();

        playertDeleteClass.execute(PlayerID);
    }




    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowPlayerInfoActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowPlayerInfoActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("PlayerID",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }

    public void btnBack(View view) {

        Intent intent = new Intent(ShowPlayerInfoActivity.this,ShowPlayersActivity.class);

        // Sending team Id, Name, User to next UpdateActivity.
        intent.putExtra("User", EmailHolder);//   Admin User (Mail)
        intent.putExtra("User2", UserHolder);// Trainer User (Mail)
        intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
        intent.putExtra("ListViewValue2", TempItem2); // Team Id
        intent.putExtra("ListViewValue3", TempItem3);//  Player Id

        startActivity(intent);
        // Finishing current activity after opening next activity.
        finish();
    }

    // Parsing Complete JSON Object.
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
                            IdHolder=jsonObject.getString("Player_Id").toString() ;
                            PlayerNameHolder = jsonObject.getString("Player_Name").toString() ;
                            PhoneHolder = jsonObject.getString("Phone").toString() ;


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
            Name.setText(PlayerNameHolder);
            Phone.setText(PhoneHolder);


        }
    }


}
