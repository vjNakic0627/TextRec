package hr.aspira.textrec;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.mlkit.vision.common.InputImage;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity<FirebaseVisionText> extends Activity {

    private Button captureImageBtn, detectTextBtn;
    private ImageView imageView;
    private TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureImageBtn = findViewById(R.id.capture_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBit(imageBitmap);
        FirebaseVisionImage firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new On);
            @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText){
                displayTextFromImage(firebaseVisionText);

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error:", e.getMessage());
            }
        });
        
    }
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText){
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
            if(blockList.size() ==0){
                Toast.makeText(this, "No Text Found in image", Toast.LENGTH_SHORT).show();
    }
            else
            {
                for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks())
                {
                    String text = block.getText();
                    textView.setText(text);
                }
            }
    }
}