package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlayerRegisterActivity extends AppCompatActivity {


    Button btn_sign;
    EditText player_id, player_fname, player_lname, phone;
    String TempItem2, TempItem, EmailHolder, UserHolder;
    Button ButtonBack;
    boolean existCharFName,existCharLName; // check if char exists in the name
    String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z";
    String[] partsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_register);

        btn_sign = (Button) findViewById(R.id.signup);

        player_id = (EditText) findViewById(R.id.playerId);
        player_fname = (EditText) findViewById(R.id.playerName);
        player_lname = (EditText) findViewById(R.id.playerLastName);
        phone = (EditText) findViewById(R.id.phone);
        ButtonBack = (Button) findViewById(R.id.btnBack);

        // Get Object id
        TempItem = getIntent().getStringExtra("ListViewValue");// User Pass (ID)
        EmailHolder = getIntent().getStringExtra("User");//   Admin User (Mail)
        TempItem2 = getIntent().getStringExtra("ListViewValue2");// Team Id
        UserHolder = getIntent().getStringExtra("User2");// Trainer User (Mail)

        partsNames=str.split(" ");



        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String player_id2 = player_id.getText().toString();
                String player_fname2 = player_fname.getText().toString();
                String player_lname2 = player_lname.getText().toString();
                String phone2 = phone.getText().toString();

                checkNameHasChar(player_fname2, player_lname2);

                if (player_id2.isEmpty() || existCharFName==false || existCharLName==false|| phone2.isEmpty())
                {

                    Toast.makeText(PlayerRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
                else
                    {
                    String player_name2=player_fname2+" "+player_lname2;
                    PlayerRegisterTask playerRegisterTask = new PlayerRegisterTask(getApplicationContext());
                    playerRegisterTask.execute("reg", player_id2, player_name2, TempItem2, phone2);
                }


            }
        });


        ButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerRegisterActivity.this, ShowSingleRecordActivity.class);

                intent.putExtra("ListViewValue", TempItem);// User Pass (ID)
                intent.putExtra("User", EmailHolder);//   Admin User (Mail)
                intent.putExtra("User2", UserHolder);// Trainer User (Mail)
                intent.putExtra("ListViewValue2", TempItem2);// Team Id
                startActivity(intent);

                // Finishing current activity after opening next activity.
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
                existCharFName=true; // if team name has a 1 char atleast

            if (playerLName.contains(partsNames[j]))
                existCharLName=true; // if team name has a 1 char atleast

        }

    }




    public void onBackPressed() {

    }
}
