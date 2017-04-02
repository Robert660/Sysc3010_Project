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

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity implements View.OnClickListener  {

    //this is the mainActivity, for now login goes to straight to GPS for testing
    //Register will add the user to the database.

    Button bLogin,newReg;
    EditText etUsername, etPassword;
    private Object lock = new Object();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        newReg = (Button) findViewById(R.id.newReg);

        bLogin.setOnClickListener(this);
        newReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogin://if it was the login button that was pressed
                //NotifyClass nc = new NotifyClass();
                //nc.showNotifications();
                userLogin(v);
                eventCheck();
                break;

            case R.id.newReg://if the new Register button was pressed
                showNotifications();

                startActivity(new Intent(this, Register.class));
                break;


        }
    }

    public void userLogin(View v){

        String uName = etUsername.getText().toString();
        String uPass = etPassword.getText().toString();
        String method = "login";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method,uName,uPass);

        while(true){
            if(bt.finished){
                Log.d("Login",""+bt.check);
                if(bt.check){
                    startActivity(new Intent(this, AddContacts.class));
                    break;
                }
                break;

            }
        }

    }


    public void eventCheck(){
        String method = "Event";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method);

        while(true){

            if(bt.checkDone){
                Log.d("Login",""+bt.check);
                if(bt.check){
                    startActivity(new Intent(this, AddContacts.class));
                    break;
                }
                break;

            }
        }


    }


    public void showNotifications(){//Creates a notification that leads to the NotifyClass when pushed

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.alert);
        builder.setContentTitle("Content Title");
        builder.setContentText("This is the content text");
        //when the notification is pressed
        Intent intent = new Intent(this,GPS.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotifyClass.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

    }


}
