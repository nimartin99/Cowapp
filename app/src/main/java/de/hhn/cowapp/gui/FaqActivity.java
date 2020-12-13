package de.hhn.cowapp.gui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hhn.cowapp.R;
import de.hhn.cowapp.gui.ExpandableListAdapterFAQ;

/**
 * @author Mergim Miftari
 */
public class FaqActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_cowapp);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#142850")));
        setContentView(R.layout.activity_faq);

        listView = (ExpandableListView) findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapterFAQ(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add(getString(R.string.question1));
        listDataHeader.add(getString(R.string.question2));
        listDataHeader.add(getString(R.string.question3));
        listDataHeader.add(getString(R.string.question4));
        listDataHeader.add(getString(R.string.question5));
        listDataHeader.add(getString(R.string.question6));
        listDataHeader.add(getString(R.string.question7));
        listDataHeader.add(getString(R.string.question8));
        listDataHeader.add(getString(R.string.question9));
        listDataHeader.add(getString(R.string.question10));
        listDataHeader.add(getString(R.string.question11));
        listDataHeader.add(getString(R.string.question12));
        listDataHeader.add(getString(R.string.question13));

        List<String> answer1 = new ArrayList<>();
        List<String> answer2 = new ArrayList<>();
        List<String> answer3 = new ArrayList<>();
        List<String> answer4 = new ArrayList<>();
        List<String> answer5 = new ArrayList<>();
        List<String> answer6 = new ArrayList<>();
        List<String> answer7 = new ArrayList<>();
        List<String> answer8 = new ArrayList<>();
        List<String> answer9 = new ArrayList<>();
        List<String> answer10 = new ArrayList<>();
        List<String> answer11 = new ArrayList<>();
        List<String> answer12 = new ArrayList<>();
        List<String> answer13 = new ArrayList<>();

        answer1.add(getString(R.string.answer1));
        answer2.add(getString(R.string.answer2));
        answer3.add(getString(R.string.answer3));
        answer4.add(getString(R.string.answer4));
        answer5.add(getString(R.string.answer5));
        answer6.add(getString(R.string.answer6));
        answer7.add(getString(R.string.answer7));
        answer8.add(getString(R.string.answer8));
        answer9.add(getString(R.string.answer9));
        answer10.add(getString(R.string.answer10));
        answer11.add(getString(R.string.answer11));
        answer12.add(getString(R.string.answer12));
        answer13.add(getString(R.string.answer13));

        listHash.put(listDataHeader.get(0), answer1);
        listHash.put(listDataHeader.get(1), answer2);
        listHash.put(listDataHeader.get(2), answer3);
        listHash.put(listDataHeader.get(3), answer4);
        listHash.put(listDataHeader.get(4), answer5);
        listHash.put(listDataHeader.get(5), answer6);
        listHash.put(listDataHeader.get(6), answer7);
        listHash.put(listDataHeader.get(7), answer8);
        listHash.put(listDataHeader.get(8), answer9);
        listHash.put(listDataHeader.get(9), answer10);
        listHash.put(listDataHeader.get(10), answer11);
        listHash.put(listDataHeader.get(11), answer12);
        listHash.put(listDataHeader.get(12), answer13);
    }
}