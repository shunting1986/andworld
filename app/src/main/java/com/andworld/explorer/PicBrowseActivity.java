package com.andworld.explorer;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;
import android.widget.ImageView;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PicBrowseActivity extends Activity {
  private static final String TAG = PicBrowseActivity.class.getSimpleName();
  private final int REQUEST_CODE = 314159;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "Created activity");
    
    setContentView(R.layout.picbrowse);
    Button browseButton = (Button) findViewById(R.id.picBrowseButton);
    browseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && intent != null) {
      Log.i(TAG, "Receive call back for pic selection");

      Uri intentData = intent.getData();
      Log.d(TAG, "Intent data " + intentData.toString());

      String filePathColumnName = MediaStore.Images.Media.DATA;
      Cursor cursor = getContentResolver().query(intentData,
        new String[] { filePathColumnName}, null, null, null);
      cursor.moveToFirst();
      String filePath = cursor.getString(cursor.getColumnIndex(filePathColumnName));

      Log.d(TAG, "Path is " + filePath);
      cursor.close();

      ImageView imageView = (ImageView) findViewById(R.id.picBrowseImageView);
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(new File(filePath));
      } catch (IOException e) {
        Toast.makeText(this, "Fail to open file: " + e.toString(), Toast.LENGTH_LONG).show();
        Log.i(TAG, "Fail to open file " + filePath, e);

        // TODO handle the permission granting gracefully
        ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
          618);
        return;
      }

      Bitmap bitmap = BitmapFactory.decodeStream(fis);
      if (bitmap == null) {
        Log.i(TAG, "Fail to create bitmap");
        return;
      }

      imageView.setImageBitmap(bitmap);
    }
  }
}
