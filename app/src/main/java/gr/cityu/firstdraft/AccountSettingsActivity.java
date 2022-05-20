package gr.cityu.firstdraft;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AccountSettingsActivity extends AppCompatActivity {



    private Button mButtonApplyChanges;
    private ImageView mImageViewProfPic ;
    private EditText mEditTextName;
    private EditText mEditTextLikes;




    //Firebase Storage
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, mDatabaseRef2;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    //firebase authorisation for getting the current user
    private FirebaseAuth mAuth;


    private static final String TAG = "AccountSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //next line hides the action bar
        getSupportActionBar().hide();

        mButtonApplyChanges = findViewById(R.id.buttonApplyChanges);
        mImageViewProfPic = findViewById(R.id.imageViewProfPic);
        mEditTextName = findViewById(R.id.editTextName);
        mEditTextLikes = findViewById(R.id.editTextLikes);

        //getting current userID
        String currentUserID= FirebaseAuth.getInstance().getUid();

        //toast to test if the current user is correct
        //Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();





        //firebase storage and database initialize
        mStorageRef = FirebaseStorage.getInstance().getReference("account photos/"+currentUserID);
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("account info/"+currentUserID);

        DatabaseReference mImageRef = mDatabaseRef2.child("/account info/"+currentUserID+"/mImageUrl");
        DatabaseReference mNameRef = mDatabaseRef2.child("/account info/"+currentUserID+"/mName");
        DatabaseReference mLikesRef = mDatabaseRef2.child("/account info/"+currentUserID+"/mLikes");
       // String url = "https://firebasestorage.googleapis.com/v0/b/firstdraft-a5850.appspot.com/o/account%20photos%2FW9Tbt9FVjBYJNbpgKRxQcSGn9Zf1%2F1651333004232.png?alt=media&token=bfff1de4-c487-4798-bcb4-fdd98c98bfd6";


        //showcasing the profile photo of the user
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


        //showcasing the name of the user
        mNameRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mEditTextName.setText(text);

                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        //showcasing the likes of the user
        mLikesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mEditTextLikes.setText(text);

                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });



        mImageViewProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        mButtonApplyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();

            }
        });



    }
    public void newItemIntent(View view){
        Intent intent= new Intent(this, NewItemUploadActivity.class);
        startActivity(intent);
    }








    public void logout (View view){
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            logRegIntent();
                        }else {
                            Log.e(TAG,"onComplete:", task.getException());
                        }
                    }
                });



    }

    public void logRegIntent(){
        Intent intent = new Intent(this, LoginRegActivity.class);
        startActivity(intent);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mImageViewProfPic.setImageURI(mImageUri);


        }

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    //photo uploading
    private void uploadFile(){

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Toast.makeText(AccountSettingsActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();


                            UserInfoModel upload = new UserInfoModel(mEditTextName.getText().toString().trim(),downloadUrl.toString(),
                                    mEditTextLikes.getText().toString()
                                    );

                            //i dont need upload id because the info are under the userID unlike the items that are multiple
                            //for one user
                            //String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.setValue(upload);
                            //mDatabaseRef.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

}