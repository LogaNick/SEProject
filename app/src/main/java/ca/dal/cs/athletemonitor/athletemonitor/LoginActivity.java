package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("lee")) {
                    //myRef.child("messages").setValue("lee_exists");
                    //do stuff

                }
                int i = 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
