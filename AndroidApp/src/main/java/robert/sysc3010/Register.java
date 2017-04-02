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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static java.security.AccessController.getContext;




public class Register extends AppCompatActivity implements View.OnClickListener {

    //TODO Make sure the user enters non-null values in both textfields(use toast)
    //TODO maybe make the password **** then have a 2nd box to confirm? not really required
    //TODO Search the DB to make sure the username isn't already in use.
    //TODO if the user doesn't allow internet access we should toast them and say they cannot register

    private static final String TAG= Register.class.getSimpleName();
    Button bRegister;
    EditText regUsername,regPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regUsername = (EditText) findViewById(R.id.regUsername);
        regPassword = (EditText) findViewById(R.id.regPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
    }

    /**
     * User register method calls background task which will create a new user
     * with the two textfields.
     */
    public void userRegister(View v){
        Log.d(TAG,"username ="+regUsername.getText().toString()+
                " \npassword="+regPassword.getText().toString());

        String uName = regUsername.getText().toString();
        String uPass = regPassword.getText().toString();
        String method = "register";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method,uName,uPass);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bRegister://if the register button was pressed
                userRegister(v);
                startActivity(new Intent(this, AddContacts.class));
                break;

        }
    }




}
