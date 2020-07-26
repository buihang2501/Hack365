package hangbt.hust.hack365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hangbt.hust.hack365.Model.Common;
import hangbt.hust.hack365.Model.User;

public class Login extends AppCompatActivity {
    EditText edt_cmnd, edt_pass;
    Button btn_login;
    TextView tv_register, tv_forgot_pass;


    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findID();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(Login.this,SignUp.class);
                startActivity(signup);
            }
        });

    }

    private void login() {
        String cmnd = edt_cmnd.getText().toString();
        final String pass = edt_pass.getText().toString();
        if(cmnd.equals("")||pass.equals("")){
            Toast.makeText(this,"Chưa nhập đủ thông tin!",Toast.LENGTH_SHORT).show();
        }else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("User").document(cmnd);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String password = document.get("password").toString();
                            if(pass.equals(password)){
//                                Common.currentUser.setCmnd(cmnd);
//                                Common.currentUser.setName(document.get("name").toString());
                                Intent home = new Intent(Login.this, MainActivity.class);
                                startActivity(home);
                                finish();
                            }else{
                                Toast.makeText(Login.this,"Mật khẩu không đúng",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this,"CMND không đúng",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void findID(){
        edt_cmnd = findViewById(R.id.edt_cmnd);
        edt_pass = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_sigin);
        tv_register = findViewById(R.id.tv_register);
    }
}