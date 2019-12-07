package com.suong99.gianphoidoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    RadioButton rdThuVao;
    RadioButton rdDayRa;
    ToggleButton tgBtnDieuKhien;
    TextView txtTrangThaiPhoiDo;

    {
        try {
            mSocket = IO.socket("http://192.168.1.7:3000/");
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
    }

    private void AnhXa() {
        rdThuVao = findViewById(R.id.rdThuVao);
        rdDayRa = findViewById(R.id.rdDayRa);
        tgBtnDieuKhien = findViewById(R.id.tgBtnDieuKhien);
        txtTrangThaiPhoiDo = findViewById(R.id.txtTrangThaiPhoiDo);
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
                        System.out.println(valueRainSensor + "-" + valueCongTac1 + "-" + valueCongTac2);
                        if (valueRainSensor == 1 && valueCongTac1 == 1 && valueCongTac2 == 0) {
                            rdThuVao.setChecked(false);
                            rdDayRa.setChecked(true);
                            txtTrangThaiPhoiDo.setText("Đang Phơi Đồ Kìa Anh Yêu");
                            txtTrangThaiPhoiDo.setTextColor(Color.rgb(16, 201, 22));
                        } else if (valueRainSensor == 0 && valueCongTac1 == 0 && valueCongTac2 == 1) {
                            txtTrangThaiPhoiDo.setText("Đồ đã lấy vào rồi kìa");
                            txtTrangThaiPhoiDo.setTextColor(Color.RED);
                            rdThuVao.setChecked(true);
                            rdDayRa.setChecked(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
