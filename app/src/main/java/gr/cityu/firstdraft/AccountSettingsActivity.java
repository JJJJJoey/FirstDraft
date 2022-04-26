package gr.cityu.firstdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountSettingsActivity extends AppCompatActivity {

    EditText meditTextName;
    EditText meditTextDescription;
    Button mButtonNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        meditTextName = findViewById(R.id.editTextName);
        meditTextDescription = findViewById(R.id.editTextDescription);
        mButtonNewItem = findViewById(R.id.buttonNewItem);

    }
    public void newItemIntent(View view){
        Intent intent= new Intent(this, NewItemUploadActivity.class);
        startActivity(intent);
    }

}