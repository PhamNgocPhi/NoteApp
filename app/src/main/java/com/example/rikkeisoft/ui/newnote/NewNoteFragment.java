package com.example.rikkeisoft.ui.newnote;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
    //Fixme biến note này a thấy chỉ dùng trong 1 hàm thì không cần khai bao trên này
    private Note note;
    //Fixme tên này nên để là presenter, để là imp thì tên không gợi lên ý nghĩa gì cả
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
        //FIXME để back về màn hình trước sẽ gọi thế này
        getNavigationManager().navigateBack(null);
        return false;
    }

    @Override
    protected void initView() {
//        getToolbar().setVisibility(View.VISIBLE);
//        onclickCamera();
//        onClickColor();
//        onClickSave();
//        onClickBack();
        initToolbar();
        setOnChangedTitle();
        tvDate.setText(DateUtils.getTimeByPattern("dd/MM/YYYY hh:mm"));

        note = new Note();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerImage.setLayoutManager(gridLayoutManager);
        recyclerImage.setAdapter(imageAdapter);
        imageAdapter.setImages(mURLImage);
        imageAdapter.setImageOnclickListener(this);


    }

    //FIXME Vì toolbar được viết theo kiểu singleton nên không cần tách hàm nhỏ ra mà sẽ viết thế này
    private void initToolbar() {
        getToolbar().setVisibility(View.VISIBLE);
        getToolbar()
                .onClickCamera(v -> showDialogCamera())
                .onClickColor(v -> showDiaLogBackground())
                .onClickSave(v -> insertNote())
                .onClickBack(v -> {
                    //Fixme back ở toolbar sẽ viết thế này
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                });
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
        //Fixme khi back về sẽ không gọi hàm open
        //nếu gọi hàm open khi back thế này thì backstack sẽ là menu -> newNote -> menu. Ở menu cuối khi bấm back sẽ về newnote
        //getNavigationManager().open(MenuFragment.class, null);
        //đúng sẽ phải thế này
        getNavigationManager().navigateBack(null);
    }

    @Override
    public void onClickItem(String url) {

    }

    @Override
    public void onRemove(int position) {

    }
}
