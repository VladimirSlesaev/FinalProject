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

public class TrainerRegisterActivity extends AppCompatActivity {
    Button btn_sign, BackButton;
    EditText trainer_id, trainer_Fname,trainer_Lname, user_name;
    String EmailHolder, IdHolder;
    boolean existCharFirstName,existCharLastName;// check if char exists in the name
    String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z";
    String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_register);

        EmailHolder = getIntent().getStringExtra("User");
        IdHolder = getIntent().getStringExtra("Pass");

        btn_sign = (Button) findViewById(R.id.signup);

        trainer_id = (EditText) findViewById(R.id.TrainerId);
        trainer_Fname = (EditText) findViewById(R.id.Trainer_FName);
        trainer_Lname= (EditText) findViewById(R.id.Trainer_LName);
        user_name = (EditText) findViewById(R.id.Trainer_User_Name);
        BackButton = (Button) findViewById(R.id.btnBack);

        parts=str.split(" ");


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String trainer_id2 = trainer_id.getText().toString();
                String trainer_Fname2 = trainer_Fname.getText().toString();
                String trainer_Lname2 = trainer_Lname.getText().toString();
                String user_name2 = user_name.getText().toString();

                checkNameHasChar(trainer_Fname2,trainer_Lname2);// check if name has chars


                if(trainer_id2.isEmpty()||existCharFirstName==false||existCharLastName==false||user_name2.isEmpty())
            {
                Toast.makeText(TrainerRegisterActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

            }
            else
            {
                String trainer_name2=trainer_Fname2+" "+trainer_Lname2;

                 TrainerRegisterTask backgroundTask3 = new TrainerRegisterTask(getApplicationContext());
                backgroundTask3.execute("reg", trainer_id2, trainer_name2, user_name2);
            }


            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrainerRegisterActivity.this, ShowAdminActivity.class);
                intent.putExtra("User", EmailHolder);
                intent.putExtra("Pass", IdHolder);
                startActivity(intent);
                finish();

            }
        });


    }




    // func that check if Name of player has a char
    public void checkNameHasChar(String trainerFName,String trainerLName)
    {
        existCharFirstName=false;// initialization
        existCharLastName=false;// initialization


        for (int j = 0; j < parts.length; j++)
        {
            if (trainerFName.contains(parts[j]))
                existCharFirstName=true; // if trainer First name has a 1 char atleast

            if (trainerLName.contains(parts[j]))
                existCharLastName=true; // if trainer Last name has a 1 char atleast

        }


    }



    public void onBackPressed()
    {

    }
}
