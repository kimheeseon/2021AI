//main 화면(가사를 말해 주세요)
package com.cookandroid.ai_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, SpeechRecognizeListener{
    private SpeechRecognizerClient client;
    public static Intent intent_search; //intent

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;

        // library를 초기화 합니다.
        // API를 사용할 시점이 되었을 때 initializeLibrary(Context)를 호출한다.
        // 사용을 마치면 finalizeLibrary()를 호출해야 한다.
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.speechbutton).setOnClickListener(this);

        //아래는 인식 취소, 중지, 재인식, tts기능, kakao에서 제공하는 음성 인식 기능 관련 -> 사용하지 않을 것 -> 주석
        //findViewById(R.id.cancelbutton).setOnClickListener(this);
        //findViewById(R.id.restartbutton).setOnClickListener(this);
        //findViewById(R.id.stopbutton).setOnClickListener(this);
        //findViewById(R.id.uibutton).setOnClickListener(this);
        //findViewById(R.id.ttsbutton).setOnClickListener(this);

        setButtonsStatus(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    private void setButtonsStatus(boolean enabled) {
        findViewById(R.id.speechbutton).setEnabled(enabled);
        //findViewById(R.id.uibutton).setEnabled(enabled);
        //findViewById(R.id.restartbutton).setEnabled(!enabled);
        //findViewById(R.id.cancelbutton).setEnabled(!enabled);
        //findViewById(R.id.stopbutton).setEnabled(!enabled);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //입력 방법을 4가지(웹, 연속어, 지도, 단어) 중에 선택 가능
        //원래는 web으로 했지만 연속어(dictation)으로 변경함
        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;

        //RadioGroup serviceRadioGroup = (RadioGroup)this.findViewById(R.id.service_group);
/*        switch (serviceRadioGroup.getCheckedRadioButtonId()) {
            case R.id.web:
                serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;
                break;
            case R.id.dictation:
                serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;
                break;
            case R.id.local:
                serviceType = SpeechRecognizerClient.SERVICE_TYPE_LOCAL;
                break;
            case R.id.word:
                serviceType = SpeechRecognizerClient.SERVICE_TYPE_WORD;
                break;
        }*/

        Log.i("SpeechSampleActivity", "serviceType : " + serviceType);


        // 음성인식 버튼 listener
        if (id == R.id.speechbutton) {
            if(PermissionUtils.checkAudioRecordPermission(this)) {

                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                        setServiceType(serviceType);

                /*if (serviceType.equals(SpeechRecognizerClient.SERVICE_TYPE_WORD)) {
                    EditText words = (EditText)findViewById(R.id.words_edit);
                    String wordList = words.getText().toString();
                    builder.setUserDictionary(wordList);

                    Log.i("SpeechSampleActivity", "word list : " + wordList.replace('\n', ','));
                }*/

                client = builder.build();

                client.setSpeechRecognizeListener(this);
                client.startRecording(true);

                setButtonsStatus(false);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    intent_search = new Intent(MainActivity.this, SearchMusic.class);
                    intent_search.putExtra("check", result);
                    Log.e("heeseon_check4", result);
                    startActivity(intent_search);
                }
            }, 5000);
        }
        // 음성인식 취소버튼 listener
/*        else if (id == R.id.cancelbutton) {
            if (client != null) {
                client.cancelRecording();
            }

            setButtonsStatus(true);
        }
        // 음성인식 재시작버튼 listener
        else if (id == R.id.restartbutton) {
            if (client != null) {
                client.cancelRecording();
                client.startRecording(true);
            }
        }
        // 음성인식 중지버튼 listener
        else if (id == R.id.stopbutton) {
            if (client != null) {
                client.stopRecording();
            }
        }*/
        // 음성인식 기본 UI 버튼 listener
        /*else if (id == R.id.uibutton) {
            Intent i = new Intent(getApplicationContext(), VoiceRecoActivity.class);

            *//*if (serviceType.equals(SpeechRecognizerClient.SERVICE_TYPE_WORD)) {
                EditText words = (EditText)findViewById(R.id.words_edit);
                String wordList = words.getText().toString();

                Log.i("SpeechSampleActivity", "word list : " + wordList.replace('\n', ','));

                i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_USER_DICTIONARY, wordList);
            }*//*

            i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_SERVICE_TYPE, serviceType);

            startActivityForResult(i, 0);
        }*/
        // 음성합성 sample activity 열기 -> 우리는 TTS 기능 구현하지 않으므로 주석 처리
//        else if (id == R.id.ttsbutton) {
//            Intent i = new Intent(getApplicationContext(), TextToSpeechActivity.class);
//            startActivity(i);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(VoiceRecoActivity.EXTRA_KEY_RESULT_ARRAY);

            StringBuilder builder = new StringBuilder();

            for (String result : results) {
                builder.append(result);
                builder.append("\n");
            }
            new AlertDialog.Builder(this).
                    setMessage(builder.toString()).
                    setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).
                    show();
        }
        else if (requestCode == RESULT_CANCELED) {
            // 음성인식의 오류 등이 아니라 activity의 취소가 발생했을 때.
            if (data == null) {
                return;
            }

            int errorCode = data.getIntExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_CODE, -1);
            String errorMsg = data.getStringExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_MESSAGE);

            if (errorCode != -1 && !TextUtils.isEmpty(errorMsg)) {
                new AlertDialog.Builder(this).
                        setMessage(errorMsg).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        show();
            }
        }
    }

    @Override
    public void onReady() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onBeginningOfSpeech() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onEndOfSpeech() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        //TODO implement interface DaumSpeechRecognizeListener method
        Log.e("SpeechSampleActivity", "onError");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setButtonsStatus(true);
            }
        });

        client = null;
    }

    @Override
    public void onPartialResult(String text) {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    public String result;
    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");

        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        //texts에 builder의 0번째만 저장
        builder.append(texts.get(0));
        result = texts.get(0);
        Log.e("heeseon_result", result);

        //전체 경우(정확도 따라) 저장
        /*for (int i = 0; i < texts.size(); i++) {
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }*/
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // finishing일때는 처리하지 않는다.
                if (activity.isFinishing()) return;

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity).
                        setMessage(builder.toString()).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                setButtonsStatus(true);
            }
        });
        client = null;
    }

    @Override
    public void onAudioLevel(float v) {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onFinished() {
        Log.i("SpeechSampleActivity", "onFinished");
    }
}