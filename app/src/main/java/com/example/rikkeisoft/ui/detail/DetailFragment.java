package com.example.rikkeisoft.ui.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.data.model.Note;
import com.example.rikkeisoft.service.SchedulingService;
import com.example.rikkeisoft.ui.adapter.ImageAdapter;
import com.example.rikkeisoft.ui.base.BaseFragment;
import com.example.rikkeisoft.util.CommonUtils;
import com.example.rikkeisoft.util.DateUtils;
import com.example.rikkeisoft.util.Define;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class DetailFragment extends BaseFragment implements DetailView, View.OnClickListener, ImageAdapter.ImageOnClickListener, AdapterView.OnItemSelectedListener {

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

    @BindView(R.id.tvAlarm)
    TextView tvAlarm;

    @BindView(R.id.ivPreviouitem)
    ImageView ivPreviouitem;

    @BindView(R.id.ivShare)
    ImageView ivShare;

    @BindView(R.id.ivDiscard)
    ImageView ivDiscard;

    @BindView(R.id.ivNext)
    ImageView ivNext;

    @BindView(R.id.llSpinner)
    LinearLayout llSpinner;

    @BindView(R.id.spnHour)
    Spinner spnHour;

    @BindView(R.id.spnDateday)
    Spinner spnDay;

    @BindView(R.id.btnSaveAlarm)
    Button btnSaveAlarm;

    private Dialog dialogColor;
    private Dialog dialogCamera;
    private Button btnColorwhite;
    private Button btnColorblue;
    private Button btnColorpink;
    private Button btnColormandarin;
    private Button btnColordacbiet;
    private LinearLayout llCamera;
    private LinearLayout llAlbum;

    private int colorNoteUpdate;
    private int noteIDUpdate;
    private int currentPosition;
    private List<Note> notes;
    private List<String> mURLImage;
    private DetailPresenterImp detailPresenterImp;
    private ImageAdapter imageAdapter;
    private Note curentNote;

    private ArrayAdapter<String> mDayAdapter;
    private ArrayAdapter<String> mHourAdapter;

    private String hourAlarm;
    private String dayAlarm;

    private String days[] = {"Day", "Today", "Tomorrow", "After Tomorrow", "Other"};
    private String hours[] = {"Time", "08:00", "12:00", "16:00", "20:00", "Other"};
    private static final String PREVIOUS_NOTE = "PREVIOUS_NOTE";
    private static final String NEXT_NOTE = "NEXT_NOTE";
    private String pictureImagePath;
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
        setUpForEditNote();
        imageAdapter.setImageOnclickListener(this);
        getNoteUpdate(currentPosition);
        setOnChangedTitle();
        ivNext.setOnClickListener(this);
        ivPreviouitem.setOnClickListener(this);
        ivDiscard.setOnClickListener(this);
        ivShare.setOnClickListener(this);


        mDayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, days);
        mDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnDay.setAdapter(mDayAdapter);

        mHourAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, hours);
        mHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnHour.setAdapter(mHourAdapter);

        spnDay.setOnItemSelectedListener(this);
        spnHour.setOnItemSelectedListener(this);
        ivAlarm.setOnClickListener(this);
        btnSaveAlarm.setOnClickListener(this);


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
        colorNoteUpdate = curentNote.getColor();
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

        llCamera = dialogCamera.findViewById(R.id.llCamera);
        llAlbum = dialogCamera.findViewById(R.id.llAlbum);

        llCamera.setOnClickListener(this);
        llAlbum.setOnClickListener(this);


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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                pictureImagePath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(
                        getContext(),
                        "com.example.rikkeisoft.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Define.CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File  image = File.createTempFile(timeStamp,".jpg",storageDir);
        return image;
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
                Bitmap imageBitmap = BitmapFactory.decodeFile(pictureImagePath);
                mURLImage.add(CommonUtils.getImageUri(getContext(), imageBitmap).toString());
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

        if (NEXT_NOTE.equals(action) && currentPosition < notes.size() - 1) {
            currentPosition = currentPosition + 1;
        } else if (PREVIOUS_NOTE.equals(action) && currentPosition > 0) {
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
        builder.setPositiveButton("OK", (dialogInterface, i) -> detailPresenterImp.deleteNote(notes.get(currentPosition).getId()));
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType(Define.TYPE_SHARE);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, etTitleUpdate.getText().toString());
        sendIntent.putExtra(Intent.EXTRA_TEXT, etContentUpdate.getText().toString());
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
        if (Define.MY_PERMISSIONS_REQUEST_ACCOUNTS == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogCamera();
            } else {
                Toast.makeText(getContext(), R.string.permission, Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColorwhite:
                scrollView.setBackgroundColor(Color.WHITE);
                colorNoteUpdate = Color.WHITE;
                dialogColor.dismiss();
                break;
            case R.id.btnColorblue:
                scrollView.setBackgroundResource(R.color.btncolorblue);
                colorNoteUpdate = getResources().getColor(R.color.btncolorblue);
                dialogColor.dismiss();
                break;
            case R.id.btnColordacbiete:
                scrollView.setBackgroundResource(R.color.btncolordacbiet);
                colorNoteUpdate = getResources().getColor(R.color.btncolordacbiet);
                dialogColor.dismiss();
                break;
            case R.id.btnColorpink:
                scrollView.setBackgroundResource(R.color.btnColorpink);
                colorNoteUpdate = getResources().getColor(R.color.btnColorpink);
                dialogColor.dismiss();
                break;
            case R.id.btnColormandarin:
                scrollView.setBackgroundResource(R.color.btncolormandarin);
                colorNoteUpdate = getResources().getColor(R.color.btncolormandarin);
                dialogColor.dismiss();
                break;
            case R.id.llAlbum:
                getImageFromAlbum();
                dialogCamera.dismiss();
                break;
            case R.id.llCamera:
                addImageToCamera();
                dialogCamera.dismiss();
                break;
            case R.id.ivPreviouitem:
                updateActionBottom(PREVIOUS_NOTE);
                break;
            case R.id.ivNext:
                updateActionBottom(NEXT_NOTE);
                break;
            case R.id.ivDiscard:
                showDeleteNoteDialog();
                break;
            case R.id.ivShare:
                share();
                break;
            case R.id.ivAlarm:
                tvAlarm.setVisibility(View.GONE);
                llSpinner.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSaveAlarm:
                showAlarmDialog();
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


    private void setAlarmNote() {
        long timesInMillis = DateUtils.parseDateToMilisecond(dayAlarm + " " + hourAlarm);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), SchedulingService.class);
        intent.putExtra(Define.KEY_TYPE, notes.get(currentPosition).getTitle());
        intent.putExtra(Define.KEY_ID, notes.get(currentPosition).getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(), notes.get(currentPosition).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager
                    .setExact(AlarmManager.RTC_WAKEUP, timesInMillis, pendingIntent);
        } else {
            alarmManager
                    .set(AlarmManager.RTC_WAKEUP, timesInMillis, pendingIntent);
        }
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
            hourAlarm = hourOfDay + ":" + minute1;
            hours[0] = hourAlarm;
            mHourAdapter.notifyDataSetChanged();
            spnHour.setSelection(0);

        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void showDataPickerDiaLog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            dayAlarm = dayOfMonth + "/" + monthOfYear + "/" + year1;
            days[0] = dayAlarm;
            mDayAdapter.notifyDataSetChanged();

        }, year, month, day);
        datePickerDialog.show();
    }

    public void showAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alarm Note");
        builder.setMessage("Alarm Date: " + dayAlarm + " " + hourAlarm);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialogInterface, i) -> setAlarmNote());
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spnDateday) {
            switch (position) {
                case 1:
                    dayAlarm = DateUtils.addDate(0);
                    days[0] = dayAlarm;
                    mDayAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Date Alarm: " + dayAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    dayAlarm = DateUtils.addDate(1);
                    days[0] = dayAlarm;
                    mDayAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Date Alarm: " + dayAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dayAlarm = DateUtils.addDate(2);
                    days[0] = dayAlarm;
                    mDayAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Date Alarm: " + dayAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    showDataPickerDiaLog();
                    break;
                default:
                    break;
            }
        } else if (parent.getId() == R.id.spnHour) {
            switch (position) {
                case 1:
                    hourAlarm = hours[1];
                    hours[0] = hourAlarm;
                    mHourAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Hour Alarm: " + hourAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    hourAlarm = hours[2];
                    hours[0] = hourAlarm;
                    mHourAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Hour Alarm: " + hourAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    hourAlarm = hours[3];
                    hours[0] = hourAlarm;
                    mHourAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Hour Alarm: " + hourAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    hourAlarm = hours[4];
                    hours[0] = hourAlarm;
                    mHourAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Hour Alarm: " + hourAlarm, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    showTimePickerDialog();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
