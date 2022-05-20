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

public class EditItemActivity extends AppCompatActivity {
    //initialising the required objects
    private ImageView mImageViewPic;
    private EditText mEditTextItemName;
    private EditText mEditTextItemCategory;
    private EditText mEditTextItemDescription;

    private EditText mEditTextImageTags;
    private Button mButtonApplyChanges;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //next line hides the action bar
        getSupportActionBar().hide();

        mImageViewPic = findViewById(R.id.imageViewPic);
        mEditTextItemName=findViewById(R.id.editTextItemName);
        mEditTextItemCategory=findViewById(R.id.editTextItemCategory);
        mEditTextItemDescription = findViewById(R.id.editTextItemDescription);
        mButtonApplyChanges = findViewById(R.id.buttonApplyChanges);
        mEditTextImageTags = findViewById(R.id.editTextItemTags);


        String id = getIntent().getStringExtra("id");

        //toast for testing
        Toast.makeText(EditItemActivity.this, "2 the id of the item is: "+id,Toast.LENGTH_SHORT).show();
        //getting current userID
        String currentUserID= FirebaseAuth.getInstance().getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference("item photos/"+currentUserID);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        //line for testing the right path
        DatabaseReference mNameRef = mDatabaseRef.child("/item info/ "+currentUserID+"/"+id+"/mName");

        DatabaseReference mImageRef = mDatabaseRef.child("/item info/"+currentUserID+"/"+id+"/mImageUrl");
        //DatabaseReference mNameRef = mDatabaseRef.child("/item info/"+currentUserID+"/mName");
        DatabaseReference mDescriptionRef = mDatabaseRef.child("/item info/ "+currentUserID+"/"+id+"/mImageDescription");
        DatabaseReference mCategoryRef = mDatabaseRef.child("/item info/"+currentUserID+"/"+id+"/mImageCategory");
        DatabaseReference mTagsRef = mDatabaseRef.child("/item info/"+currentUserID+"/"+id+"/mImageTags");

        //line for testing
        // String url = "https://firebasestorage.googleapis.com/v0/b/firstdraft-a5850.appspot.com/o/account%20photos%2FW9Tbt9FVjBYJNbpgKRxQcSGn9Zf1%2F1651333004232.png?alt=media&token=bfff1de4-c487-4798-bcb4-fdd98c98bfd6";




        mImageRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String url = snapshot.getValue(String.class);
                    Glide.with(getApplicationContext()).load(url).into(mImageViewPic);
                    //Toast.makeText(AccountSettingsActivity.this,"image ref is: "+url,Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        mNameRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mEditTextItemName.setText(text);

                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        mDescriptionRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mEditTextItemDescription.setText(text);

                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        mCategoryRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    mEditTextItemCategory.setText(text);

                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });



        mImageViewPic.setOnClickListener(new View.OnClickListener() {
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
            mImageViewPic.setImageURI(mImageUri);


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


                            Toast.makeText(EditItemActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();


                            ItemUploadModel upload = new ItemUploadModel(mEditTextItemName.getText().toString().trim(),downloadUrl.toString(),
                                    mEditTextItemCategory.getText().toString(),mEditTextItemDescription.getText().toString(),mEditTextImageTags.getText().toString()
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
                            Toast.makeText(EditItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}