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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

public class UpdatePlayerActivity extends AppCompatActivity {

    String IdHolder,PlayerNameHolder,PhoneHolder,TempItem,TempItemId,EmailHolder;
    EditText FName,LName, Phone;
    Button UpdatePlayer,btnBack;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;
    String TempItem2,TempItem3,UserHolder;
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://netanel7.com//updatePlayer.php";
    String[] parts;// divid player name by " "
    String FirstName, LastName;
    String SavePlayerNameHolder,SavePhoneHolder; // Save that original values
    boolean existCharFName,existCharLName; // check if char exists in the name
    String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z";
    String[] partsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_player);

        FName = (EditText) findViewById(R.id.editFName);
        LName = (EditText) findViewById(R.id.editLName);
        Phone = (EditText) findViewById(R.id.editUser);
        UpdatePlayer = (Button) findViewById(R.id.UpdateButton);
        btnBack = (Button) findViewById(R.id.buttonBack);

        // Receive team ID, Name , User Name Send by previous ShowSingleRecordActivity.
        SavePlayerNameHolder=PlayerNameHolder = getIntent().getStringExtra("Player_Name");
        SavePhoneHolder=PhoneHolder = getIntent().getStringExtra("Phone");
        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        TempItem2 = getIntent().getStringExtra("ListViewValue2");// Team Id
        TempItem3 = getIntent().getStringExtra("ListViewValue3");//  Player Id
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        UserHolder = getIntent().getStringExtra("User2");// Trainer User (Mail)

        parts=PlayerNameHolder.split(" ");
        partsNames=str.split(" ");



        FName.setText(parts[0]);
        LName.setText(parts[1]);
        Phone.setText(PhoneHolder);



        UpdatePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();
                checkNameHasChar(FirstName, LastName);

                if (PhoneHolder.isEmpty()||existCharFName==false||existCharLName==false)
                {
                    PlayerNameHolder=SavePlayerNameHolder;
                    PhoneHolder=SavePhoneHolder;

                    Toast.makeText(UpdatePlayerActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    PlayerNameHolder=FirstName+" "+LastName;

                    // Sending team Name, user to method to update on server.
                    PlayerRecordUpdate(TempItem3, PlayerNameHolder, PhoneHolder);
                }


            }
        });




        //
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePlayerActivity.this,ShowPlayerInfoActivity.class);

                intent.putExtra("ListViewValue3", TempItem3);
                intent.putExtra("Player_Name", PlayerNameHolder);
                intent.putExtra("Phone", PhoneHolder);
                intent.putExtra("User", EmailHolder);//   Admin User (Mail)
                intent.putExtra("User2", UserHolder);// Trainer User (Mail)
                intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
                intent.putExtra("ListViewValue2", TempItem2); // Team Id
                intent.putExtra("ListViewValue3", TempItem3);//  Player Id
                startActivity(intent);
                finish();

            }
        });

    }


    // func that check if Name of player has a char
    public void checkNameHasChar(String playerFName,String playerLName)
    {

        existCharFName=false;// initialization
        existCharLName=false;// initialization

        for (int j = 0; j < partsNames.length; j++)
        {
            if (playerFName.contains(partsNames[j]))
                existCharFName=true; // if player first name has a 1 char atleast

            if (playerLName.contains(partsNames[j]))
                existCharLName=true; // if player last name has a 1 char atleast

        }

    }



    public void onBackPressed()
    {

    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText() {

        FirstName = FName.getText().toString();
        LastName = LName.getText().toString();
        PhoneHolder = Phone.getText().toString();


    }


    // Method to Update Object Record.
    public void PlayerRecordUpdate(final String PlayerId, final String PlayerName, final String Phone) {

        class PlayerRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdatePlayerActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdatePlayerActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("Player_Id", params[0]);

                hashMap.put("Player_Name", params[1]);

                hashMap.put("Phone", params[2]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        PlayerRecordUpdateClass playerRecordUpdateClass = new PlayerRecordUpdateClass();

        playerRecordUpdateClass.execute(PlayerId, PlayerName, Phone);
    }
}
