package gr.cityu.firstdraft;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class NewItemUploadActivity extends AppCompatActivity {
    Button mButtonUpload;
    Button mButtonChooseImage;
    EditText mEditTextItemTitle;
    EditText mEditTextItemDescription;
    EditText mEditTextItemCategory;
    EditText mEditItemTags;
    ImageView mImageView;



    //for image upload
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    //Firebase Storage
    private StorageReference mStorageRef;
    DatabaseReference mDatabaseRef, mDatabaseRefAll;


    //firebase authorisation for getting the current user
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_upload);

        //next line hides the action bar
        getSupportActionBar().hide();

        mEditTextItemTitle = findViewById(R.id.editTextItemTitle);

        mButtonUpload = findViewById(R.id.buttonUpload);
        mButtonChooseImage = findViewById(R.id.buttonAddPhoto);

        mImageView = findViewById(R.id.imageView);

        mEditTextItemDescription = findViewById(R.id.editTextItemDescription);
        mEditTextItemCategory = findViewById(R.id.editTextItemCategory);
        mEditItemTags = findViewById(R.id.editItemTags);

        //getting current userID
        String currentUserID= FirebaseAuth.getInstance().getUid();
        Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();

        //firebase storage and database initialize
        mStorageRef = FirebaseStorage.getInstance().getReference("item photos/"+currentUserID);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("item info/ "+currentUserID);

        //second tree that all users will have access
        mDatabaseRefAll = FirebaseDatabase.getInstance().getReference("item info all");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
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
            mImageView.setImageURI(mImageUri);
        }

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Toast.makeText(NewItemUploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            //calling the class
                            ItemUploadModel upload = new ItemUploadModel(mEditTextItemTitle.getText().toString().trim(),downloadUrl.toString(),
                                    mEditTextItemDescription.getText().toString(),
                                    mEditTextItemCategory.getText().toString(),
                                    mEditItemTags.getText().toString());

                            //==================================================================================================================================
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);

                            //Second saving database so all users have access
                            mDatabaseRefAll.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewItemUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}
