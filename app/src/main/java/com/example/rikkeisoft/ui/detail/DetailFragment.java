package com.example.rikkeisoft.ui.detail;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.example.rikkeisoft.util.DateUtils;
import com.example.rikkeisoft.util.Define;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class DetailFragment extends BaseFragment implements DetailView, View.OnClickListener,ImageAdapter.ImageOnClickListener {

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.recyclerImage)
    RecyclerView recyclerImage;

    @BindView(R.id.etTitleUpdate)
    EditText etTitleUpdate;

    @BindView(R.id.etContentUpdate)
    EditText etContentUpdate;

    @BindView(R.id.tvDateUpdate)
    TextView tvDateUpdate;
    @BindView(R.id.ivAlarm)
    ImageView ivAlarm;

    @BindView(R.id.ivPreviouitem)
    ImageView ivPreviouitem;

    @BindView(R.id.ivShare)
    ImageView ivShare;

    @BindView(R.id.ivDiscard)
    ImageView ivDiscard;

    @BindView(R.id.ivNext)
    ImageView ivNext;

    private Dialog dialogColor;
    private Dialog dialogCamera;
    private Button btnColorwhite;
    private Button btnColorblue;
    private Button btnColorpink;
    private Button btnColormandarin;
    private Button btnColordacbiet;
    private ImageView ivImageGallery;
    private ImageView ivCameraImage;
    private int colorNoteUpdate;
    private int noteIDUpdate;
    private int currentPosition;
    private List<Note> notes;
    private Date currentTime;
    private List<String> mURLImage;
    private DetailPresenterImp detailPresenterImp;
    private ImageAdapter imageAdapter;
    private Note curentNote;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_detail;
    }

    @Override
    public boolean onBackPressed() {
        getNavigationManager().navigateBack(null);
        return false;
    }

    @Override
    protected void initView() {
        initToolbar();
        notes = new ArrayList<>();
        mURLImage = new ArrayList<>();
        detailPresenterImp = new DetailPresenterImp(this);
        notes = detailPresenterImp.getAllNote();
        currentPosition = noteIDUpdate;

        imageAdapter = new ImageAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerImage.setLayoutManager(gridLayoutManager);
        recyclerImage.setAdapter(imageAdapter);
        imageAdapter.setImages(mURLImage);
        imageAdapter.setImageOnclickListener(this);
        getNoteUpdate(currentPosition);
        setOnChangedTitle();
        ivNext.setOnClickListener(this);
        ivPreviouitem.setOnClickListener(this);
        ivDiscard.setOnClickListener(this);


    }

    @Override
    public void handleReceivedData(Bundle bundle) {
        super.handleReceivedData(bundle);
        noteIDUpdate = bundle.getInt(Define.ID);
    }

    private void initToolbar() {
        getToolbar().setVisibility(View.VISIBLE);
        getToolbar()
                .onClickCamera(v -> {
                    askForPermission();

                })
                .onClickColor(v -> showDiaLogBackground())
                .onClickSave(v -> updatetNote())
                .onClickBack(v -> {
                    //back về màn hình trước
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                });
    }

    private void getNoteUpdate(int currentPosition) {
        curentNote = notes.get(currentPosition);
        tvDateUpdate.setText(DateUtils.partDateToString(curentNote.getCreateDate()).trim());
        scrollView.setBackgroundColor(curentNote.getColor());
        etTitleUpdate.setText(curentNote.getTitle().trim());
        etContentUpdate.setText(curentNote.getContent().trim());
        colorNoteUpdate  = curentNote.getColor();
        getToolbar().setTvTitle(curentNote.getTitle().trim());
        mURLImage.addAll(curentNote.getUrls());
        imageAdapter.setImages(curentNote.getUrls());


    }

    private void updatetNote() {
        Note note = new Note(curentNote);
        note.setTitle(etTitleUpdate.getText().toString().trim());
        note.setContent(etContentUpdate.getText().toString().trim());
        note.setCreateDate(Calendar.getInstance().getTime());
        note.setColor(colorNoteUpdate);
        note.setUrls(mURLImage);
        detailPresenterImp.updateNote(note);
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
                mURLImage.add(DateUtils.getImageUri(getContext(), image).toString());
                imageAdapter.setImages(mURLImage);
                imageAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpForEditNote() {
        if (notes.size() == 1) {
            setImageButtonNextEnable(false);
            setImageButtonPreviousEnable(false);
        } else if (currentPosition == notes.size() - 1) {
            setImageButtonPreviousEnable(true);
            setImageButtonNextEnable(false);
        } else if (currentPosition == 0) {
            setImageButtonNextEnable(true);
            setImageButtonPreviousEnable(false);
        } else {
            setImageButtonNextEnable(true);
            setImageButtonPreviousEnable(true);
        }
    }

    public void setImageButtonPreviousEnable(boolean isEnable) {
        if (isEnable) {
            ivPreviouitem.setAlpha(255);
            ivPreviouitem.setEnabled(true);
        } else {
            ivPreviouitem.setAlpha(60);
            ivPreviouitem.setEnabled(false);
        }


    }


    public void setImageButtonNextEnable(boolean isEnable) {
        ivNext.setAlpha(isEnable ? 255 : 60);
        ivNext.setEnabled(isEnable);
    }
    private void updateActionBottom(String action) {

        if (Define.NEXT_NOTE.equals(action) && currentPosition < notes.size() - 1) {
            currentPosition = currentPosition + 1;
        } else if (Define.PREVIOUS_NOTE.equals(action) && currentPosition > 0) {
            currentPosition = currentPosition - 1;
        }
        setUpForEditNote();
        getNoteUpdate(currentPosition);
    }
    public void showDeleteNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialogInterface, i) -> detailPresenterImp.deleteNote(currentPosition));
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, etContentUpdate.getText().toString());
        sendIntent.setType(Define.TYPE_SHARE);
        startActivity(Intent.createChooser(sendIntent, getString(R.string.text_share)));
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
                }, Define.MY_PERMISSIONS_REQUEST_ACCOUNTS);
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
        //fixme trong trường hợp chỉ có 1 điều kiện thế này thì nên dùng if
        switch (requestCode) {
            case Define.MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialogCamera();
                } else {
                    Toast.makeText(getContext(), R.string.permission, Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        //fixme color khai báo trong file color.xml chứ không đặt trong define
        switch (v.getId()) {
            case R.id.btnColorwhite:
                scrollView.setBackgroundColor(Color.WHITE);
                colorNoteUpdate = Color.WHITE;
                dialogColor.dismiss();
                break;
            case R.id.btnColorblue:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR1));
                colorNoteUpdate = Color.parseColor(Define.COLOR1);
                dialogColor.dismiss();
                break;
            case R.id.btnColordacbiete:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR3));
                colorNoteUpdate = Color.parseColor(Define.COLOR3);
                dialogColor.dismiss();
                break;
            case R.id.btnColorpink:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR2));
                colorNoteUpdate = Color.parseColor(Define.COLOR2);
                dialogColor.dismiss();
                break;
            case R.id.btnColormandarin:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR4));
                colorNoteUpdate = Color.parseColor(Define.COLOR4);
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
            case R.id.ivPreviouitem:
                updateActionBottom(Define.PREVIOUS_NOTE);
                break;
            case R.id.ivNext:
                updateActionBottom(Define.NEXT_NOTE);
                break;
            case R.id.ivDiscard:
                showDeleteNoteDialog();
                break;
            case R.id.ivShare:
                share();
                break;
            default:
                break;
        }
    }
    private void setOnChangedTitle() {
        etTitleUpdate.addTextChangedListener(new TextWatcher() {
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
    public void onClickItem(String url) {
        previewImage(url);
    }

    @Override
    public void onRemove(int position) {
        mURLImage.remove(position);
        imageAdapter.setImages(mURLImage);
        imageAdapter.notifyDataSetChanged();

    }

    @Override
    public void backMenu() {
        getNavigationManager().navigateBack(null);
    }
}
