package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signInClick(View view) throws InterruptedException {
        //get data from activity
        final String username = ((EditText) this.findViewById(R.id.txtUsername)).getText().toString().trim();
        String password = ((EditText) this.findViewById(R.id.txtPassword)).getText().toString();
        final Activity thisActivity = this;

        //attempt to authenticate
        AccountManager.authenticate(new User(username, password), new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                //if authenticate returns true, login is valid.  Switch to main activity
                //with the loaded user information, otherwise, display invalid credentials message
                if (result) {

                    ((TextView) thisActivity.findViewById(R.id.lblMessage)).setText(R.string.loginSuccess);

                    AccountManager.getUser(username, new AccountManager.UserObjectListener() {
                        @Override
                        public void onUserPopulated(User user) {
                            Intent mainActivityIntent = new Intent(thisActivity, MainActivity.class);
                            mainActivityIntent.putExtra("user", user);
                            startActivityForResult(mainActivityIntent, 1);

                        }
                    });

                } else {
                    ((TextView)thisActivity.findViewById(R.id.lblMessage)).setText(R.string.loginFailure);
                }
            }
        });
    }

    public void onRegisterButtonClick(View view) {
        //get data from activity
        final String username = ((EditText) this.findViewById(R.id.txtUsername)).getText().toString().trim();
        String password = ((EditText) this.findViewById(R.id.txtPassword)).getText().toString();

        Log.d("CREATING_USER", "Creating user..");
        AccountManager.createUser(new User(username, password), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                Log.d("CREATING_USER", "Callback entered");
                if (user != null) {
                    Log.d("CREATING_USER", "Account created...username: " + user.getUsername());
                    ((TextView) LoginActivity.this.findViewById(R.id.lblMessage)).setText(R.string.accountCreated);
                } else {
                    Log.d("CREATING_USER", "User not created");
                    ((TextView) LoginActivity.this.findViewById(R.id.lblMessage)).setText(R.string.accountNotCreated);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }
}
