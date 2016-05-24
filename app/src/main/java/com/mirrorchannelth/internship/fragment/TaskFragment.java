package com.mirrorchannelth.internship.fragment;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.GalleryRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.model.Image;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.net.Connection;
import com.mirrorchannelth.internship.service.ServiceDao;
import com.mirrorchannelth.internship.util.DateUtil;
import com.mirrorchannelth.internship.util.FormValidation;
import com.mirrorchannelth.internship.util.WindowsUtil;
import com.mirrorchannelth.internship.view.DatePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment implements View.OnClickListener, Connection.OnConnectionCallBackListener {

    private RecyclerView galleryRecyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    private GalleryRecyclerViewAdapter adapter;
    private ImageView cameraImageview;
    private ImageView imageImageview;
    private ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    private TextView toolbartitle;
    private ImageView rightMenuButton;
    private TextView taskDateTextview;
    private List<Image> imageList;
    private String imageUrlTemp = null;
    private Button saveButton;
    private EditText descriptionEditText;
    private EditText hourEditText;
    private EditText titleEditText;
    private ServiceDao serviceDao;
    private ProgressBar progressBar;
    private LinearLayout mainContent;

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
        bindWidget(rootview);
        initWidget();
        setWedgetListener();

        return rootview;
    }

    private void setWedgetListener() {

        rightMenuButton.setOnClickListener(this);
        cameraImageview.setOnClickListener(this);
        imageImageview.setOnClickListener(this);
        taskDateTextview.setOnClickListener(this);
        saveButton.setOnClickListener(this);

    }

    private void initWidget() {
        layoutManager = new GridLayoutManager(getActivity(),4);
        galleryRecyclerView.setLayoutManager(layoutManager);
        imageList = new ArrayList<Image>();
        adapter = new GalleryRecyclerViewAdapter(getActivity(), imageList);
        galleryRecyclerView.setAdapter(adapter);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        taskDateTextview.setText(currentDate);

        rightMenuButton.setVisibility(View.VISIBLE);
        rightMenuButton.setImageResource(R.drawable.ic_history_white_24dp);
        toolbartitle.setText(R.string.task_toolbar_title);

        serviceDao = new ServiceDao(WebAPI.URL);
    }

    private void bindWidget(View rootview) {
        mainContent = (LinearLayout) rootview.findViewById(R.id.mainContent);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        cameraImageview = (ImageView) rootview.findViewById(R.id.cameraButton);
        imageImageview = (ImageView) rootview.findViewById(R.id.imageButton);
        imageView = (ImageView) rootview.findViewById(R.id.imageView);
        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        rightMenuButton = (ImageView) rootview.findViewById(R.id.rightMenu);
        imageView = (ImageView) rootview.findViewById(R.id.image);
        taskDateTextview = (TextView) rootview.findViewById(R.id.taskDateTextview);
        galleryRecyclerView = (RecyclerView) rootview.findViewById(R.id.galleryRecyclerView);
        saveButton = (Button) rootview.findViewById(R.id.saveButton);
        titleEditText = (EditText) rootview.findViewById(R.id.titleTaskEditText);
        hourEditText = (EditText) rootview.findViewById(R.id.houreEditText);
        descriptionEditText = (EditText) rootview.findViewById(R.id.descriptionEditText);

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
            case R.id.taskDateTextview:
                openDateDialog();
                break;
            case R.id.saveButton:
                saveTask();
                break;

        }
    }

    private void saveTask() {

       if(FormValidation.isEmpty(titleEditText, getActivity())) return;
       if(FormValidation.isEmpty(hourEditText, getActivity())) return;
       if(FormValidation.isEmpty(titleEditText, getActivity())) return;

        String date = taskDateTextview.getText().toString();
        String oldateFormat = "dd/mm/yyyy";
        String newFormatDate = "yyyy-mm-dd";
        date = DateUtil.changeFormatDate(oldateFormat, newFormatDate, date);

        progressBar.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);
        serviceDao.addTask(ShareData.getUserProfile(), titleEditText.getText().toString(), date, hourEditText.getText().toString(), descriptionEditText.getText().toString(), imageList, this);


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
                    setImageToGallery();
                }
                break;
            case REQUEST_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    setImageToGallery(data);

                    break;
                }
        }
    }

    private void setImageToGallery(Intent data) {
        if (data != null) {
            ClipData clipdata = data.getClipData();
            Image image = null;
            if(clipdata != null) {
                for (int i = 0; i < clipdata.getItemCount(); i++) {
                    ClipData.Item item = clipdata.getItemAt(i);
                    Uri uri = item.getUri();
                    image = new Image(uri.getPath());
                    image.setUri(uri);
                    image.setProtocol("file://");
                    imageList.add(image);
                }
            } else {
                Uri uri = data.getData();
                image = new Image(uri.getPath());
                image.setUri(uri);
                image.setProtocol("file://");
                imageList.add(image);
            }
            adapter.notifyDataSetChanged();

        }
    }

    private void setImageToGallery() {
        Image image = new Image(imageUrlTemp);
        image.setProtocol("file://");
        imageList.add(image);
        adapter.notifyDataSetChanged();
    }

    public void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right)
                .replace(R.id.fragmentContainer, TaskHistoryFragment.newInstance(ShareData.getUserProfile().getUser_id()),"TaskHistory")
                .addToBackStack(null)
                .commit();
    }

    private void openDateDialog() {
        DialogFragment dateDialog = DatePickerFragment.getInstance(getActivity(), taskDateTextview);
        dateDialog.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onSuccess(String result) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(0, 0, R.anim.from_left, R.anim.to_right)
                        .replace(R.id.fragmentContainer, TaskHistoryFragment.newInstance(ShareData.getUserProfile().getUser_id()))
                        .addToBackStack("taskHistory")
                        .commit();
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
                WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.default_message_dialog), getString(R.string.default_label_dialog_button), getActivity());
                Log.d("Task", result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLostConnection() {
        progressBar.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.default_message_dialog), getString(R.string.default_label_dialog_button), getActivity());

    }

    @Override
    public void onUnreachHost() {
        progressBar.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.default_message_dialog), getString(R.string.default_label_dialog_button), getActivity());

    }
}
