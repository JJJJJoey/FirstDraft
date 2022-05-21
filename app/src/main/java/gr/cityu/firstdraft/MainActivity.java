package gr.cityu.firstdraft;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements RecycleViewInterfaceMain{

    //initialising the components
    private Button mButtonNextScreen;
    private FirebaseAuth mAuth;
    private ImageView mImageViewProfPic;
    private SearchView mSearchView;

    private DatabaseReference mDatabaseRef;
    private RecyclerViewAdapterMain mAdapter,recyclerViewAdapterMain;

    private RecyclerView mRecyclerViewMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //next line hides the action bar
        getSupportActionBar().hide();
        //disables dark (night) mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        //connecting the components with the xml file
        mImageViewProfPic = findViewById(R.id.imageViewProfPic);
        mRecyclerViewMain = findViewById(R.id.recyclerViewMain);
        mSearchView = findViewById(R.id.searchView);
        mRecyclerViewMain.setLayoutManager(new LinearLayoutManager(this));


        //checking if the user is singed in , if not the login register activity starts, asking th user to log in or create a new account
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(this,LoginRegActivity.class);
            startActivity(intent);
            finish();
        }


        //getting current user
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this,"the current user is: "+currentUser,Toast.LENGTH_LONG).show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        //getting userID
        String currentUserID= FirebaseAuth.getInstance().getUid();
        //Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();


        //Recycler View
        FirebaseRecyclerOptions<ItemUploadModel> options=
                new FirebaseRecyclerOptions.Builder<ItemUploadModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("item info all"), ItemUploadModel.class)
                        .build();

        mAdapter = new RecyclerViewAdapterMain(options,this);
        mRecyclerViewMain.setAdapter(mAdapter);

        DatabaseReference mImageRef = mDatabaseRef.child("/account info/"+currentUserID+"/mImageUrl");
        mImageRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String url = snapshot.getValue(String.class);
                    Glide.with(getApplicationContext()).load(url).into(mImageViewProfPic);
                    //Toast.makeText(AccountSettingsActivity.this,"image ref is: "+url,Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                textSearch(query);
                return false;
            }
        });
    }

    public void nextActivity(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    public void logRegIntent(){
        Intent intent = new Intent(this, LoginRegActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }*/

    @Override
    public void onItemClick(String id, int position) {
        //String id = documentSnapshot.getId();
        Intent intent=new Intent(this,ItemViewAllActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("id",id);

        Log.d(TAG, "position accountA: " + position);
        Log.d(TAG, "id accountA: " + id);
        startActivity(intent);
    }

    //search function
    private void textSearch(String txt){
        FirebaseRecyclerOptions<ItemUploadModel> options=
                new FirebaseRecyclerOptions.Builder<ItemUploadModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("item info all").orderByChild("mName")
                                .startAt(txt).endAt(txt+"~"), ItemUploadModel.class)
                        .build();
        recyclerViewAdapterMain = new RecyclerViewAdapterMain(options,this);
        recyclerViewAdapterMain.startListening();
        mRecyclerViewMain.setAdapter(recyclerViewAdapterMain);
    }


}