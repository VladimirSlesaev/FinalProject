/**
 * Add Team Activity
 * Created by Vladimir Slesarev
 */
package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTeamActivity extends AppCompatActivity {
    String EmailHolder;
    EditText TeamName;
    String UserHolder, TempItem;
    boolean existChar; // check if char exists
    String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z";
    String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        EmailHolder = getIntent().getStringExtra("User");
        UserHolder = getIntent().getStringExtra("User2");
        TempItem = getIntent().getStringExtra("ListViewValue");
        TeamName = (EditText) findViewById(R.id.AddNewTeam);

        parts = str.split(" ");// divide every char to array

    }

    public void onBackPressed() {

    }

    public void btnAddNewTeam(View view) {

        String TeamName2 = TeamName.getText().toString();
        // Checking if the name has at least one character

        checkNameHasChar(TeamName2);

        if (existChar) {
            AddingTeam addingTeam = new AddingTeam(getApplicationContext());
            addingTeam.execute("reg", UserHolder, TeamName2);
        } else {
            Toast.makeText(AddTeamActivity.this, "Please fill name of Object.", Toast.LENGTH_LONG).show();

        }


    }

// func that check if Name of team has a char
    public void checkNameHasChar(String teamName)
    {

        existChar=false;// initialization

        for (int j = 0; j < parts.length; j++)
        {
            if (teamName.contains(parts[j]))
                existChar=true; // if team name has a 1 char atleast
        }

    }

// back to previous page
    public void Back(View view) {

        Intent intent = new Intent(AddTeamActivity.this, ShowAllTeamsActivity.class);
        intent.putExtra("User", EmailHolder);
        intent.putExtra("User2", UserHolder);
        intent.putExtra("ListViewValue", TempItem);
        startActivity(intent);
        finish();

    }
}
