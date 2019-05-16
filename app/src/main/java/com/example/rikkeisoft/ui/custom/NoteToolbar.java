package com.example.rikkeisoft.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rikkeisoft.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteToolbar extends RelativeLayout {

    private String textTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.ivNote)
    ImageView ivNote;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivColor)
    ImageView ivColor;

    @BindView(R.id.ivCamera)
    ImageView ivCamera;

    @BindView(R.id.ivSave)
    ImageView ivSave;

    @BindView(R.id.ivAdd)
    ImageView ivAdd;


    public NoteToolbar(Context context) {
        super(context);
        initView(context);
    }

    public NoteToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NoteToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public NoteToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_toolbar, this, true);
        ButterKnife.bind(this, view);
        textTitle =  tvTitle.getText().toString().trim();
        ivBack.setVisibility(GONE);
        ivCamera.setVisibility(GONE);
        ivColor.setVisibility(GONE);
        ivSave.setVisibility(GONE);
        ivAdd.setVisibility(GONE);
    }

    public NoteToolbar onClickBack(OnClickListener listener) {
        ivBack.setVisibility(VISIBLE);
        if (listener != null) {
            ivBack.setOnClickListener(listener);
        }
        return this;
    }

    public NoteToolbar onClickNote(OnClickListener listener) {
        ivNote.setVisibility(VISIBLE);
        if (listener != null) {
            ivNote.setOnClickListener(listener);
        }
        return this;
    }

    public NoteToolbar onClickColor(OnClickListener listener) {
        ivColor.setVisibility(VISIBLE);
        if (listener != null) {
            ivColor.setOnClickListener(listener);
        }
        return this;
    }

    public NoteToolbar onClickCamera(OnClickListener listener) {
        ivCamera.setVisibility(VISIBLE);
        if (listener != null) {
            ivCamera.setOnClickListener(listener);
        }
        return this;
    }

    public NoteToolbar onClickSave(OnClickListener listener) {
        ivSave.setVisibility(VISIBLE);
        ivAdd.setVisibility(GONE);
        if (listener != null) {
            ivSave.setOnClickListener(listener);
        }
        return this;
    }

    public NoteToolbar onClickAdd(OnClickListener listener) {
        ivAdd.setVisibility(VISIBLE);
        if (listener != null) {
            ivAdd.setOnClickListener(listener);
        }
        return this;
    }
    public void setTvTitle(CharSequence title){
        tvTitle.setText(title);
    }
    public NoteToolbar setVisibilityIvBack(){
        ivBack.setVisibility(GONE);
        return this;
    }
    public NoteToolbar setVisibilityIvColor(){
        ivColor.setVisibility(GONE);
        return this;
    }
    public NoteToolbar setVisibilityIvCamera(){
        ivCamera.setVisibility(GONE);
        return this;
    }
    public NoteToolbar setVisibilityIvSave(){
        ivSave.setVisibility(GONE);
        return this;
    }
    public NoteToolbar clearTitle(){
        tvTitle.setText(textTitle);
        return this;
    }

}
