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

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.ivNote)
    ImageView ivNote;

    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

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

        ivBack.setVisibility(GONE);
    }

    public NoteToolbar onClickBack(OnClickListener listener) {
        ivBack.setVisibility(VISIBLE);
        if(listener != null) {
            ivBack.setOnClickListener(listener);
        }
        return this;
    }

}
