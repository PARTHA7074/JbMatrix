package com.parthacreation.jbmatrix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.parthacreation.jbmatrix.databinding.ActivityMainBinding;
import com.parthacreation.jbmatrix.models.VideoItem;
import com.parthacreation.jbmatrix.models.VideoTabResponse;
import com.parthacreation.jbmatrix.services.RetrofitAPI;
import com.parthacreation.jbmatrix.services.VideoApiService;
import com.parthacreation.jbmatrix.utils.Constants;
import com.parthacreation.jbmatrix.utils.MyProgressDialog;
import com.parthacreation.jbmatrix.utils.PermissionManager;
import com.parthacreation.jbmatrix.utils.VideoDownloader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<String> videoUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.downloadWaitingTV.setText("Please wait for the download to finish\n" +
                "Video Will start playing after downloading " +Constants.maxVideoDownloadCount+" video");

        if(PermissionManager.checkPermission(this)){
            fetchVideoTab();
        }
    }

    private void fetchVideoTab(){
        MyProgressDialog progressDialog = new MyProgressDialog(binding.downloadingCard);
        progressDialog.show();

        VideoApiService videoApiService = RetrofitAPI.getClient().create(VideoApiService.class);
        Call<VideoTabResponse> call = videoApiService.getVideoTab(13);

        call.enqueue(new Callback<VideoTabResponse>() {
            @Override
            public void onResponse(Call<VideoTabResponse> call, retrofit2.Response<VideoTabResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    progressDialog.dismiss();
                    VideoTabResponse videoTabResponse = response.body();
                    binding.totalDownloadCountTV.setText(String.valueOf(videoTabResponse.getData().size()));
                    int i = 0;
                    for (VideoItem item : videoTabResponse.getData()){
                        if(item.getVideoPresent() != null && item.getVideoPresent().equals("yes")) {
                            if (i++ == Constants.maxVideoDownloadCount) break;
                            videoUrls.add(item.getVideo());
                        }
                    }
                    VideoDownloader.downloadVideos(getApplicationContext(),videoUrls,binding.completedDownloadTV,binding.completedDownloadCountTV);
                }
            }

            @Override
            public void onFailure(Call<VideoTabResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Filed to load video tab", Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        PermissionManager.checkPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults,this)){
            fetchVideoTab();
        }
    }
}