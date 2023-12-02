package com.parthacreation.jbmatrix.utils;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.TextView;
import com.parthacreation.jbmatrix.activities.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class VideoDownloader {
    public static List<Uri> uriList = new ArrayList<>();

    private static final int PROGRESS_BAR_MAX = 100;
    static int downloadIndex = 0;

    public static void downloadVideos(Context context, List<String> videoUrls, TextView progressTextView, TextView downloadCountTV) {
        // Download videos sequentially
        downloadVideo(context, videoUrls, progressTextView,downloadCountTV);

    }

    private static void downloadVideo(Context context, List<String> videoUrls, TextView progressTextView,TextView downloadCountTV) {
        if(downloadIndex<videoUrls.size()) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Constants.baseVideoUrl + videoUrls.get(downloadIndex)));
            //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoUrls.get(downloadIndex));
            request.setDestinationInExternalFilesDir(context,context.getCacheDir().getAbsolutePath(),videoUrls.get(downloadIndex));

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = manager.enqueue(request);

            // Start monitoring download progress
            checkDownloadStatus(context, manager, downloadId, progressTextView, downloadCountTV, videoUrls);
        }

    }

    private static void checkDownloadStatus(Context context, DownloadManager manager, long downloadId, TextView progressTextView,TextView downloadCountTV,List<String> videoUrls) {
         new Thread(() -> {
            boolean downloading = true;

            while (downloading) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);

                try (Cursor cursor = manager.query(query)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int totalSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                        int downloadedSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

                        int status = cursor.getInt(statusIndex);
                        Thread.sleep(80);

                        switch (status) {
                            case DownloadManager.STATUS_SUCCESSFUL:
                                downloading = false;
                                uriList.add(manager.getUriForDownloadedFile(downloadId));
                                // Update progress to 100% when download is complete
                                downloadIndex++;
                                updateProgressOnUI(progressTextView, 100,downloadCountTV);
                                Thread.sleep(100);
                                if(downloadIndex<videoUrls.size())
                                    downloadVideo(context, videoUrls, progressTextView,downloadCountTV);
                                else startVideoActivity(context);
                                break;
                            case DownloadManager.STATUS_FAILED:
                                downloading = false;
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                // Handle paused state
                                break;
                            case DownloadManager.STATUS_PENDING:
                                // Handle pending state
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                int totalSize = cursor.getInt(totalSizeIndex);
                                int downloaded = cursor.getInt(downloadedSizeIndex);

                                if (totalSize > 0) {
                                    float progress = (downloaded * 1.0f / totalSize) * PROGRESS_BAR_MAX;
                                    // Update TextView with download progress
                                    updateProgressOnUI(progressTextView, (int) progress,downloadCountTV);
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static void startVideoActivity(Context context){
        context.startActivity(new Intent(context, VideoPlayerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private static void updateProgressOnUI(TextView textView, int progress,TextView downloadCountTV) {
        // Ensure to update TextView on the UI thread
        textView.post(() -> textView.setText("Download Completed - " + progress + "%"));
        downloadCountTV.post(() -> downloadCountTV.setText(String.valueOf(downloadIndex)));
    }
}


