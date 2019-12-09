package com.suong99.gianphoidoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    MediaPlayer mediaPlayer;

    private Socket mSocket;
    RadioButton rdThuVao;
    RadioButton rdDayRa;
    ToggleButton tgBtnDieuKhien;
    TextView txtTrangThaiPhoiDo;
    Switch switchDieuKhien;

    {
        try {
//            mSocket = IO.socket("http://192.168.0.109:3000/");
            mSocket = IO.socket("https://gianphoidoonline.herokuapp.com/");
        } catch (URISyntaxException e) {
            System.out.println("Loi: '} catch (URISyntaxException e) {' + " + e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        mSocket.connect();
        mSocket.on("server_gui_trang_thai_DC", JSON_STATUSDC);

        rdDayRa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("android_gui_dieuKhienDC", "dayRa");
            }
        });

        rdThuVao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("android_gui_dieuKhienDC", "thuVao");
            }
        });

        tgBtnDieuKhien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSocket.emit("android_gui_Control", true);
                } else {
                    mSocket.emit("android_gui_Control", false);
                }
            }
        });

        switchDieuKhien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSocket.emit("android_gui_Control", true);
                } else {
                    mSocket.emit("android_gui_Control", false);
                }
            }
        });
    }

    private void AnhXa() {
        rdThuVao = findViewById(R.id.rdThuVao);
        rdDayRa = findViewById(R.id.rdDayRa);
        tgBtnDieuKhien = findViewById(R.id.tgBtnDieuKhien);
        txtTrangThaiPhoiDo = findViewById(R.id.txtTrangThaiPhoiDo);
        switchDieuKhien = findViewById(R.id.switchDieuKhien);
    }

    private Emitter.Listener JSON_STATUSDC = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        int valueRainSensor = data.getInt("valueRainSensor");
                        int valueCongTac1 = data.getInt("valueCongTac1");
                        int valueCongTac2 = data.getInt("valueCongTac2");
                        int voice = data.getInt("voice");
                        System.out.println(valueRainSensor + "-" + valueCongTac1 + "-" + valueCongTac2 + "-" + voice);
                        if (valueRainSensor == 1 && valueCongTac1 == 1 && valueCongTac2 == 0 && voice == 0) {
                            rdThuVao.setChecked(false);
                            rdDayRa.setChecked(true);
                            txtTrangThaiPhoiDo.setText("Đang Phơi Đồ Kìa Anh ơi");
                            txtTrangThaiPhoiDo.setTextColor(Color.rgb(16, 201, 22));
                        } else if (valueRainSensor == 0 && valueCongTac1 == 0 && valueCongTac2 == 1 && voice == 0) {
                            txtTrangThaiPhoiDo.setText("Đồ đã lấy vào rồi kìa");
                            txtTrangThaiPhoiDo.setTextColor(Color.RED);
                            rdThuVao.setChecked(true);
                            rdDayRa.setChecked(false);
                        } else if (valueRainSensor == 1 && valueCongTac1 == 1 && valueCongTac2 == 0 && voice == 1) {
                            rdThuVao.setChecked(false);
                            rdDayRa.setChecked(true);
                            txtTrangThaiPhoiDo.setText("Đang Phơi Đồ Kìa Anh ơi");
                            txtTrangThaiPhoiDo.setTextColor(Color.rgb(16, 201, 22));
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.phoido);
                            mediaPlayer.start();
                        } else if (valueRainSensor == 0 && valueCongTac1 == 0 && valueCongTac2 == 1 && voice == 1) {
                            txtTrangThaiPhoiDo.setText("Đồ đã lấy vào rồi kìa");
                            txtTrangThaiPhoiDo.setTextColor(Color.RED);
                            rdThuVao.setChecked(true);
                            rdDayRa.setChecked(false);
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.laydo);
                            mediaPlayer.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void voice(View view) {
        //Create an Intent with “RecognizerIntent.ACTION_RECOGNIZE_SPEECH” action//

        {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            try {

                //Start the Activity and wait for the response//

                startActivityForResult(intent, REQUEST_CODE);
            } catch (ActivityNotFoundException a) {

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (result.get(0).contains("lấy")) {
                        Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();
                        mSocket.emit("android_gui_dieuKhienDC", "thuVaoVoice");
                    } else if (result.get(0).contains("phơi")) {
                        Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();
                        mSocket.emit("android_gui_dieuKhienDC", "dayRaVoice");
                    }
                }
                break;
            }

        }
    }
}