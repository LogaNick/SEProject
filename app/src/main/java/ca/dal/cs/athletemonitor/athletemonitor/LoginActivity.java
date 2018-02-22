package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signInClick(View view) {
        String username;
        String password;

        //get data from activity
        username = ((EditText) this.findViewById(R.id.txtUsername)).getText().toString();
        password = ((EditText) this.findViewById(R.id.txtPassword)).getText().toString();

        //validate login information. if login information is valid then switch to main activity
        //with the loaded user information, otherwise, display invalid credentials message

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);

    }

}
