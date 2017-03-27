package robert.sysc3010;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //this is the mainActivity, for now login goes to straight to GPS for testing
    //Register will add the user to the database.

    Button bLogin,newReg;
    EditText etUsername, etPassword;
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
                startActivity(new Intent(this, GPS.class));//TODO for now it goes to GPS Activity
                break;

            case R.id.newReg://if the new Register button was pressed
                startActivity(new Intent(this, Register.class));
                break;


        }

    }
}
