package com.gwsoftware.alahazratkakalam.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.activity.HamaraIslamActivity;
import com.gwsoftware.alahazratkakalam.adapter.AudioAdapter;
import com.gwsoftware.alahazratkakalam.asyncktask.DownloadAnyFile;
import com.gwsoftware.alahazratkakalam.interfaces.DownloadAnyFileCallback;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class DownloadedAudio extends Fragment implements  JcPlayerManagerListener {
    RecyclerView audioRecyclerView;
    TextView noDownloadsFound;
    AudioAdapter audioAdapter;
    private JcPlayerView player;
    boolean isAudioClicked;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.downloaded_audio, container, false);
        audioRecyclerView = view.findViewById(R.id.audio_recycler_view);
        noDownloadsFound = view.findViewById(R.id.no_downloads_found);
        player = view.findViewById(R.id.jcplayer);
        player.setVisibility(View.GONE);
        initializeRecyclerView();
        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        audioRecyclerView.setLayoutManager(gridLayoutManager);
        ArrayList<JcAudio> audiosList = Utils.getAudioList();
        //audioAdapter = new AudioAdapter(getContext(), audiosList);
        //audioRecyclerView.setAdapter(audioAdapter);

        if(audiosList!=null && audiosList.size() > 0) {
            player.initPlaylist(audiosList, this);
            adapterSetup();
        }
    }

    protected void adapterSetup() {
        audioAdapter = new AudioAdapter(getContext(), player.getMyPlaylist());
        audioAdapter.setShowDelete(true);
        audioAdapter.setPlayer(player);
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                isAudioClicked = true;
                if (new File(Constants.AUDIO_FOLDER + player.getMyPlaylist().get(position).getTitle() + ".mp3").exists()) {
                    player.setVisibility(View.VISIBLE);
                    JcAudio jsAudio = JcAudio.createFromFilePath(player.getMyPlaylist().get(position).getTitle(), new File((Constants.AUDIO_FOLDER + player.getMyPlaylist().get(position).getTitle() + ".mp3")).getAbsolutePath());
                    player.playAudioFromLocalFile(jsAudio);

                }

            }

            @Override
            public void onPlayClicked(int position) {

            }

            @Override
            public void onFileDownload(final int position, View view) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        audioRecyclerView.setLayoutManager(layoutManager);
        audioRecyclerView.setAdapter(audioAdapter);

        ((SimpleItemAnimator) audioRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }


    @Override
    public void onPause() {
        super.onPause();
        player.createNotification();
        //stopAudioPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAudioPlayer();
        player.createNotification();
    }

    private void updateProgress(final JcStatus jcStatus) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float progress = (float) (jcStatus.getDuration() - jcStatus.getCurrentPosition())
                        / (float) jcStatus.getDuration();
                progress = 1.0f - progress;
                audioAdapter.updateProgress(jcStatus.getJcAudio(), progress);
            }
        });
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
    private void stopAudioPlayer(){
        if(player!=null){
            player.setVisibility(View.GONE);
            player.createNotification();
            player.kill();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            //Toast.makeText(getContext(),"...1..."+isVisibleToUser,Toast.LENGTH_SHORT).show();
            stopAudioPlayer();
        }else {
            if(isAudioClicked) {
                isAudioClicked = false;
                initializeRecyclerView();
            }
        }
    }
}