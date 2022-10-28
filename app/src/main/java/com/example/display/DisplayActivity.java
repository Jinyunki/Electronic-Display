package com.example.display;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;

import com.example.display.databinding.ActivityDisplayBinding;

import java.util.Locale;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityDisplayBinding binding;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayBinding.inflate(getLayoutInflater());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(binding.getRoot());
        initView();
    }
    private void initView() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        Intent getIntent = getIntent();
        String text = getIntent.getStringExtra("text");
        int textColor = getIntent.getIntExtra("textColor",0);
        int backColor = getIntent.getIntExtra("backColor",1);

        binding.tvPreView.setText(text);
        binding.tvPreView.setTextColor(textColor);
        binding.tvPreView.setBackgroundColor(backColor);
        binding.tvPreView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        binding.tvPreView.setSelected(true);

        binding.ivClose.setOnClickListener(this);
        binding.ivSound.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int viewType = view.getId();
        switch (viewType) {
            case R.id.ivClose:
                finish();
                break;
            case R.id.ivSound:
                String readText = binding.tvPreView.getText().toString();
                tts.speak(readText, TextToSpeech.QUEUE_FLUSH, null, "uid");
                break;
        }
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
}