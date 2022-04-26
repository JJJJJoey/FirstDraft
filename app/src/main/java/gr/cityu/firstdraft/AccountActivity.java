package gr.cityu.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {
    Button mButtonAccountSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mButtonAccountSettings = findViewById(R.id.buttonAccountSettings);
    }

    public void AccountSettingsIntent(View view){
        Intent intent = new Intent(this,AccountSettingsActivity.class);
        startActivity(intent);
    }
}