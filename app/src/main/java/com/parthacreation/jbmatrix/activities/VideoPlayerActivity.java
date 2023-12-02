package com.parthacreation.jbmatrix.activities;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.parthacreation.jbmatrix.databinding.ActivityVideoPlayerBinding;
import com.parthacreation.jbmatrix.utils.PermissionManager;
import com.parthacreation.jbmatrix.utils.VideoDownloader;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.List;


public class VideoPlayerActivity extends AppCompatActivity {
    private ActivityVideoPlayerBinding binding;
    private int currentIndex = 0;
    private List<Uri> uriList; // Your list of URIs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Assuming you have populated uriList with the URIs
        uriList = VideoDownloader.uriList;

        // Set the initial video to play
        playVideo(uriList.get(currentIndex));
    }

    private void playVideo(Uri uri) {
        binding.videoView.setVideoURI(uri);
        binding.videoView.setOnCompletionListener(mp -> {
            // Check if there's another video in the list
            if (currentIndex < uriList.size() - 1) {
                // Play the next video
                currentIndex++;
                playVideo(uriList.get(currentIndex));
            } else {
                // All videos played
                Toast.makeText(VideoPlayerActivity.this, "All videos played", Toast.LENGTH_SHORT).show();
            }
        });

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoView);
        mediaController.setMediaPlayer(binding.videoView);
        binding.videoView.setMediaController(mediaController);

        binding.videoView.start(); // Start playing the video
    }

}