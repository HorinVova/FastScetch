package com.example.kitan.lab;


import java.io.FileNotFoundException;
import java.io.IOException;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class File extends AppCompatActivity { //implements  android.view.View.OnClickListener {

    private Button loadButton;
    private ImageView image;
    private static final int REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

//        image = (ImageView) findViewById(R.id.imageView1);
//        loadButton = (Button) findViewById(R.id.fileOpen);
//
//        loadButton.setOnClickListener(this);

    }
//    @Override
//    public void onClick(View v) {
//
//        Intent i = new Intent(Intent.ACTION_PICK);
//        i.setType("image/*");
//        startActivityForResult(i, REQUEST);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        Bitmap img = null;
//
//        if (requestCode == REQUEST && resultCode == RESULT_OK) {
//            Uri selectedImage = data.getData();
//            try {
//                img = Media.getBitmap(getContentResolver(), selectedImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            image.setImageBitmap(img);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
