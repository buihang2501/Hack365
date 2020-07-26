package hangbt.hust.hack365.navigation;

import android.content.Intent;
import android.media.Session2Command;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import hangbt.hust.hack365.R;

public class VerifyFragment extends Fragment {
    ImageButton img_muabh;
    Uri saveUri;
    private static final int PICK_IMAGE_REQUEST = 42;


    public VerifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == -1 &&
                data != null && data.getData() != null){
            saveUri = data.getData();
//            img_cmnd.setImageURI(saveUri);
//            btn_selectimg.setText("selected");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}