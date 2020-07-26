package hangbt.hust.hack365;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class Verify extends AppCompatActivity {

    String imageEncoded;
    List<String> imagesEncodedList;

    Button btn_test;
    Uri saveUri;

    private static final int PICK_IMAGE_REQUEST = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

//        chooseImages();
        btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImages();
            }
        });

    }

    private void chooseImage(){
        Intent chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
        chooseImage.setType("image/*");
        startActivityForResult(Intent.createChooser(chooseImage,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    private void chooseImages(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            saveUri = data.getData();
//            img_cmnd.setImageURI(saveUri);
//            btn_selectimg.setText("selected");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}