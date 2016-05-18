package com.mirrorchannelth.internship.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.net.Connection;
import com.mirrorchannelth.internship.service.ServiceDao;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityFragment extends Fragment implements View.OnClickListener, Connection.OnConnectionCallBackListener {
    private TextView toolbarTitleTextview;
    private ImageView rightMenuImageview;
    private FrameLayout cameraLayout;
    private FrameLayout imageLayout;
    private FrameLayout videoLayout;
    private ImageView imageView;
    private EditText titleactivityEditText;
    private EditText dateActivityEditText;
    private EditText detailActivityEditText;
    private Button saveButton;
    private ProgressBar progressbar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    private Uri imageUri;
    private ServiceDao serviceDao;

    public AddActivityFragment() {
        // Required empty public constructor
    }
    public static AddActivityFragment newInstance(){
        AddActivityFragment addActivityFragment = new AddActivityFragment();
        return addActivityFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_add_activity, container, false);
        initInstance(rootview);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serviceDao = new ServiceDao(WebAPI.URL);
    }

    private void initInstance(View rootview) {
        toolbarTitleTextview = (TextView) rootview.findViewById(R.id.toolbar_title);
        rightMenuImageview = (ImageView) rootview.findViewById(R.id.rightMenu);
        cameraLayout = (FrameLayout) rootview.findViewById(R.id.cameraLayout);
        imageLayout = (FrameLayout) rootview.findViewById(R.id.imageLayout);
        videoLayout = (FrameLayout) rootview.findViewById(R.id.videoLayout);
        imageView = (ImageView) rootview.findViewById(R.id.image);
        titleactivityEditText = (EditText) rootview.findViewById(R.id.activityTitleEditText);
        dateActivityEditText = (EditText) rootview.findViewById(R.id.dateActivity);
        detailActivityEditText = (EditText) rootview.findViewById(R.id.activityDetailEditText);
        saveButton = (Button) rootview.findViewById(R.id.saveButton);
        progressbar = (ProgressBar) rootview.findViewById(R.id.activityprogressBar);
        saveButton.setOnClickListener(this);
        cameraLayout.setOnClickListener(this);
        imageLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);


        rightMenuImageview.setImageResource(R.drawable.ic_history_white_24dp);
        toolbarTitleTextview.setText(R.string.add_activity_toolbar_title);
        rightMenuImageview.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightMenu:
                closeFragment();
                break;
            case R.id.cameraLayout:
                openCamera();
                break;
            case R.id.imageLayout:
                openGallery();
                break;
            case R.id.videoLayout:
                openVideoCam();
                break;
            case R.id.saveButton:
                addActivity();
                break;

        }
    }

    private void addActivity() {
        progressbar.setVisibility(View.VISIBLE);
        String title = titleactivityEditText.getText().toString();
        String date = dateActivityEditText.getText().toString();
        String detail = detailActivityEditText.getText().toString();
        String file = "";
        if(imageUri !=null) {
            file = imageUri.getPath();
        }

        serviceDao.addActivity(ShareData.getUserProfile(), title, date, detail, file, this);


    }

    private void openVideoCam() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);
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




    private void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0, R.anim.from_left, R.anim.to_right)
                .replace(R.id.fragmentContainer, ActivityHistoryFragment.newInstance())
                .addToBackStack("HistoryFragment")
                .commit();
    }

    @Override
    public void onSuccess(String result) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0, R.anim.from_left, R.anim.to_right)
                .replace(R.id.fragmentContainer, ActivityHistoryFragment.newInstance())
                .addToBackStack("HistoryFragment")
                .commit();
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onLostConnection() {
        Toast.makeText(getActivity(), "On lost connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnreachHost() {
        Toast.makeText(getActivity(), "On Un reachHost", Toast.LENGTH_SHORT).show();
    }
}
