package robert.sysc3010;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin,newReg;
    EditText etUsername, etPassword;
    UserStorage userStorage;



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
        userStorage = new UserStorage(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogin://if it was the login button that was pressed
                User user = new User(null,null);

                userStorage.storeUserData(user);
                userStorage.setUserLoggedIn(true);
                break;

            case R.id.newReg://if the new Register button was pressed
                startActivity(new Intent(this, Register.class));//TODO for testing it goes straight to GPS Activity


                break;


        }

    }
}
