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

public class NewUserActivity extends AppCompatActivity {


    private ImageView mNewImageViewProfPic;
    private EditText mNewEditTextName;
    private EditText mNewEditTextLikes;
    private Button mButtonSave;



    //Firebase Storage
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    //firebase authorisation for getting the current user
    private FirebaseAuth mAuth;


    private static final String TAG = "AccountSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mNewImageViewProfPic = findViewById(R.id.imageButtonPhotoProf);
        mNewEditTextName = findViewById(R.id.editTextNewName);
        mNewEditTextLikes = findViewById(R.id.editTextNewLikes);
        mButtonSave = findViewById(R.id.buttonSave);

        //getting current userID
        String currentUserID= FirebaseAuth.getInstance().getUid();
        Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();

        //firebase storage and database initialize
        mStorageRef = FirebaseStorage.getInstance().getReference("account photos/"+currentUserID);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("account info/"+currentUserID);

        mNewImageViewProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
                mainIntent();

            }
        });






    }
    public void mainIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

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
            mNewImageViewProfPic.setImageURI(mImageUri);


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


                            Toast.makeText(NewUserActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();


                            UserInfo upload = new UserInfo(mNewEditTextName.getText().toString().trim(),downloadUrl.toString(),
                                    mNewEditTextLikes.getText().toString()
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
                            Toast.makeText(NewUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}