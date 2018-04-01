package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.username = this.findViewById(R.id.txtUsername);
        this.password = this.findViewById(R.id.txtPassword);
        this.signInButton = this.findViewById(R.id.btnSignIn);

        configureViews();
    }

    /**
     * Configures the components on this activity
     */
    private void configureViews() {
        this.username.setText("");
        this.password.setText("");
        this.signInButton.setEnabled(false);

        // set text watchers to control enable state of sign in button
        this.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoginActivity.this.signInButton.setEnabled(
                        !LoginActivity.this.username.getText().toString().equals("") &&
                        !LoginActivity.this.password.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        this.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoginActivity.this.signInButton.setEnabled(
                        !LoginActivity.this.username.getText().toString().equals("") &&
                        !LoginActivity.this.password.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Handles sign in button clicks
     *
     * @param view
     * @throws InterruptedException
     */
    public void signInClick(View view) throws InterruptedException {
        //disable the sign-in button to prevent multiple clicks during authentication
        this.signInButton.setEnabled(false);

        //attempt to authenticate
        AccountManager.authenticate(new User(this.username.getText().toString(), this.password.getText().toString()), new BooleanResultListener() {
            @Override
            public void onResult(boolean result) {
                //if authenticate returns true, login is valid.  Switch to main activity
                //with the loaded user information, otherwise, display invalid credentials message
                if (result) {
                    ((TextView) LoginActivity.this.findViewById(R.id.lblMessage)).setText(R.string.loginSuccess);

                    AccountManager.getUser(LoginActivity.this.username.getText().toString(), new AccountManager.UserObjectListener() {
                        @Override
                        public void onUserPopulated(User user) {
                            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainActivityIntent.putExtra("user", user);
                            startActivity(mainActivityIntent);
                            finish();
                        }
                    });
                } else {
                    ((TextView)LoginActivity.this.findViewById(R.id.lblMessage)).setText(R.string.loginFailure);

                    //re-enable the sign in button if invalid login
                    LoginActivity.this.signInButton.setEnabled(true);
                }
            }
        });
    }

    /**
     * Handles register account button click
     *
     * @param view
     */
    public void onRegisterButtonClick(View view) {
        //get data from activity
        final String username = ((EditText) this.findViewById(R.id.txtUsername)).getText().toString().trim();
        String password = ((EditText) this.findViewById(R.id.txtPassword)).getText().toString();

        AccountManager.createUser(new User(username, password), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                if (user != null) {
                    ((TextView) LoginActivity.this.findViewById(R.id.lblMessage)).setText(R.string.accountCreated);
                } else {
                    ((TextView) LoginActivity.this.findViewById(R.id.lblMessage)).setText(R.string.accountNotCreated);
                }
            }
        });
    }
}
