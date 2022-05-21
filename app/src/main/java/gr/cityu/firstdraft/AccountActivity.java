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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity implements RecyclerViewInterface{
    private ImageView mButtonAccountSettings,mImageViewProfPic;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private DatabaseReference mDatabaseRef;

    private TextView mTextViewUserName;
    private TextView mTextViewUserLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //next line hides the action bar
        getSupportActionBar().hide();


        mRecyclerView = findViewById(R.id.recyclerViewItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTextViewUserName = findViewById(R.id.textViewUserName);
        mTextViewUserLikes = findViewById(R.id.textViewUserLikes);
        mImageViewProfPic=findViewById(R.id.imageViewProfPic);


        //getting current userID
        String currentUserID= FirebaseAuth.getInstance().getUid();

        //using toast to test if the user ID os correct
        //Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();

        //Recycler View
        FirebaseRecyclerOptions<ItemUploadModel> options=
                new FirebaseRecyclerOptions.Builder<ItemUploadModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/item info/ "+currentUserID), ItemUploadModel.class)
                        .build();


        //creating a new adapter instance
        mAdapter = new RecyclerViewAdapter(options,this);
        mRecyclerView.setAdapter(mAdapter);

        //Show user name
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        //lines for testing
        //DatabaseReference nameTextRef = mDatabaseRef.child("/account info/"+currentUserID+"/mName/");
        //DatabaseReference nameTextRef = mDatabaseRef.child("/account info/W9Tbt9FVjBYJNbpgKRxQcSGn9Zf1/mName");
        DatabaseReference nameTextRef = mDatabaseRef.child("/account info/"+currentUserID+"/mName");

        //toast to test is the image reference works
        //Toast.makeText(AccountActivity.this,"image ref is: "+nameTextRef,Toast.LENGTH_LONG).show();


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
        finish();
    }

    //account settings intent
    public void AccountSettingsIntent(View view){
        Intent intent = new Intent(AccountActivity.this,AccountSettingsActivity.class);

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
        finish();
    }
    public void RequestsIntent(View view){
        Intent intent = new Intent(AccountActivity.this, RequestsActivity.class);
        startActivity(intent);
    }

}