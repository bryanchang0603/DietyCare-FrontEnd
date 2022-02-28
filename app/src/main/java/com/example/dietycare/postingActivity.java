package com.example.dietycare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

public class postingActivity extends AppCompatActivity {

    private ImageView return_button, photo_display, add_photo;
    private EditText text_field;
    private MaterialButton post_button;
    private static final int PReqCode = 2;
    private Uri selected_image_uri = null;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    selected_image_uri = uri;
                    photo_display.setImageURI(selected_image_uri);
                }
            });
    private FirebaseApp FirebaseAppCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        //bind the components to variables
        return_button = findViewById(R.id.posting_return);
        photo_display = findViewById(R.id.image_display);
        add_photo = findViewById(R.id.add_image_button);
        text_field = findViewById(R.id.posting_text_field);
        post_button = findViewById(R.id.posting_submit_button);

        //the following code are for testing retrieve image from firebase storage based on image_path
/*        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("community_images/image:31");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(photo_display);
            }
        });*/

        button_setup();


    }

    private void button_setup() {
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_gallery_premission();
                openGallery();
            }
        });

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(postingActivity.this, communityActivity.class);
                //Starting of the Intent
                startActivity(intent);
                finish();
            }
        });

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                System.out.println(text_field);
                System.out.println(selected_image_uri.getLastPathSegment());*/
/*                file_type = getContentResolver().getType(selected_image_uri).split(Pattern.quote("/")); // get the file surfix
                System.out.println(file_type[1]);*/
                post_button.setEnabled(false);
                if(!text_field.getText().toString().equals("") && selected_image_uri != null){

                    //upload the image to Firebase Storage first
                    StorageReference storage_reference = FirebaseStorage.getInstance().getReference().child("community_images");
                    String image_name = UUID.randomUUID().toString();
                    final StorageReference image_file_path = storage_reference.child(image_name);// change the naming here
                    // use UUID for naming instead
                    image_file_path.putFile(selected_image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            image_file_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String firebase_uri = uri.toString();
                                    //create post
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference my_ref = database.getReference("posts").push();
                                    String key = my_ref.getKey();
                                    String image_string = "community_images/"+image_name;
                                    Post post = new Post(text_field.getText().toString(), firebase_uri,
                                            FirebaseAuth.getInstance().getCurrentUser().getUid(), key, image_string, null, 0);
/*                                    post.appendComment("123");
                                    post.appendComment("456");
                                    post.appendLikedUser("123");
                                    post.appendLikedUser("456");*/
                                    my_ref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(postingActivity.this, communityActivity.class);
                                            //Starting of the Intent
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    // the following code are for testing retriving modified
/*                                    post.appendLikedUser("user 3");
                                    DatabaseReference mDatabase = database.getReference();
                                    mDatabase.child("posts").child(key).setValue(post);
                                    ArrayList<String> test = new ArrayList<String>();
                                    test.add("null");
                                    post.setUser_liked(test);
                                    mDatabase.child("posts").child(key).setValue(post);*/
                                }
                            });
                        }
                    });
                }
                else{
                    post_button.setEnabled(true);
                    System.out.println("some field is incomplete");
                    Toast.makeText(getBaseContext(), "The post need a photo and some text", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void check_gallery_premission(){
        if (ContextCompat.checkSelfPermission(postingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(postingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(postingActivity.this, "Please accept the permission", Toast.LENGTH_SHORT).show();
            }

            else{
                ActivityCompat.requestPermissions(postingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
    }

    private void openGallery(){
        mGetContent.launch("image/*");
    }
}
