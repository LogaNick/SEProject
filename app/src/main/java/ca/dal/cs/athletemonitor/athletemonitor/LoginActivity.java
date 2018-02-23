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
        final String username = ((EditText) this.findViewById(R.id.txtUsername)).getText().toString();
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

                    Intent mainActivityIntent = new Intent(thisActivity, MainActivity.class);
                    mainActivityIntent.putExtra("username", username);
                    startActivity(mainActivityIntent);
                } else {
                    ((TextView)thisActivity.findViewById(R.id.lblMessage)).setText(R.string.loginFailure);
                }
            }
        });
    }
}
