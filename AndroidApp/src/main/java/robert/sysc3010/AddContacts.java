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
    public Boolean firstTime = true;
    public static int size = 0;

    public String robs = "6138507623";
    public String bens = "6138056172";
    public String survesh = "6473829176";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contacts = new String[3];//max 3 contacts
        phoneNumbers = new String[3]; // Max 3 phone numbers
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
                else{
                    break;
                }
                if(firstTime) {
                    eventCheck();
                    Log.d(TAG, "evenCheck Done");
                }
                break;

            case R.id.done://if the continue button was pressed
                if(firstTime){
                    Toast.makeText(ctx, "Please enter atleast 1 contact", Toast.LENGTH_LONG).show();
                    break;
                }
                GPS myGPS = new GPS();
                myGPS.setName(contacts);
                myGPS.setNumbers(phoneNumbers);
                startActivity(new Intent(this, GPS.class));
                break;


        }
    }

    public void eventCheck(){
        //start checking for events

        firstTime = false;
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



    public void validate(String inputName,String inputNumber) {
        //Check that the input was legitimate

        CharSequence dash = "-";

        if (inputName == "" || inputNumber == "") {
            retry = true;
            Toast.makeText(ctx, "Try Again", Toast.LENGTH_LONG).show();
        } else if (inputNumber.equals("911")) {
            retry = true;
            Toast.makeText(ctx, "Nice Try;) We'd rather not bother them", Toast.LENGTH_LONG).show();
        } else if (inputNumber.contains(dash)) {
            retry = true;
            Toast.makeText(ctx, "- not Allowed", Toast.LENGTH_LONG).show();
            //Toast them to not enter dash
        } else if (inputNumber.length() != 10) {
            Toast.makeText(ctx, "Invalid Phone Number", Toast.LENGTH_LONG).show();
            retry = true;
        } else if (!inputNumber.matches(".*\\d+.*")) {
            //if it contains invalid values try again
            Toast.makeText(ctx, "Only numbers allowed", Toast.LENGTH_LONG).show();
            retry = true;
        }  else {
            retry = false;
        }
    }


    public void addNewNumber(String inputName,String inputNumber){
        //Store phone number and contact name into arrays

        if(size>=3){
            Toast.makeText(ctx,"Max Contacts Reached",Toast.LENGTH_LONG).show();
        }
        else {
            contacts[size] = inputName;
            phoneNumbers[size] = inputNumber;
            size++;
            int contactRem = 3-size;
            Toast.makeText(ctx,"Contact Added! "+contactRem+ " contacts remaining",Toast.LENGTH_LONG).show();

            GPS myGPS = new GPS();
            myGPS.setName(contacts);
            myGPS.setNumbers(phoneNumbers);
        }
    }
}
