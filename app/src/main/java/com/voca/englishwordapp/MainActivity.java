package com.voca.englishwordapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // 단어 데이터 구조 클래스
    class WordItem {
        String day, word, meaning;
        WordItem(String day, String word, String meaning) {
            this.day = day; this.word = word; this.meaning = meaning;
        }
    }

    private List<WordItem> allWords = new ArrayList<>(); // 전체 데이터
    private List<WordItem> filteredWords = new ArrayList<>(); // 선택된 날짜 데이터
    private int currentIndex = 0;

    private View layoutHome, layoutDaySelection, layoutWordStudy;
    private TextView wordText, meaningText;
    private LinearLayout dayButtonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 광고 초기화
        MobileAds.initialize(this, initializationStatus -> {});
        AdView adTop = findViewById(R.id.adViewTop);
        AdView adBottom = findViewById(R.id.adViewBottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        adTop.loadAd(adRequest);
        adBottom.loadAd(adRequest);

        // 2. 뷰 초기화
        layoutHome = findViewById(R.id.layout_home);
        layoutDaySelection = findViewById(R.id.layout_day_selection);
        layoutWordStudy = findViewById(R.id.layout_word_study);
        dayButtonContainer = findViewById(R.id.day_button_container);
        wordText = findViewById(R.id.wordText);
        meaningText = findViewById(R.id.meaningText);

        // 3. CSV 데이터 로드
        loadWordsFromCSV();

        // 4. 버튼 이벤트 설정
        findViewById(R.id.btn_go_study_list).setOnClickListener(v -> showDaySelection());
        findViewById(R.id.nextButton).setOnClickListener(v -> {
            if (currentIndex < filteredWords.size() - 1) {
                currentIndex++;
                updateUI();
            }
        });
        findViewById(R.id.prevButton).setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateUI();
            }
        });
    }

    private void loadWordsFromCSV() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("words.csv")))) {
            String line;
            br.readLine(); // 헤더 무시
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 3);
                if (p.length >= 3) allWords.add(new WordItem(p[0].trim(), p[1].trim(), p[2].trim()));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showDaySelection() {
        layoutHome.setVisibility(View.GONE);
        layoutDaySelection.setVisibility(View.VISIBLE);
        layoutWordStudy.setVisibility(View.GONE);

        Set<String> days = new LinkedHashSet<>();
        for (WordItem item : allWords) days.add(item.day);

        dayButtonContainer.removeAllViews();
        for (String day : days) {
            Button b = new Button(this);
            b.setText(day);
            b.setOnClickListener(v -> {
                filteredWords.clear();
                for (WordItem item : allWords) if (item.day.equals(day)) filteredWords.add(item);
                currentIndex = 0;
                updateUI();
                layoutDaySelection.setVisibility(View.GONE);
                layoutWordStudy.setVisibility(View.VISIBLE);
            });
            dayButtonContainer.addView(b);
        }
    }

    private void updateUI() {
        if (!filteredWords.isEmpty()) {
            wordText.setText(filteredWords.get(currentIndex).word);
            meaningText.setText(filteredWords.get(currentIndex).meaning);
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutWordStudy.getVisibility() == View.VISIBLE) showDaySelection();
        else if (layoutDaySelection.getVisibility() == View.VISIBLE) {
            layoutDaySelection.setVisibility(View.GONE);
            layoutHome.setVisibility(View.VISIBLE);
        } else super.onBackPressed();
    }
}