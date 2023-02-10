package com.gavin.asmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

//    public void onEditorAction(TextView v, int actionId, KeyEvent event) {
//        realtimecoverage.String.valueOf(v.getText());
//        String.valueOf(actionId);
//    }
}
