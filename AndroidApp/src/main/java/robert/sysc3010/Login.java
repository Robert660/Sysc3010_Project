package robert.sysc3010;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity implements View.OnClickListener  {

    /*This is the main activity, enter the login details in the spaces, if
    an account hasn't been made then register 1st
     */

    Button bLogin,newReg,hidden;
    EditText etUsername, etPassword;
    int hiddenInc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        newReg = (Button) findViewById(R.id.newReg);
        hidden = (Button) findViewById(R.id.hidden);


        bLogin.setOnClickListener(this);
        newReg.setOnClickListener(this);
        hidden.setOnClickListener(this);






        }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogin://if it was the login button that was pressed
                userLogin(v);
                break;

            case R.id.newReg://if the new Register button was pressed
                startActivity(new Intent(this, Register.class));//Move to the register activity
                break;
            case R.id.hidden:
                hiddenInc++;
                if(hiddenInc>= 2){
                    hiddenInc=0;
                    Toast.makeText(this, "Hidden Found!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, HiddenChangeBT.class));


                }


        }
    }

    public void userLogin(View v){
        //This method runs the background task to search for the username in the database
        //then check the username to the etnered password.

        String uName = etUsername.getText().toString();
        String uPass = etPassword.getText().toString();
        if(uName.contains(";")||uName.contains("/")||uPass.contains(";")||uPass.contains("/")){

            Toast.makeText(this, "Invalid Characters", Toast.LENGTH_SHORT).show();
        }
        else {
            String method = "login";
            BackgroundTask bt = new BackgroundTask(this);
            bt.execute(method, uName, uPass);
            while (true) {
                if (bt.finished) {//TODO COME BACK TO HERE FOR COMMENTS
                    if (bt.check) {
                        startActivity(new Intent(this, AddContacts.class));
                        break;
                    }
                    break;

                }
            }
        }
    }


}
