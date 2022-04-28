package gr.cityu.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {
    Button mButtonAccountSettings;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mButtonAccountSettings = findViewById(R.id.buttonAccountSettings);

        mRecyclerView = findViewById(R.id.recyclerViewItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ItemImageUpload> options=
                new FirebaseRecyclerOptions.Builder<ItemImageUpload>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("item info"),ItemImageUpload.class)
                        .build();

        mAdapter = new ImageAdapter(options);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public void AccountSettingsIntent(View view){
        Intent intent = new Intent(this,AccountSettingsActivity.class);
        startActivity(intent);
    }
}