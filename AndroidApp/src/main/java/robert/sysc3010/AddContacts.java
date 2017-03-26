package robert.sysc3010;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContacts extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG= AddContacts.class.getSimpleName();
    Context ctx;
    private Button next;
    private Button add;
    private EditText cName;
    private EditText pNum;
    private String[] contacts;
    private String[] phoneNumbers;
    private boolean retry;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        retry = false;
        ctx=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        next = (Button) findViewById(R.id.done);
        add = (Button) findViewById(R.id.add);
        cName = (EditText) findViewById(R.id.contactName);
        pNum = (EditText) findViewById(R.id.phoneNum);

        add.setOnClickListener(this);
        next.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add://if the add button was pressed
                validate(cName.getText().toString(), pNum.getText().toString());
                if (!retry) {
                    addNewNumber(cName.getText().toString(), pNum.getText().toString());
                }

            case R.id.done://TODO move to next activity

        }
    }


    public void validate(String inputName,String inputNumber){//TODO is there any other cases?
        Log.d(TAG,"validating Values");
        Log.d(TAG,"inputName="+ inputName + "\ninputNumber="+inputNumber );
        CharSequence okNumbers = "1234567890";
        CharSequence dash = "-";
        if(inputName==""||inputNumber==""){
            retry=true;
            Toast.makeText(ctx, "Try Again", Toast.LENGTH_LONG).show();
        }
        else if(inputNumber.contains(dash)){
            Toast.makeText(ctx, "- not Allowed", Toast.LENGTH_LONG).show();
            //Toast them to not enter dash
        }
        else if(!inputNumber.matches(".*\\d+.*")){
            //if it contains invalid values try again
            Toast.makeText(ctx, "Only numbers allowed", Toast.LENGTH_LONG).show();
            retry=true;
        }
        else if(inputNumber.length()!=10){
            Toast.makeText(ctx, "Invalid Phone Number", Toast.LENGTH_LONG).show();
            retry=true;
        }
        else{
            retry =false;
        }
    }


    public void addNewNumber(String inputName,String inputNumber){//Store phone number and contact name into arrays
        contacts[contacts.length]=inputName;
        phoneNumbers[phoneNumbers.length]=inputNumber;

    }

}
