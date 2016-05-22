package com.mirrorchannelth.internship.fragment;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mirrorchannelth.internship.adapter.GalleryRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.model.Image;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.net.Connection;
import com.mirrorchannelth.internship.service.ServiceDao;
import com.mirrorchannelth.internship.view.DatePickerFragment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityFragment extends Fragment implements View.OnClickListener, Connection.OnConnectionCallBackListener {

    private RecyclerView galleryRecyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    private GalleryRecyclerViewAdapter adapter;
    private TextView toolbarTitleTextview;
    private ImageView rightMenuImageview;
    private FrameLayout cameraLayout;
    private FrameLayout imageLayout;
    private FrameLayout videoLayout;
    //private ImageView imageView;
    private EditText titleactivityEditText;
    private TextView dateActivityTextview;
    private EditText detailActivityEditText;
    private Button saveButton;
    private ProgressBar progressbar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    private Uri imageUri;
    private ServiceDao serviceDao;
    private List<Image> imageList;
    private String imageUrlTemp = null;

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

        galleryRecyclerView = (RecyclerView) rootview.findViewById(R.id.galleryRecyclerView);
        layoutManager = new GridLayoutManager(getActivity(),4);
        galleryRecyclerView.setLayoutManager(layoutManager);
        imageList = new ArrayList<Image>();
        adapter = new GalleryRecyclerViewAdapter(getActivity(), imageList);
        galleryRecyclerView.setAdapter(adapter);


        titleactivityEditText = (EditText) rootview.findViewById(R.id.activityTitleEditText);

        dateActivityTextview = (TextView) rootview.findViewById(R.id.activityDateTextview);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        dateActivityTextview.setText(currentDate);
        dateActivityTextview.setOnClickListener(this);





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
            case R.id.activityDateTextview:
                openDateDialog();
                break;

        }
    }

    private void openDateDialog() {
        android.support.v4.app.DialogFragment dateDialog = DatePickerFragment.getInstance(getActivity(), dateActivityTextview);
        dateDialog.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void addActivity() {
        progressbar.setVisibility(View.VISIBLE);
        String title = titleactivityEditText.getText().toString();
        String detail = detailActivityEditText.getText().toString();
        String file = "";
        if(imageUri !=null) {
            file = imageUri.getPath();
        }

        serviceDao.addActivity(ShareData.getUserProfile(), title, null, detail, file, this);


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
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_SELECT_IMAGE);

    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File imageFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "PIC_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                imageFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                imageUrlTemp = imageFile.getAbsolutePath();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    setImageToImageToGallery();
                }
                break;
            case REQUEST_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    setImageToGallery(data);

                }
                break;
        }
    }

    private void setImageToGallery(Intent data) {
        if (data != null) {
            ClipData clipdata = data.getClipData();
            Image image = null;
            for (int i = 0 ; i<clipdata.getItemCount(); i++){
                ClipData.Item item = clipdata.getItemAt(i);
                Uri uri = item.getUri();
                image = new Image(uri.getPath());
                image.setUri(uri);
                image.setProtocol("file://");
                imageList.add(image);
            }
            adapter.notifyDataSetChanged();

        }
    }

    private void setImageToImageToGallery() {
        Image image = new Image(imageUrlTemp);
        image.setProtocol("file://");
        imageList.add(image);
        adapter.notifyDataSetChanged();
    }




    private void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right)
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
