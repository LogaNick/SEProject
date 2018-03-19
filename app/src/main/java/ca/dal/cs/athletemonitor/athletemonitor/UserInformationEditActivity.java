package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.dal.cs.athletemonitor.athletemonitor.utilities.StringJoiner;

import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER;
import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_INFORMATION;

/**
 * This class allows the user to edit their personal information, and save it to Firebase.
 */
public class UserInformationEditActivity extends AppCompatActivity {

    private User user;
    private UserInformation info;

    private static Pattern digitPattern = Pattern.compile(".*?(\\d+).*?");

    /**
     * This method updates the internal UserInformation object using
     * the values in the EditText fields.
     */
    private void updateInfo() {
        EditText nameEditText = findViewById(R.id.nameEditText);
        String[] names = nameEditText.getText().toString().split("\\s");
        StringJoiner sj = new StringJoiner(" ");
        for (int i = 0; i < names.length - 1; i++)
            sj.add(names[i]);
        String firstName = sj.toString();
        String lastName = names[names.length - 1];

        EditText ageEditText = findViewById(R.id.ageEditText);
        Matcher ageMatcher = digitPattern.matcher(ageEditText.getText().toString());
        int age = ageMatcher.matches() ? Integer.parseInt(ageMatcher.group(1)) : 0;

        EditText heightEditText = findViewById(R.id.heightEditText);
        Matcher heightMatcher = digitPattern.matcher(heightEditText.getText().toString());
        int height = heightMatcher.matches() ? Integer.parseInt(heightMatcher.group(1)) : 0;

        EditText weightEditText = findViewById(R.id.weightEditText);
        Matcher weightMatcher = digitPattern.matcher(weightEditText.getText().toString());
        int weight = weightMatcher.matches() ? Integer.parseInt(weightMatcher.group(1)) : 0;

        EditText athleteEditText = findViewById(R.id.athleteTypeEditText);
        String athleteType = athleteEditText.getText().toString();

        EditText statementEditText = findViewById(R.id.statementEditText);
        String personalStatement = statementEditText.getText().toString();

        info = new UserInformation.UserInformationBuilder(firstName, lastName)
                .age(age)
                .height(height)
                .weight(weight)
                .athleteType(athleteType)
                .personalStatement(personalStatement)
                .build();
    }

    /**
     * This method updates the UserInformation object in Firebase associated
     * with the user currently logged in.
     */
    private void updateDatabase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = db.getReference(getString(R.string.activity_user_information_firebase, user.getUsername()));

        myRef.setValue(info);
    }

    /**
     * This method changes the information displayed on the personal info screen,
     * based on the passed object.
     *
     * @param info a representation of the the info to be displayed
     */
    private void changeDisplayedInfo(UserInformation info) {
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText statementEditText = findViewById(R.id.statementEditText);
        EditText ageEditText = findViewById(R.id.ageEditText);
        EditText heightEditText = findViewById(R.id.heightEditText);
        EditText weightEditText = findViewById(R.id.weightEditText);
        EditText athleteEditText = findViewById(R.id.athleteTypeEditText);

        nameEditText.setText(getString(
                R.string.activity_user_information_format_name,
                info.firstName,
                info.lastName)
        );
        statementEditText.setText(getString(
                R.string.activity_user_information_format_statement,
                info.personalStatement)
        );
        ageEditText.setText(getString(R.string.activity_user_information_format_age, info.age));
        heightEditText.setText(
                getString(R.string.activity_user_information_format_height, info.height)
        );
        weightEditText.setText(
                getString(R.string.activity_user_information_format_weight, info.weight)
        );
        athleteEditText.setText(
                getString(R.string.activity_user_information_format_athlete_type, info.athleteType)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER);
        info = intent.getParcelableExtra(USER_INFORMATION);

        changeDisplayedInfo(info);

        FloatingActionButton fab = findViewById(R.id.saveInfo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
                updateDatabase();
                Intent intent = new Intent();
                intent.putExtra(USER_INFORMATION, info);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
