package com.cookandroid.ai_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String NAVER_SEARCH_URL = "https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=";
    String naverSearchUrl = NAVER_SEARCH_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VoiceRecoActivity.class);
                intent.putExtra("api_key", "네이티브 앱 키 입력");
                startActivityForResult(intent,0);
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        ArrayList<String> results = data.getStringArrayListExtra(VoiceRecoActivity.EXTRA_KEY_RESULT_ARRAY); //결과값 모음
        String strResult = results.get(0); //결과값 중에서 가장 정확도가 높은 값(첫번째 값)

        naverSearchUrl = NAVER_SEARCH_URL + strResult; //주소창 뒤에 결과값 붙임

        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(naverSearchUrl));
        startActivity(intent2); //네이버 검색 실행*/
    }
}