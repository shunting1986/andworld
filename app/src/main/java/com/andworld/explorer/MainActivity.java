package com.andworld.explorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TextView tv = new TextView(this);
    tv.setText("Hello, this is andworld explorer!");
    setContentView(tv);
  }
}
