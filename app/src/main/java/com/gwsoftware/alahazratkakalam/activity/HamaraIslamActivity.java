package com.gwsoftware.alahazratkakalam.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.KeyEvent;
import android.view.View;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.adapter.AudioAdapter;
import com.gwsoftware.alahazratkakalam.adapter.VolumeAdapter;
import com.gwsoftware.alahazratkakalam.asyncktask.DownloadAnyFile;
import com.gwsoftware.alahazratkakalam.interfaces.DownloadAnyFileCallback;
import com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.GridSpacingItemDecoration;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class HamaraIslamActivity extends AppCompatActivity implements recyclerViewCallback, JcPlayerManagerListener {

    RecyclerView volumetRecyclerView;
    RecyclerView audiosRecyclerView;
    VolumeAdapter volumeAdapter;
    AhApplication ahApplication;
    ArrayList<String> volumes;
    private JcPlayerView player;
    AudioAdapter audioAdapter;
    AdView adView;
    String category = Constants.HAMARA_ISLAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamara_islam);
        volumetRecyclerView = findViewById(R.id.volumes_recycler_view);
        audiosRecyclerView = findViewById(R.id.audio_recycler_view);
        player = findViewById(R.id.jcplayer);
        adView = findViewById(R.id.adView);
        Utils.loadAdView(this, adView);
        volumes = new ArrayList<>();
        ahApplication = AhApplication.getInstance();
        if (getIntent().getStringExtra("category") != null) {
            category = getIntent().getStringExtra("category");
        }
        initializeVolumeRecyclerView();

    }

    private void prepareAudioData(String volumeName) {

        try {
            HashMap<String, ArrayList<DataObjectModel.HamaraIslam>> hamaraIslamMap;
            if (category.toLowerCase().equalsIgnoreCase(Constants.HAMARA_ISLAM.toLowerCase())) {
                hamaraIslamMap = ahApplication.getDataObjectModel().getHamaraislam();
            } else {
                hamaraIslamMap = ahApplication.getDataObjectModel().getNaats();
            }
            ArrayList<JcAudio> jcAudios = new ArrayList<>();
            if (hamaraIslamMap != null && hamaraIslamMap.size() > 0) {
                ArrayList<DataObjectModel.HamaraIslam> volumeData = hamaraIslamMap.get(volumeName);
                for (DataObjectModel.HamaraIslam audioData : volumeData) {
                    jcAudios.add(JcAudio.createFromURL(audioData.getName(), audioData.getUrl()));
                }
            }
            player.initPlaylist(jcAudios, this);
            adapterSetup();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void initializeVolumeRecyclerView() {
        int spacing = 10; // 50px
        boolean includeEdge = true;
        volumetRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        volumetRecyclerView.setLayoutManager(gridLayoutManager);
        volumeAdapter = new VolumeAdapter(HamaraIslamActivity.this, getVolumes());
        volumetRecyclerView.setAdapter(volumeAdapter);
        player.setVisibility(View.GONE);
    }

    protected void adapterSetup() {
        audioAdapter = new AudioAdapter(this, player.getMyPlaylist());
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (new File(Constants.AUDIO_FOLDER + player.getMyPlaylist().get(position).getTitle() + ".mp3").exists()) {
                    JcAudio jsAudio = JcAudio.createFromFilePath(player.getMyPlaylist().get(position).getTitle(), new File((Constants.AUDIO_FOLDER + player.getMyPlaylist().get(position).getTitle() + ".mp3")).getAbsolutePath());
                    player.playAudioFromLocalFile(jsAudio);
                } else {
                    player.playAudio(player.getMyPlaylist().get(position));
                }

            }

            @Override
            public void onPlayClicked(int position) {

            }

            @Override
            public void onFileDownload(final int position, View view) {
                JcAudio audio = player.getMyPlaylist().get(position);
                if (audio != null) {
                    new DownloadAnyFile(HamaraIslamActivity.this, Constants.AUDIO_FOLDER, audio.getPath(), ".mp3", audio.getTitle(), new DownloadAnyFileCallback() {
                        @Override
                        public void downloadCallback() {
                            audioAdapter.notifyItemChanged(position);
                            //Toast.makeText(HamaraIslamActivity.this, "downloaded", Toast.LENGTH_SHORT).show();
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        audiosRecyclerView.setLayoutManager(layoutManager);
        audiosRecyclerView.setAdapter(audioAdapter);

        ((SimpleItemAnimator) audiosRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    @Override
    public void onPause() {
        super.onPause();
        player.createNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.createNotification();
    }

    private void removeItem(int position) {
        ((SimpleItemAnimator) audiosRecyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        player.removeAudio(player.getMyPlaylist().get(position));
        audioAdapter.notifyItemRemoved(position);
        ((SimpleItemAnimator) audiosRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void recyclerViewItemClicked(int position, View view) {
        volumetRecyclerView.setVisibility(View.GONE);
        audiosRecyclerView.setVisibility(View.VISIBLE);
        player.setVisibility(View.VISIBLE);
        prepareAudioData(view.getTag().toString());
    }

    private ArrayList<String> getVolumes() {
        ArrayList<String> volumes = new ArrayList<>();

        if (ahApplication.getDataObjectModel() != null) {
            Set<String> keys;
            if (category.toLowerCase().equalsIgnoreCase(Constants.NAATS.toLowerCase())) {
                keys = ahApplication.getDataObjectModel().getNaats().keySet();
            } else {
                keys = ahApplication.getDataObjectModel().getHamaraislam().keySet();
            }


            ArrayList<String> arrayList = new ArrayList<String>();
            for (String str : keys) {
                arrayList.add(str);
            }

            volumes = arrayList;
            Collections.sort(volumes);
        }
        return volumes;
    }


    @Override
    public void onPreparedAudio(JcStatus status) {

    }

    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onPaused(JcStatus status) {

    }

    @Override
    public void onContinueAudio(JcStatus status) {

    }

    @Override
    public void onPlaying(JcStatus status) {

    }

    @Override
    public void onTimeChanged(JcStatus status) {
        updateProgress(status);
    }

    @Override
    public void onStopped(JcStatus status) {

    }

    @Override
    public void onJcpError(Throwable throwable) {

    }

    private void updateProgress(final JcStatus jcStatus) {
       /* Log.d(TAG, "Song duration = " + jcStatus.getDuration()
                + "\n song position = " + jcStatus.getCurrentPosition());*/

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // calculate progress
                float progress = (float) (jcStatus.getDuration() - jcStatus.getCurrentPosition())
                        / (float) jcStatus.getDuration();
                progress = 1.0f - progress;
                audioAdapter.updateProgress(jcStatus.getJcAudio(), progress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (audiosRecyclerView.getVisibility() == View.VISIBLE) {
                audiosRecyclerView.setVisibility(View.GONE);
                volumetRecyclerView.setVisibility(View.VISIBLE);
                stopAudioPlayer();
            } else {
                finish();
            }

        }
        return true;

    }

    private void stopAudioPlayer() {
        if (player != null) {
            player.setVisibility(View.GONE);
            player.createNotification();
            player.kill();
        }
    }

}
