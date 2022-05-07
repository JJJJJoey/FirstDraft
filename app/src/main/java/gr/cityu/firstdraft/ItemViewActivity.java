package gr.cityu.firstdraft;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemViewActivity extends AppCompatActivity {

   TextView mItemNameVTextView, mTextViewItemVDesc;
   ImageView mImageViewItemView;
   int position;
   String id;
   DatabaseReference databaseReference;
   String currentUserID= FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        mItemNameVTextView = findViewById(R.id.textViewItemVName);
        mTextViewItemVDesc = findViewById(R.id.TextViewItemVDesc);
        mImageViewItemView = findViewById(R.id.imageViewItemView);

        //getting the position of the data snapshot to showcase it in the next activity
        String id = getIntent().getStringExtra("id");


        //for testing
       //System.out.println("view position"+position);
        Log.d(TAG, "view id: " + id);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference itemDataPath = databaseReference.child("/account info/"+currentUserID+"/"+id);
        DatabaseReference itemDataPathName = databaseReference.child("/item info/ "+currentUserID+"/"+id+"/mName");
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

        //Log.d(TAG,"path name: "+itemDataPath2);
        //Log.d(TAG,"user i: "+currentUserID);

    }
}