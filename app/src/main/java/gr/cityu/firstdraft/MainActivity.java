package gr.cityu.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mButtonNextScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonNextScreen = findViewById(R.id.buttonNextScreen);

    }
    public void nextActivity(View view){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}