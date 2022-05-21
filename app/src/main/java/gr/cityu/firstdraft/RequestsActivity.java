package gr.cityu.firstdraft;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RequestsActivity extends AppCompatActivity implements RecyclerViewInterfaceRequests{
    private DatabaseReference mDatabaseRef;
    private RecyclerViewAdapterRequests mAdapter,recyclerViewAdapterRequests;

    private RecyclerView mRecyclerViewRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        //next line hides the action bar
        getSupportActionBar().hide();

        mRecyclerViewRequests = findViewById(R.id.recyclerViewRequests);

        //Recycler View
        FirebaseRecyclerOptions<ItemUploadModel> options=
                new FirebaseRecyclerOptions.Builder<ItemUploadModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("requests"), ItemUploadModel.class)
                        .build();



        mAdapter = new RecyclerViewAdapterRequests(options,this);
        mRecyclerViewRequests.setAdapter(mAdapter);




    }

    @Override
    public void onItemClick(String id, int position) {

    }
}