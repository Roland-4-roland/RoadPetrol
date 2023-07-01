package com.example.roadpetrol;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class CaptureImage extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION = 1;
    private SwitchMaterial getLocation;
    private Button buttonSend;

    LocationManager locationManager;
    String Latitude,Longitude,address;
    ImageView imageView;
    FloatingActionButton button;
    TextView latlong_location;
    EditText description;
    Uri uri,mImageUri;
    private String photoURL;
    private StorageReference mStorageRef;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String DocID;
    private String CurrentUserId;
    private String finalmessage() {
        String desc_send = description.getText().toString();
        String result = (desc_send + "\naddress of pothole: " + address);
        return (result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_image);
//        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getColor(R.color.light)));
        description=findViewById(R.id.description);
        imageView =findViewById(R.id.imageView);
        latlong_location=findViewById(R.id.txt_location);
        button = findViewById(R.id.floatingActionButton);
        firestore=FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        CurrentUserId=firebaseAuth.getCurrentUser().getUid();
        getLocation=findViewById(R.id.button_GetLocation);
        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values from the EditText fields
                String subject = "POTHOLE-REPORT!!!";
                String message = finalmessage();

                // Validate the input values
                if (message.isEmpty()) {
                    // Display an error message if the message field is empty
                    Toast.makeText(CaptureImage.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send the email
                sendEmail(subject, message);
                UploadImage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(CaptureImage.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

        getLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    if (ContextCompat.checkSelfPermission(CaptureImage.this,Manifest.permission.ACCESS_FINE_LOCATION)
                            !=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(CaptureImage.this,new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },100);
                    }
                    getLocation();
                }
            }
        });
    }
    private void sendEmail(String subject, String message) {
        SendEmailTask sendEmailTask = new SendEmailTask(subject, message);
        sendEmailTask.execute();
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private String subject;
        private String message;

        public SendEmailTask(String subject, String message) {
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Sender's email credentials
                final String username = "roadpatrol2023@gmail.com";
                final String password = "qcqwzckhjefuipsj";
                final String recipient = "testreceive2023@gmail.com";

                // SMTP server settings
                final String smtpHost = "smtp.gmail.com";
                final int smtpPort = 587;

                // Create properties for the mail session
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", smtpHost);
                props.put("mail.smtp.port", smtpPort);

                // Create a mail session with the properties
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                // Create a new message
                Message emailMessage = new MimeMessage(session);

                // Set the sender and recipient addresses
                emailMessage.setFrom(new InternetAddress(username));
                emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

                // Set the email subject and message body
                emailMessage.setSubject(subject);

                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(message);


                // Create the message body part for the attachment
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(mImageUri.getEncodedPath());

                // Create a multipart message to hold the attachment
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textBodyPart);
                multipart.addBodyPart(attachmentBodyPart);

                // Set the multipart message as the content of the email
                emailMessage.setContent(multipart);


                // Send the email
                Transport.send(emailMessage);

                // Return true if the email was sent successfully
                return true;
            } catch (MessagingException | IOException e) {
                // Return false if there was an error sending the email
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // Display a success message to the user
                Toast.makeText(getApplicationContext(),"Email sent Successfully",Toast.LENGTH_SHORT).show();
            } else {
                // Display an error message to the user
                Toast.makeText(getApplicationContext(),"Email failed to send",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("MissingPermission")
    private void getLocation(){
        try {
            locationManager= (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,CaptureImage.this);
        }catch(Exception e){}
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri=data.getData();
        mImageUri=uri;
        imageView.setImageURI(uri);
    }
    private void UploadImage(){
        if(mImageUri!=null){
            final StorageReference myRef=mStorageRef.child("uploads/"+mImageUri.getLastPathSegment());
            myRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri!=null){
                                photoURL=uri.toString();
                                UploadInfo();
                            }
                        }
                    });
                }
            });
        }

    }
    private void UploadInfo(){
        String desc=description.getText().toString().trim();
        if(TextUtils.isEmpty(desc)){
            Toast.makeText(getApplicationContext(),"Please fill all fields",Toast.LENGTH_SHORT).show();
        }
        else{
            DocumentReference documentReference=firestore.collection("uploads").document();
            Upload upload=new Upload(desc,Latitude,Longitude,photoURL,"",CurrentUserId,address);
            documentReference.set(upload, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        DocID=documentReference.getId();
                        Upload.setDocID(DocID);
                        documentReference.set(upload,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double Lati=location.getLatitude();
        double Long=location.getLongitude();
        Latitude=String.valueOf(Lati);
        Longitude=String.valueOf(Long);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Lati,Long, 1);
            if (addresses != null && addresses.size() > 0) {
                address=addresses.get(0).getAddressLine(0);
                latlong_location.setText(address);
                latlong_location.setSelected(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}