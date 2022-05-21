package gr.cityu.firstdraft;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemViewAllActivity extends AppCompatActivity {
    TextView mItemNameVTextView, mTextViewItemVDesc;
    ImageView mImageViewItemView;
    private ArrayList<RequestModel> request;
    private ArrayList listAll;
    private ArrayList listUsers;

    Button mButtonRequest;
    int position;
    String id;
    DatabaseReference databaseReference;
    String currentUserID= FirebaseAuth.getInstance().getUid();
    String ownersId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view_all);


        //next line hides the action bar
        getSupportActionBar().hide();


        mItemNameVTextView = findViewById(R.id.textViewItemVName);
        mTextViewItemVDesc = findViewById(R.id.TextViewItemVDesc);
        mImageViewItemView = findViewById(R.id.imageViewItemView);
        mButtonRequest = findViewById(R.id.buttonRequest);

        //getting the position of the data snapshot to showcase it in the next activity
        String id = getIntent().getStringExtra("id");


        //for testing
        //System.out.println("view position"+position);
       // Log.d(TAG, "view id: " + id);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference itemDataPath = databaseReference.child("/account info/"+currentUserID+"/"+id);
        DatabaseReference itemDataPathName = databaseReference.child("/item info all/"+id+"/mName");


        //setting the name of the item
        itemDataPathName.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mItemNameVTextView.setText(text);
                    //Log.d(TAG,"path name: "+text);
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });
        //setting the description of the item
        DatabaseReference itemDataPathCategory = databaseReference.child("/item info all/"+id+"/mImageDescription");
        itemDataPathCategory.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mTextViewItemVDesc.setText(text);
                    //Log.d(TAG,"path name: "+text);
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        DatabaseReference mImageRef = databaseReference.child("/item info all/"+id+"/mImageUrl");
        //setting the photo of the item
        mImageRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String url = snapshot.getValue(String.class);
                    Glide.with(getApplicationContext()).load(url).into(mImageViewItemView);
                    //Toast.makeText(AccountSettingsActivity.this,"image ref is: "+url,Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        //logs for testing
        //Log.d(TAG,"path name: "+itemDataPath2);
        //Log.d(TAG,"user i: "+currentUserID);



        //items of current user
        Query queryUsers = databaseReference.child("/item info/ "+currentUserID);

        //its filling out the array everytime

        ArrayList<String> listUsers = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();
                    listUsers.add(id);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        queryUsers.addListenerForSingleValueEvent(valueEventListener);

        //items of all users
        Query queryAll = databaseReference.child("/item info all/");

        //its filling out the array everytime

        ArrayList<String> listAll = new ArrayList<>();
        ValueEventListener valueEventListenerAll = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();
                    listAll.add(id);

                    //Toast.makeText(ItemViewAllActivity.this,"hello"+listAll.size(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        queryAll.addListenerForSingleValueEvent(valueEventListener);

        DatabaseReference ownersIdRef = databaseReference.child("/item info all/"+id+"/userId");

        DatabaseReference requests =FirebaseDatabase.getInstance().getReference("requests");


        mButtonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get item's owner id
                //get current user's id
                //get item's id
                //send the request

                ownersIdRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            //getting the owners id
                            DataSnapshot snapshot = task.getResult();
                            String ownersId = snapshot.getValue(String.class);

                            //inserting the request info by using the request model
                            //which is sender, receiver, item id
                            RequestModel upload = new RequestModel(ownersId,currentUserID,id);
                            String uploadId = requests.push().getKey();
                            requests.child(uploadId).setValue(upload);

                        } else {
                            Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                        }
                    }
                });

            }
        });


       /* mButtonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(ItemViewAllActivity.this,"hello",Toast.LENGTH_LONG).show();
                for (int i=0; i<listAll.size();i++) {
                    Toast.makeText(ItemViewAllActivity.this,"hello",Toast.LENGTH_LONG).show();
                    String currAll=listAll.get(i);

                    for (int x=0; x<listUsers.size();x++) {
                        String currUser=listUsers.get(x);
                        System.out.println(listUsers);
                        if (currAll == currUser){
                            Toast.makeText(ItemViewAllActivity.this,"its the same",Toast.LENGTH_SHORT).show();

                        }else{
                            Log.d(TAG,"its different");
                            Toast.makeText(ItemViewAllActivity.this,"its different",Toast.LENGTH_LONG).show();
                        }

                    }

                }

            }
        });*/


    }




}