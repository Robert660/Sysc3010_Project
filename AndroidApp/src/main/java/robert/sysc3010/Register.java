package robert.sysc3010;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener {

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


    public void userRegister(View v){
        String uName = regUsername.getText().toString();
        String uPass = regPassword.getText().toString();
        String method = "register";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method,uName,uPass);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bRegister://if the register button was pressed
                User user =new User(regUsername.getText().toString(),regPassword.getText().toString());//after registring user should move to next view

                break;

        }
    }
}
