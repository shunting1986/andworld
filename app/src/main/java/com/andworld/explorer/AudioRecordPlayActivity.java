package com.andworld.explorer;

import java.io.IOException;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.Manifest;

public class AudioRecordPlayActivity extends Activity {
  private static final String TAG = AudioRecordPlayActivity.class.getSimpleName();
  private RecordButton recordButton;
  private PlayButton playButton;
  private String audioFilePath;
  private final int REQUEST_CODE_RECORD_AUDIO = 618;

  class RecordButton {
    private Button button;
    private boolean status;
    private MediaRecorder mediaRecorder;

    private void startRecording() {
      Log.i(TAG, "start recording...");
      mediaRecorder = new MediaRecorder();
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      mediaRecorder.setOutputFile(audioFilePath);
      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      try {
        mediaRecorder.prepare();
      } catch (IOException ioe) {
        Log.e(TAG, "Fail to prepare media recorder", ioe);
        return;
      }
      mediaRecorder.start();
    }

    private void stopRecording() {
      Log.i(TAG, "stop recording...");
      mediaRecorder.stop();
      mediaRecorder.release();
      mediaRecorder = null;
    }

    private void release() {
      if (mediaRecorder != null) {
        stopRecording();
      }
    }

    public RecordButton(Button button) {
      this.button = button;
      button.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (status) {
            stopRecording();
          } else {
            startRecording();
          }
          setState(!status);
        }
      });
      setState(false);
    }

    private void setState(boolean recording) {
      status = recording;
      if (recording) {
        button.setText("Stop recording");
      } else {
        button.setText("Start recording");
      }
    }
  }

  class PlayButton {
    private Button button;
    private boolean status;
    private MediaPlayer mediaPlayer;

    private void startPlaying() {
      Log.i(TAG, "start playing...");
      mediaPlayer = new MediaPlayer();
      try {
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
      } catch (IOException ioe) {
        Log.e(TAG, "Fail to prepare media player", ioe);
        return;
      }
      mediaPlayer.start();
    }

    private void stopPlaying() {
      Log.i(TAG, "stop playing...");
      mediaPlayer.release();
      mediaPlayer = null;
    }

    public PlayButton(Button button) {
      this.button = button;
      button.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
	        if (status) {
	          stopPlaying();
	        } else {
	          startPlaying();
	        }
	        setState(!status);
        }
      });
      setState(false);
    }

    private void setState(boolean playing) {
      status = playing;
      if (playing) {
        button.setText("Stop playing");
      } else {
        button.setText("Start playing");
      }
    }

    private void release() {
      if (mediaPlayer != null) {
        stopPlaying();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        finish();
      }
    }
  }

  @Override
  protected void onCreate(android.os.Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.audio_record_play);

    audioFilePath = getExternalCacheDir().getAbsolutePath() + "/audio_record_test.3gp";

    ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.RECORD_AUDIO},
          REQUEST_CODE_RECORD_AUDIO);
   
    recordButton = new RecordButton(
      (Button) findViewById(R.id.audio_record_button));
    playButton = new PlayButton(
      (Button) findViewById(R.id.audio_play_button));
  }

  @Override
  protected void onStop() {
    super.onStop();
    recordButton.release();
    playButton.release();
  }
}
