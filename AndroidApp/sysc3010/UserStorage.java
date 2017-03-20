package robert.sysc3010;

import android.content.Context;
import android.content.SharedPreferences;


public class UserStorage {

    public static final String NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserStorage(Context context){
        userLocalDatabase = context.getSharedPreferences(NAME,0);

    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);

        spEditor.commit();
    }

    public User getUser(){
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password","");

        User currentUser = new User(username,password);
        return currentUser;

    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();



    }


}
