package com.hardik.musix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
       mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView pause,previous,next,play;
    MediaPlayer mediaPlayer;
    ArrayList<File>songs;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
    textView=findViewById(R.id.textView);
    //pause=(ImageView) findViewById(R.id.play);
    play=(ImageView) findViewById(R.id.play);
        previous=(ImageView) findViewById(R.id.previous);
        next=(ImageView) findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();

    Bundle bundle=intent.getExtras();
    songs=(ArrayList) bundle.getParcelableArrayList("songList");
    textContent=intent.getStringExtra("currentSong");
    textView.setText(textContent);
    textView.setSelected(true);
    position=intent.getIntExtra("position",0);
    Uri uri=Uri.parse(songs.get(position).toString());
    mediaPlayer=MediaPlayer.create(this,uri);
    mediaPlayer.start();
    seekBar.setMax(mediaPlayer.getDuration());
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
        }
    });
    updateSeek=new Thread(){
        @Override
        public void run() {
            int currentosition=0;
            try {
                while (currentosition<mediaPlayer.getDuration()){
                    currentosition=mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentosition);
                    sleep(800);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
    updateSeek.start();
    play.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.play);
                mediaPlayer.pause();

            }
            else
            {
                play.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                            }
        }
    });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   mediaPlayer.stop();
                   mediaPlayer.reset();
                   mediaPlayer.release();
               }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
               if(position!=0){
                    position--;
                }
                else
                {
                    position=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(PlaySong.this,uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                play.setImageResource(R.drawable.pause);
                textContent=songs.get(position).getName();
                textView.setText(textContent);
                textView.setSelected(true);
           }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(position!=(songs.size()-1)){
                    position++;
                }
                else
                {
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(PlaySong.this,uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                play.setImageResource(R.drawable.pause);
                textContent=songs.get(position).getName();
                textView.setText(textContent);
                textView.setSelected(true);
            }
        });
    }
}