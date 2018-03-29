package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        this.setFinishOnTouchOutside(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setContentView(R.layout.activity_search_results);
    }
}
