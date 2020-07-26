package hangbt.hust.hack365;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import hangbt.hust.hack365.Model.User;

public class SignUp extends AppCompatActivity {

    EditText edt_name, edt_cmnd, edt_mail, edt_phone, edt_pass, edt_repass;
    Button btn_signup;
    ImageView img_cmnd;

    private static final int PICK_IMAGE_REQUEST = 42;

    Uri saveUri;
    User newUser;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findID();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        img_cmnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private void signUp() {
        String name = edt_name.getText().toString();
        String cmnd = edt_cmnd.getText().toString();
        String phone = edt_phone.getText().toString();
        String email = edt_mail.getText().toString();
        String pass = edt_pass.getText().toString();
        String repass = edt_repass.getText().toString();

        if(saveUri == null){
            Toast.makeText(this, "Chưa có ảnh CMND!!!",Toast.LENGTH_SHORT).show();
        }else{
            if(name.equals("")||phone.equals("")||
                    pass.equals("")||repass.equals("")||cmnd.equals("")){
                Toast.makeText(this,"Nhập thiếu thông tin!",Toast.LENGTH_SHORT).show();
            }else{
                if(!pass.equals(repass)||pass.length() < 6){
                    Toast.makeText(this,"Mật khẩu không khớp hoặc chưa đủ số kí tự!",Toast.LENGTH_SHORT).show();
                }else{
                    uploadImgCMND();
                }
            }
        }
    }

    private void uploadImgCMND() {
        if(saveUri != null){
            final ProgressDialog nDialog = new ProgressDialog(this);
            nDialog.setMessage("Uploading...");
            nDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imgeFolder = storageReference.child("CMND/" + imageName);
            imgeFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    nDialog.dismiss();
                    Toast.makeText(SignUp.this,"Upload success!",Toast.LENGTH_SHORT).show();
                    imgeFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newUser = new User(edt_name.getText().toString(),
                                    edt_mail.getText().toString(),
                                    edt_phone.getText().toString(),
                                    edt_cmnd.getText().toString(),
                                    uri.toString(),edt_pass.getText().toString());
                            //add User to Db
                            addToDb();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    nDialog.dismiss();
                    Toast.makeText(SignUp.this,"" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    nDialog.setMessage("Upload "+progress);
                }
            });
        }
    }

    private void addToDb(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("CMND",newUser.getCmnd());
        db.collection("User").document(newUser.getCmnd()).set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this,"Đăng kí thành công",Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(SignUp.this,Login.class);
                        startActivity(login);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseImage(){
        Intent chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
        chooseImage.setType("image/*");
        startActivityForResult(Intent.createChooser(chooseImage,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            saveUri = data.getData();
            img_cmnd.setImageURI(saveUri);
//            btn_selectimg.setText("selected");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void findID(){
        edt_name = findViewById(R.id.edt_name);
        edt_cmnd = findViewById(R.id.edt_cmnd);
        edt_mail = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_sdt);
        edt_pass = findViewById(R.id.edt_password);
        edt_repass = findViewById(R.id.edt_repassword);

        btn_signup = findViewById(R.id.btn_register);
        img_cmnd = findViewById(R.id.img_cmnd);
    }
}