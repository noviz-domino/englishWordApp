
package com.voca.englishwordapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {

    private List<String[]> wordList = new ArrayList<>();
    private int currentIndex = 0;
    private TextView wordText, meaningText;
    private Button nextButton;
    private AdView adViewTop, adViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordText = findViewById(R.id.wordText);
        meaningText = findViewById(R.id.meaningText);
        nextButton = findViewById(R.id.nextButton);

        // 광고 초기화
        MobileAds.initialize(this, initializationStatus -> {});

        adViewTop = findViewById(R.id.adViewTop);
        adViewBottom = findViewById(R.id.adViewBottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewTop.loadAd(adRequest);
        adViewBottom.loadAd(adRequest);

        wordText = findViewById(R.id.wordText);
        meaningText = findViewById(R.id.meaningText);
        nextButton = findViewById(R.id.nextButton);
        //광고코드 끝.

        loadWordsFromCSV();
        if (!wordList.isEmpty()) {
            showWord(currentIndex);
        }

        nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % wordList.size();
            showWord(currentIndex);
        });
    }

    private void loadWordsFromCSV() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("words.csv"))
            );
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 헤더 줄 건너뜀
                }
                // "day1,resume,이력서" 형태
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    String word = parts[1].trim();
                    String meaning = parts[2].trim();
                    wordList.add(new String[]{word, meaning});
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWord(int index) {
        String[] entry = wordList.get(index);
        wordText.setText(entry[0]);
        meaningText.setText(entry[1]);
    }
}
