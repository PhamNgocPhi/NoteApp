package com.example.rikkeisoft.ui.newnote;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.ui.adapter.ImageAdapter;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.util.CommonUtils;
import com.example.rikkeisoft.util.DateUtils;
import com.example.rikkeisoft.util.Define;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;


public class NewNoteFragment extends BaseFragment implements NewNoteView, View.OnClickListener, ImageAdapter.ImageOnClickListener {
    private Dialog dialogColor;
    private Dialog dialogCamera;
    private Button btnColorwhite;
    private Button btnColorblue;
    private Button btnColorpink;
    private Button btnColormandarin;
    private Button btnColordacbiet;
    private ImageView ivImageGallery;
    private ImageView ivCameraImage;
    private int colorNote;
    private List<String> mURLImage;
    private NewNotePresenterImp newNotePresenterImp;
    private ImageAdapter imageAdapter;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etContent)
    EditText etContent;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.recyclerImage)
    RecyclerView recyclerImage;


    public NewNoteFragment() {
        newNotePresenterImp = new NewNotePresenterImp(this);
        mURLImage = new ArrayList<>();
        imageAdapter = new ImageAdapter();
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_newnote;
    }

    @Override
    public boolean onBackPressed() {
        //back về màn hình trước
        getNavigationManager().navigateBack(null);
        return false;
}

    @Override
    protected void initView() {
        initToolbar();
        setOnChangedTitle();
        tvDate.setText(DateUtils.getTimeByPattern("dd/MM/YYYY hh:mm"));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerImage.setLayoutManager(gridLayoutManager);
        recyclerImage.setAdapter(imageAdapter);
        imageAdapter.setImages(mURLImage);
        imageAdapter.setImageOnclickListener(this);


    }

    private void initToolbar() {
        getToolbar().setVisibility(View.VISIBLE);
        getToolbar()
                .onClickCamera(v -> {
                    askForPermission();

                })
                .onClickColor(v -> showDiaLogBackground())
                .onClickSave(v -> insertNote())
                .onClickBack(v -> {
                    //back về màn hình trước
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                });
    }

    private void insertNote() {
        Note note = new Note();
        note.setTitle(etTitle.getText().toString().trim());
        note.setContent(etContent.getText().toString().trim());
        note.setCreateDate(Calendar.getInstance().getTime());
        note.setColor(colorNote);
        note.setUrls(mURLImage);
        newNotePresenterImp.insertNote(note);

    }

    private void showDiaLogBackground() {
        dialogColor = new Dialog(getContext());
        dialogColor.setCancelable(true);
        dialogColor.setContentView(R.layout.layout_dialog_colornote);
        dialogColor.show();

        btnColorwhite = dialogColor.findViewById(R.id.btnColorwhite);
        btnColorblue = dialogColor.findViewById(R.id.btnColorblue);
        btnColorpink = dialogColor.findViewById(R.id.btnColorpink);
        btnColormandarin = dialogColor.findViewById(R.id.btnColormandarin);
        btnColordacbiet = dialogColor.findViewById(R.id.btnColordacbiete);

        btnColorblue.setOnClickListener(this);
        btnColordacbiet.setOnClickListener(this);
        btnColormandarin.setOnClickListener(this);
        btnColorpink.setOnClickListener(this);
        btnColorwhite.setOnClickListener(this);


    }

    private void showDialogCamera() {
        dialogCamera = new Dialog(getContext());
        dialogCamera.setCancelable(true);
        dialogCamera.setContentView(R.layout.layout_dialog_insertpicture);
        dialogCamera.show();

        ivImageGallery = dialogCamera.findViewById(R.id.ivImageGallery);
        ivCameraImage = dialogCamera.findViewById(R.id.ivCameraImage);

        ivImageGallery.setOnClickListener(this);
        ivCameraImage.setOnClickListener(this);


    }

    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, Define.RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    private void addImageToCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Define.CAMERA_PIC_REQUEST);

    }
    private void previewImage(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse(url), Define.TYPE_IMAGE);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (reqCode == Define.RESULT_LOAD_IMAGE) {
                final Uri imageUri = data.getData();
                mURLImage.add(imageUri.toString());
                imageAdapter.setImages(mURLImage);
                imageAdapter.notifyDataSetChanged();

            } else if (reqCode == Define.CAMERA_PIC_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                mURLImage.add(CommonUtils.getImageUri(getContext(), image).toString());
                imageAdapter.setImages(mURLImage);
                imageAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColorwhite:
                scrollView.setBackgroundColor(Color.WHITE);
                colorNote = Color.WHITE;
                dialogColor.dismiss();
                break;
            case R.id.btnColorblue:
                scrollView.setBackgroundResource(R.color.btncolorblue);
                colorNote = getResources().getColor(R.color.btncolorblue);
                dialogColor.dismiss();
                break;
            case R.id.btnColordacbiete:
                scrollView.setBackgroundResource(R.color.btncolordacbiet);
                colorNote = getResources().getColor(R.color.btncolordacbiet);
                dialogColor.dismiss();
                break;
            case R.id.btnColorpink:
                scrollView.setBackgroundResource(R.color.btnColorpink);
                colorNote = getResources().getColor(R.color.btnColorpink);
                dialogColor.dismiss();
                break;
            case R.id.btnColormandarin:
                scrollView.setBackgroundResource(R.color.btncolormandarin);
                colorNote =getResources().getColor(R.color.btncolormandarin);
                dialogColor.dismiss();
                break;
            case R.id.ivImageGallery:
                getImageFromAlbum();
                dialogCamera.dismiss();
                break;
            case R.id.ivCameraImage:
                addImageToCamera();
                dialogCamera.dismiss();
                break;
            default:
                break;
        }
    }

    private void setOnChangedTitle() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getToolbar().setTvTitle(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void backMenu() {
        getNavigationManager().navigateBack(null);
    }

    @Override
    public void onClickItem(String url) {
        previewImage(url);

    }

    @Override
    public void onRemove(int position) {
        mURLImage.remove(position);
        imageAdapter.setImages(mURLImage);
        imageAdapter.notifyDataSetChanged();

    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                         Manifest.permission.CAMERA,
                         Manifest.permission.WRITE_EXTERNAL_STORAGE,
                         Manifest.permission.READ_EXTERNAL_STORAGE
                },Define.MY_PERMISSIONS_REQUEST_ACCOUNTS);
            } else {
                showDialogCamera();
            }
        } else {
            showDialogCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Define.MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialogCamera();
                } else {
                    Toast.makeText(getContext(), R.string.permission, Toast.LENGTH_SHORT).show();
                    //System.exit(0);
                }
                break;
            default:
                break;
        }
    }
}

