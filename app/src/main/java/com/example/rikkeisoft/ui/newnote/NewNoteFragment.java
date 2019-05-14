package com.example.rikkeisoft.ui.newnote;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.ui.adapter.ImageAdapter;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.ui.menu.MenuFragment;
import com.example.rikkeisoft.util.DateUtils;
import com.example.rikkeisoft.util.Define;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;


public class NewNoteFragment extends BaseFragment implements NewNoteView, View.OnClickListener,ImageAdapter.ImageOnClickListener{
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
    private Note note;
    private NewNotePresenterImp imp;
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
        imp = new NewNotePresenterImp(this);
        mURLImage = new ArrayList<>();
        imageAdapter = new ImageAdapter();
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_newnote;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void initView() {

        note = new Note();
        getToolbar().setVisibility(View.VISIBLE);
        onclickCamera();
        onClickColor();
        onClickSave();
        onClickBack();
        setOnChangedTitle();
        tvDate.setText(DateUtils.getTimeByPattern("dd/MM/YYYY hh:mm"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerImage.setLayoutManager(gridLayoutManager);
        recyclerImage.setAdapter(imageAdapter);
        imageAdapter.setImages(mURLImage);
        imageAdapter.setImageOnclickListener(this);


    }

    private void onclickCamera() {
        getToolbar().onClickCamera(v -> {
            showDialogCamera();
        });
    }

    private void onClickColor() {
        getToolbar().onClickColor(v -> {
            showDiaLogBackground();
        });
    }

    private void onClickSave() {
        getToolbar().onClickSave(v -> {
            insertNote();

        });
    }

    private void onClickBack() {
        getToolbar().onClickBack(v -> {

        });
    }

    private void insertNote() {
        note.setTitle(etTitle.getText().toString().trim());
        note.setContent(etContent.getText().toString().trim());
        note.setCreateDate(Calendar.getInstance().getTime());
        note.setColor(colorNote);
        imp.insertNote(note);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColorwhite:
                scrollView.setBackgroundColor(Color.WHITE);
                colorNote = Color.WHITE;
                dialogColor.dismiss();
                break;
            case R.id.btnColorblue:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR1));
                colorNote = Color.parseColor(Define.COLOR1);
                dialogColor.dismiss();
                break;
            case R.id.btnColordacbiete:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR3));
                colorNote = Color.parseColor(Define.COLOR3);
                dialogColor.dismiss();
                break;
            case R.id.btnColorpink:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR2));
                colorNote = Color.parseColor(Define.COLOR2);
                dialogColor.dismiss();
                break;
            case R.id.btnColormandarin:
                scrollView.setBackgroundColor(Color.parseColor(Define.COLOR4));
                colorNote = Color.parseColor(Define.COLOR4);
                dialogColor.dismiss();
                break;
            case R.id.ivImageGallery:
                getImageFromAlbum();
                dialogCamera.dismiss();
                break;
            case R.id.ivCameraImage:

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
        getNavigationManager().open(MenuFragment.class, null);
    }

    @Override
    public void onClickItem(String url) {

    }

    @Override
    public void onRemove(int position) {

    }
}
