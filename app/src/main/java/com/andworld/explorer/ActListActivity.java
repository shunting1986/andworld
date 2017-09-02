package com.andworld.explorer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.util.Log;
import android.content.Intent;

// TODO be able to swith to the default activity automatically
public class ActListActivity extends Activity {
  private static final String TAG = ActListActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ListView lv = new ListView(this);
    final Class[] clsList = {
      MainActivity.class, // first sample
      PicBrowseActivity.class,
      AudioRecordPlayActivity.class,
    };
    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, clsList);
    lv.setAdapter(adapter);
    lv.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Clicked...");
        Intent intent = new Intent(ActListActivity.this, clsList[position]);
        startActivity(intent);
      }
    });

    setContentView(lv);
  }
}
