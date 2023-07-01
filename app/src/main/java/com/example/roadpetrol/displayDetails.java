package com.example.roadpetrol;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class displayDetails extends AppCompatActivity {
    TextView Address;
    Button mapslink,deletebutton;
    String link;
    ImageView Image;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_details);
        Address=findViewById(R.id.address);
        deletebutton=findViewById(R.id.delete_button);
        Image=findViewById(R.id.image);
        mapslink=findViewById(R.id.maps_link);
        Intent intent = getIntent();
        String add_str = intent.getStringExtra("address");
        String image_str=intent.getStringExtra("image");
        String lat_str=intent.getStringExtra("latitude");
        String long_str=intent.getStringExtra("longitude");
        String document_id=intent.getStringExtra("docID");
        String uploaded_user_id=intent.getStringExtra("currentuserID");
        String currentuserId=currentUser.getUid();
        Address.setText(add_str);
        link="https://maps.google.com/?q="+lat_str+','+long_str;
        Glide.with(this).load(image_str).into(Image);
        mapslink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri url=Uri.parse(link);
                startActivity(new Intent(Intent.ACTION_VIEW,url));
            }
        });
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentuserId.equals(uploaded_user_id)) {
                    DocumentReference documentReference = db.collection("uploads").document(document_id);
                    documentReference.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(displayDetails.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(displayDetails.this, "Some error occured", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                else{
                    Toast.makeText(displayDetails.this,"You are not authorized",Toast.LENGTH_SHORT).show();
                }
                Intent it = new Intent(displayDetails.this, MainActivity.class);
                startActivity(it);
            }
        });
    }
}
