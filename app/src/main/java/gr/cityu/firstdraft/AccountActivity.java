package gr.cityu.firstdraft;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity implements RecyclerViewInterface{
    private ImageView mButtonAccountSettings;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;

    private TextView mTextViewUserName;
    private TextView mTextViewUserLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mButtonAccountSettings = findViewById(R.id.buttonAccountSettings);

        mRecyclerView = findViewById(R.id.recyclerViewItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTextViewUserName = findViewById(R.id.textViewUserName);
        mTextViewUserLikes = findViewById(R.id.textViewUserLikes);


        //getting current userID
        String currentUserID= FirebaseAuth.getInstance().getUid();

        //just for testing
        //Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();

        //Recycler View
        FirebaseRecyclerOptions<ItemImageUpload> options=
                new FirebaseRecyclerOptions.Builder<ItemImageUpload>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/item info/ "+currentUserID),ItemImageUpload.class)
                        .build();



        mAdapter = new ImageAdapter(options,this);
        mRecyclerView.setAdapter(mAdapter);

        //Show user name
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nameTextRef = mDatabaseRef.child("/account info/"+currentUserID+"/mName");

        nameTextRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mTextViewUserName.setText(text);
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        //Show user Likes
        DatabaseReference likesTextRef = mDatabaseRef.child("/account info/"+currentUserID+"/mLikes");
        likesTextRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mTextViewUserLikes.setText(text);
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });
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

    //new item intent
    public void AddNewItemIntent(View view){
        Intent intent = new Intent(this,NewItemUploadActivity.class);
        startActivity(intent);
    }

    //account settings intent
    public void AccountSettingsIntent(View view){
        Intent intent = new Intent(this,AccountSettingsActivity.class);
        startActivity(intent);
    }



    @Override
    public void onItemClick(String id, int position) {
        //String id = documentSnapshot.getId();
        Intent intent=new Intent(this,ItemViewActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("id",id);
        //intent.putExtra("id",mDatabaseRef.getKey());
        //String id= mDatabaseRef.get(position).getKey();

        Log.d(TAG, "position accountA: " + position);
        Log.d(TAG, "id accountA: " + id);
        startActivity(intent);
    }

}