package com.example.display;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.display.data.LikeData;
import com.example.display.data.RoomDB;
import com.example.display.databinding.ActivityMainBinding;
import com.example.display.databinding.NumberPickerBinding;
import com.example.display.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener, View.OnClickListener {

    private ActivityMainBinding binding;
    private String moveOn = "Move\nOn";
    private Paint mPaint;
    private TextToSpeech tts;
    private int mColor = Color.WHITE;
    private int mBackColor = Color.BLACK;
    private NumberPickerBinding numberPickerBinding;
    private Dialog dialog;
    private LikeAdapter adapter;
    private RoomDB database;
    private List<LikeData> dataList = new ArrayList<>();
    private int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.tvPreView.setTextColor(mColor);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        database = RoomDB.getInstance(this);
        loadData();

        adapter = new LikeAdapter(dataList, MainActivity.this);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new LikeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                final LikeData data = dataList.get(pos);
                binding.tvPreView.setText(data.getText());
                binding.tvPreView.setTextColor(data.getTextColor());
                binding.tvPreView.setBackgroundColor(data.getBackColor());
            }
        });
        mPaint = new Paint();
        binding.tvMoveOnOff.setText(moveOn);
        binding.tvMoveOnOff.setOnClickListener(this);
        binding.tvColor.setOnClickListener(this);
        binding.tvBackColor.setOnClickListener(this);
        binding.tvTextSize.setOnClickListener(this);
        binding.tvRead.setOnClickListener(this);
        binding.rlStart.setOnClickListener(this);
        binding.llLike.setOnClickListener(this);

        binding.et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textItem = binding.et.getText().toString();
                binding.tvPreView.setText(textItem);
            }
        });
    }

    private void loadData() {
        dataList.clear();
        dataList.addAll(database.likeDao().getAll());
    }

    @Override
    public void colorChanged(String type, int color) {
        LikeData data = new LikeData();
        switch (type) {
            case ColorPickerDialog.TYPE_BACKGROUND:
                mBackColor = color;
                data.setBackColor(color);
                binding.tvPreView.setBackgroundColor(color);
                break;
            default:
                mColor = color;
                data.setTextColor(color);
                binding.tvPreView.setTextColor(color);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int viewType = view.getId();
        switch (viewType) {
            case R.id.tvMoveOnOff:
                String text = binding.tvMoveOnOff.getText().toString();
                if (text.equals(moveOn)) {
                    binding.tvMoveOnOff.setText("Move\nOff");
                    binding.tvMoveOnOff.setBackgroundColor(Color.RED);
                    binding.tvPreView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    binding.tvPreView.setSelected(true);
                } else {
                    binding.tvMoveOnOff.setText(moveOn);
                    binding.tvMoveOnOff.setBackgroundColor(Color.BLUE);
                    binding.tvPreView.setEllipsize(TextUtils.TruncateAt.END);
                }
                break;
            case R.id.tvColor:
                ColorPickerDialog.getInstance(MainActivity.this)
                        .setListener(MainActivity.this)
                        .setType(ColorPickerDialog.TYPE_TEXT)
                        .setInitialColor(mPaint.getColor())
                        .show();
                break;
            case R.id.tvBackColor:
                ColorPickerDialog.getInstance(MainActivity.this)
                        .setListener(MainActivity.this)
                        .setType(ColorPickerDialog.TYPE_BACKGROUND)
                        .setInitialColor(mPaint.getColor())
                        .show();
                break;
            case R.id.tvTextSize:
                showCustomDialog();
                break;
            case R.id.tvRead:
                String readText = binding.et.getText().toString();
                tts.speak(readText, TextToSpeech.QUEUE_FLUSH, null, "uid");
                break;
            case R.id.rlStart:
                CommonUtils.showToast(MainActivity.this, "StartDisplay");
                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                String value = binding.tvPreView.getText().toString();
                intent.putExtra("text", value);
                intent.putExtra("textColor", mColor);
                intent.putExtra("backColor", mBackColor);
                startActivity(intent);
                break;

            case R.id.llLike:
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
                binding.llLike.startAnimation(animation);
                String sText = binding.et.getText().toString().trim();
                if (!sText.equals("")) {
                    LikeData data = new LikeData(sText, mColor, mBackColor);
                    database.likeDao().insert(data);
                    loadData();
                    adapter.notifyDataSetChanged();
                }
        }
    }

    private void showCustomDialog() {
        numberPickerBinding = NumberPickerBinding.inflate(getLayoutInflater());
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(numberPickerBinding.getRoot());
        numberPickerBinding.numberPicker.setMaxValue(50);
        numberPickerBinding.numberPicker.setMinValue(40);
        numberPickerBinding.numberPicker.setWrapSelectorWheel(true);
        numberPickerBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSize = numberPickerBinding.numberPicker.getValue();
                binding.tvPreView.setTextSize(textSize);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}