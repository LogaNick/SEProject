package ca.dal.cs.athletemonitor.athletemonitor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class UserInformationActivity extends AppCompatActivity {

	/**
	 * This method connects to Firebase, retrieving the user information
	 * stored for a given userId.
	 * @param userId the ID for the user to display
	 * @return the user info corresponding with the ID if successful, null otherwise
	 */
	private UserInformation retrieveInfo(String userId) {

		//TODO Mockup for now, need to access Firebase
		final String MOCKUP_FIRST_NAME = getString(R.string.user_information_test_first_name);
		final String MOCKUP_LAST_NAME = getString(R.string.user_information_test_last_name);
		//TODO Handle NumberFormatException
		final int MOCKUP_AGE = Integer.parseInt(getString(R.string.user_information_test_age));
		final int MOCKUP_HEIGHT = Integer.parseInt(getString(R.string.user_information_test_height));
		final int MOCKUP_WEIGHT = Integer.parseInt(getString(R.string.user_information_test_weight));
		final String MOCKUP_ATHLETE_TYPE = getString(R.string.user_information_test_athlete_type);
		final String MOCKUP_STATEMENT = getString(R.string.user_information_test_statement);

		UserInformation info =
				new UserInformation.UserInformationBuilder(MOCKUP_FIRST_NAME, MOCKUP_LAST_NAME)
						.age(MOCKUP_AGE)
						.height(MOCKUP_HEIGHT)
						.weight(MOCKUP_WEIGHT)
						.athleteType(MOCKUP_ATHLETE_TYPE)
						.personalStatement(MOCKUP_STATEMENT)
						.build();

		return info;
	}

	/**
	 * This method changes the information displayed on the personal info screen,
	 * based on the passed object.
	 * @param info a representation of the the info to be displayed
	 */
	private void changeDisplayedInfo(UserInformation info) {
		TextView nameView = findViewById(R.id.nameTextView);
		TextView statementTextView = findViewById(R.id.statementTextView);
		TextView ageTextView = findViewById(R.id.ageDisplayView);
		TextView heightDisplayView = findViewById(R.id.heightDisplayView);
		TextView weightDisplayView = findViewById(R.id.weightDisplayView);
		TextView athleteTypeDisplayView = findViewById(R.id.athleteTypeDisplayView);

		nameView.setText(getString(
				R.string.activity_user_information_format_name,
				info.firstName,
				info.lastName)
		);
		statementTextView.setText(getString(
				R.string.activity_user_information_format_statement,
				info.personalStatement)
		);
		ageTextView.setText(getString(R.string.activity_user_information_format_age, info.age));
		heightDisplayView.setText(
				getString(R.string.activity_user_information_format_height, info.height)
		);
		weightDisplayView.setText(
				getString(R.string.activity_user_information_format_weight, info.weight)
		);
		athleteTypeDisplayView.setText(
				getString(R.string.activity_user_information_format_athlete_type, info.athleteType)
		);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		String userId = "mockup";

		UserInformation info = retrieveInfo(userId);
		changeDisplayedInfo(info);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
	}

}
