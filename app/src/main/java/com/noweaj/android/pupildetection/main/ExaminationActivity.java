package com.noweaj.android.pupildetection.main;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.noweaj.android.pupildetection.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExaminationActivity extends AppCompatActivity {

    RadioGroup rg;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        LinearLayout linearLayout = findViewById(R.id.container1);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        InputStream is = getResources().openRawResource(R.raw.questions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        for (int i = 0; i < 4; i++) {
            try {
                String s;
                rg = new RadioGroup(getApplicationContext());
                rg.setOrientation(RadioGroup.VERTICAL);
                while ((s = reader.readLine()) != null) {
                    RadioButton rb = new RadioButton(this);
                    rb.setText(s);
                    rg.addView(rb);
                }
                rg.setGravity(Gravity.END);
                rg.setPadding(10, 0, 10, 0);

                linearLayout.addView(rg);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
