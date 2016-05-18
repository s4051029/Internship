package com.mirrorchannelth.internship.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirrorchannelth.internship.R;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment implements View.OnClickListener {

    private ImageView cameraImageview;
    private ImageView imageImageview;
    private ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    private Uri imageUri;
    private TextView toolbartitle;
    private ImageView rightMenuButton;
    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(){
        TaskFragment taskFragment = new TaskFragment();
        return taskFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootview = inflater.inflate(R.layout.fragment_task, container, false);
        initInstance(rootview);
        return rootview;
    }

    private void initInstance(View rootview) {
        cameraImageview = (ImageView) rootview.findViewById(R.id.cameraButton);
        imageImageview = (ImageView) rootview.findViewById(R.id.imageButton);
        imageView = (ImageView) rootview.findViewById(R.id.imageView);
        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        rightMenuButton = (ImageView) rootview.findViewById(R.id.rightMenu);
        imageView = (ImageView) rootview.findViewById(R.id.image);

        rightMenuButton.setVisibility(View.VISIBLE);
        rightMenuButton.setImageResource(R.drawable.ic_history_white_24dp);

        rightMenuButton.setOnClickListener(this);
        cameraImageview.setOnClickListener(this);
        imageImageview.setOnClickListener(this);


        toolbartitle.setText(R.string.task_toolbar_title);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cameraButton:
                openCamera();
                break;
            case R.id.imageButton:
                openGallery();
                break;
            case R.id.rightMenu:
                closeFragment();
                break;

        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_SELECT_IMAGE);

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    setImageToImageview();
                }
                break;
            case REQUEST_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 360, 360, true);
                            imageView.setImageBitmap(scaled);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
        }
    }

    private void setImageToImageview() {
        Uri selectedImage = imageUri;
        getActivity().getContentResolver().notifyChange(selectedImage, null);
        ContentResolver cr = getActivity().getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media
                    .getBitmap(cr, selectedImage);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 360, 360, true);
            imageView.setImageBitmap(scaled);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT)
                    .show();
            Log.e("Camera", e.toString());
        }
    }

    public void closeFragment(){

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, TaskHistoryFragment.newInstance(),"TaskHistory")
                .commit();

    }
}
