package com.geoway.landdetect.retrofitrxjavadownloaddemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geoway.landdetect.retrofitrxjavadownloaddemo.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainContact.MainView {
    private MainPersenter mPersenter;
    private AppCompatSeekBar seekbar;
    private ProgressBar progressBar;
    private Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPersenter = new MainPersenter();
        mPersenter.bindView(this);
        initView();
    }

    private void initView() {
        seekbar = (AppCompatSeekBar) findViewById(R.id.seekbar);
        seekbar.setMax(100);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        but = (Button) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (but.getText().equals("开始")) {
                    mPersenter.startDownload();
                } else {
                    mPersenter.pause();
                }
            }
        });
    }

    @Override
    public void updataProgress(int progress) {
        seekbar.setProgress(progress);
        progressBar.setProgress(progress);
    }

    @Override
    public void onComplete() {
        if (but != null) {
            but.setText("开始");
        }
    }

    @Override
    public void onStartDown() {
        if (but != null) {
            but.setText("暂停");
        }
    }

    @Override
    public void chageState(int state) {

    }

    @Override
    public boolean checkPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 111);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersenter.unBindView();
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
