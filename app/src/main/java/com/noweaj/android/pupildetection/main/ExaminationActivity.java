package com.noweaj.android.pupildetection.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.noweaj.android.pupildetection.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ExaminationActivity extends AppCompatActivity {

    RadioGroup rg;
    RadioGroup rg2;
    RadioGroup rg3;
    Gson gson;
    int number;
    InputStream inputStream;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);

        Bundle b = getIntent().getExtras();
        number = b.getInt("num");

        LinearLayout linearLayout = findViewById(R.id.container1);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        gson = new Gson();

        if (number == 1) {
            inputStream = getResources().openRawResource(R.raw.q_video1);
        } else if (number == 2){
            inputStream = getResources().openRawResource(R.raw.q_video1);
        } else if (number == 3){
            inputStream = getResources().openRawResource(R.raw.q_video3);
        } else if (number == 4){
            inputStream = getResources().openRawResource(R.raw.q_video4);
        } else if (number == 5){
            inputStream = getResources().openRawResource(R.raw.q_video5);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

//        try (reader) {

            // Convert JSON File to Java Object
            Questions questions = gson.fromJson(reader, Questions.class);
            ArrayList qs = questions.questions;
            for (int i = 0; i < qs.size(); i++) {
                Question q = (Question) qs.get(i);
                TextView textView = new TextView(this);
                textView.setText(q.label);
                textView.setGravity(Gravity.TOP);
                textView.setBackgroundColor(Color.WHITE);
                textView.setPadding(10,0,10,0);
                textView.setTextSize(20);
                linearLayout.addView(textView);
                ArrayList choices = q.choices;
                for (int j = 0; j < choices.size(); j++) {
                    rg = new RadioGroup(getApplicationContext());
                    rg.setOrientation(RadioGroup.VERTICAL);
                    RadioButton rb = new RadioButton(this);
                    Choice ch = (Choice) choices.get(j);
                    rb.setText(ch.label);
                    rg.addView(rb);
                    rg.setGravity(Gravity.END);
                    rg.setPadding(10,0,10,0);
                    linearLayout.addView(rg);
                }
            }

            System.out.println(questions);

//        } catch (IOException e) {
//            e.printStackTrace();
//        }



//        TextView textView = new TextView(this);
//        textView.setText("دلیل اینکه بیشتر خواب ها رو فراموش میکنیم چیست؟");
//        textView.setGravity(Gravity.TOP);
//        textView.setBackgroundColor(Color.WHITE);
//        textView.setPadding(10,0,10,0);
//        textView.setTextSize(20);
//        linearLayout.addView(textView);
//
//        rg = new RadioGroup(getApplicationContext());
//        rg.setOrientation(RadioGroup.VERTICAL);
//        RadioButton rb = new RadioButton(this);
//        rb.setText("خاطرات ما نمیتونن با اطلاعات زیادی درگیر باشند.");
//        rg.addView(rb);
//        RadioButton rb1 = new RadioButton(this);
//        rb1.setText("ممکن است در مورد اینکه چی واقعیت هست چی رویا اشتباه کنیم.");
//        rg.addView(rb1);
//        RadioButton rb9 = new RadioButton(this);
//        rb9.setText("چون اهمیتی ندارند.");
//        rg.addView(rb9);
//        rg.setGravity(Gravity.END);
//        rg.setPadding(10,0,10,0);
//        linearLayout.addView(rg);
//
//        TextView textView2 = new TextView(this);
//        textView2.setText("دو نفر در مورد خواب هایی که آینده رو پیش بینی میکنند چه توافق نظری دارند؟");
//        textView2.setGravity(Gravity.TOP);
//        textView2.setBackgroundColor(Color.WHITE);
//        textView2.setPadding(10,10,10,0);
//        textView2.setTextSize(20);
//        linearLayout.addView(textView2);
//
//        rg2 = new RadioGroup(getApplicationContext());
//        rg2.setOrientation(RadioGroup.VERTICAL);
//        RadioButton rb2 = new RadioButton(this);
//        rb2.setText("ممکن است خواب ها فقط تصادفی باشند.");
//        rg2.addView(rb2);
//        RadioButton rb3 = new RadioButton(this);
//        rb3.setText("فقط با انواع خاصی از محرک ها و رویداد ها اتفاق میفتند.");
//        rg2.addView(rb3);
//        RadioButton rb4 = new RadioButton(this);
//        rb4.setText("بیشتر از آنچه برخی افراد فکر میکنند اتفاق میفتند.");
//        rg2.addView(rb4);
//        rg2.setGravity(Gravity.END);
//        rg2.setPadding(10,0,10,0);
//        linearLayout.addView(rg2);
//
//        TextView textView3 = new TextView(this);
//        textView3.setText("سوزی درباره مطالعه\u200Cای روی کودکان پیش\u200Cدبستانی که چرت کوتاهی در روز داشتند، معتقد است که:");
//        textView3.setGravity(Gravity.TOP);
//        textView3.setBackgroundColor(Color.WHITE);
//        textView3.setPadding(10,10,10,0);
//        textView3.setTextSize(20);
//        linearLayout.addView(textView3);
//
//        rg3 = new RadioGroup(getApplicationContext());
//        rg3.setOrientation(RadioGroup.VERTICAL);
//        RadioButton rb6 = new RadioButton(this);
//        rb6.setText("نتوانست به هیچ نتیجه روشنی برسد.");
//        rg3.addView(rb6);
//        RadioButton rb7 = new RadioButton(this);
//        rb7.setText("نتایج بحث برانگیزی داشت.");
//        rg3.addView(rb7);
//        RadioButton rb8 = new RadioButton(this);
//        rb8.setText("از روش تحقیق نادرست استفاده کرد.");
//        rg3.addView(rb8);
//        rg3.setGravity(Gravity.END);
//        rg3.setPadding(10,0,10,0);
//        linearLayout.addView(rg3);
    }

    public class Questions {

        public ArrayList<Question> questions;

    }

    public class Question {
        public String label;
        public ArrayList<Choice> choices;
    }

    public class Choice {
        public String label;
    }

}
