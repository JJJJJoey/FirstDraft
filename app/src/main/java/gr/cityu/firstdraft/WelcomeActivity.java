package gr.cityu.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    Button mButtonAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        mButtonAccount = findViewById(R.id.buttonAccount);
    }

    public void accountIntent(View view){
        Intent intent = new Intent(this,AccountActivity.class);
        startActivity(intent);

    }
}