package gr.cityu.firstdraft;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemViewActivity extends AppCompatActivity {

   TextView mItemNameVTextView, mTextViewItemVDesc;
   ImageView mImageViewItemView;
   Button mButtonEditItem;
   int position;
   //String id = getIntent().getStringExtra("id");

   String id;


   DatabaseReference databaseReference;
   String currentUserID= FirebaseAuth.getInstance().getUid();
    //getting the id of the data snapshot to showcase it in the next activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        //next line hides the action bar
        getSupportActionBar().hide();

        mItemNameVTextView = findViewById(R.id.textViewItemVName);
        mTextViewItemVDesc = findViewById(R.id.TextViewItemVDesc);
        mImageViewItemView = findViewById(R.id.imageViewItemView);
        mButtonEditItem = findViewById(R.id.buttonEditItem);

        String id = getIntent().getStringExtra("id");
        //Toast.makeText(ItemViewActivity.this, "1 the id of the item is: "+id,Toast.LENGTH_SHORT).show();





        mButtonEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getIntent().getStringExtra("id");
                //Toast.makeText(ItemViewActivity.this, "2 the id of the item is: "+id,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                intent.putExtra("id", id);

                startActivity(intent);
            }
        });

        //for testing
       //System.out.println("view position"+position);
        Log.d(TAG, "view id: " + id);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference itemDataPath = databaseReference.child("/account info/"+currentUserID+"/"+id);
        DatabaseReference itemDataPathName = databaseReference.child("/item info/ "+currentUserID+"/"+id+"/mName");


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
        DatabaseReference itemDataPathCategory = databaseReference.child("/item info/ "+currentUserID+"/"+id+"/mImageDescription");
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

        DatabaseReference mImageRef = databaseReference.child("/item info/ "+currentUserID+"/"+id+"/mImageUrl");
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

    }
    public void EditItemIntent(View view){
        Intent intent = new Intent(this,EditItemActivity.class);
        //intent.putExtra("id",id);
        //Toast.makeText(ItemViewActivity.this, "2 the id of the item is: "+id,Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }



}