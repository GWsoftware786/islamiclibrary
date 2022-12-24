package com.gwsoftware.alahazratkakalam.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.interfaces.AlertCallback;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by jean on 27/06/16.
 */

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterViewHolder> {
    private static final String TAG = AudioAdapter.class.getSimpleName();
    private static OnItemClickListener mListener;
    private List<JcAudio> jcAudioList;
    private SparseArray<Float> progressMap = new SparseArray<>();
    Context context;
    int clickPosition = -1;

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }

    boolean isShowDelete;

    public JcPlayerView getPlayer() {
        return player;
    }

    public void setPlayer(JcPlayerView player) {
        this.player = player;
    }

    private JcPlayerView player;


    public AudioAdapter(Context context, List<JcAudio> jcAudioList) {
        this.context = context;
        this.jcAudioList = jcAudioList;
        setHasStableIds(true);
    }

    // Define the method that allows the parent activity or fragment to define the jcPlayerManagerListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public AudioAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new AudioAdapterViewHolder(view, context);
//        audiosViewHolder.itemView.setOnClickListener(this);
//        return audiosViewHolder;
    }

    @Override
    public void onBindViewHolder(final AudioAdapterViewHolder holder, final int position) {
        String title = position + 1 + "    " + jcAudioList.get(position).getTitle();
        holder.audioTitle.setText(title);
        holder.itemView.setTag(jcAudioList.get(position));
        applyProgressPercentage(holder, progressMap.get(position, 0.0f));
        if (position == clickPosition) {
            holder.audioTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.audioTitle.setTextColor(Color.parseColor("#000000"));
        }
        if (isShowDelete) {
            holder.deleteAudio.setVisibility(View.VISIBLE);
            holder.done.setVisibility(View.GONE);
            holder.downloadImg.setVisibility(View.GONE);
        } else {
            holder.deleteAudio.setVisibility(View.GONE);
            if (new File(Constants.AUDIO_FOLDER + File.separator + jcAudioList.get(position).getTitle() + ".mp3").exists()) {
                holder.done.setVisibility(View.VISIBLE);
                holder.downloadImg.setVisibility(View.GONE);
                //holder.downloadImg.setColorFilter(context.getResources().getColor(R.color.light_gray));
            } else {
                holder.done.setVisibility(View.GONE);
                holder.downloadImg.setVisibility(View.VISIBLE);
                //holder.downloadImg.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = position;
                holder.audioTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                if (mListener != null) mListener.onItemClick(position);
                notifyDataSetChanged();
            }
        });

        holder.downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (Utils.isNetworkConnected(context)) {

                    Utils.showAlert(context, "Do You Want To Download ?", new AlertCallback() {
                        @Override
                        public void alertCallback(boolean value) {
                            if (value) {
                                if (mListener != null) {
                                    view.setTag(jcAudioList.get(position));
                                    mListener.onFileDownload(position, view);
                                }
                            }

                        }
                    }, true, "Yes");

                } else {
                    Toast.makeText(context, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPlayClicked(position);
                }
            }
        });
        holder.deleteAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null && !player.isPlaying()) {
                    Utils.showAlert(context, "Do You Want To Delete ? ", new AlertCallback() {
                        @Override
                        public void alertCallback(boolean value) {
                            if (value) {
                                if (player != null) {
                                    player.setVisibility(View.GONE);
                                    player.kill();
                                    player = null;
                                }
                                if (progressMap != null && progressMap.size() > 0) {
                                    progressMap.clear();
                                }
                                clickPosition = -1;
                                JcAudio audio = jcAudioList.get(position);
                                if (new File(audio.getPath()).exists()) {
                                    jcAudioList.remove(position);
                                    new File(audio.getPath()).delete();
                                }
                                notifyDataSetChanged();
                            }

                        }
                    }, true, "Yes");
                } else {
                    Utils.showAlert(context, "Please Stop Audio Player", null, false, "Ok");
                }

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Applying percentage to progress.
     *
     * @param holder     ViewHolder
     * @param percentage in float value. where 1 is equals as 100%
     */
    private void applyProgressPercentage(AudioAdapterViewHolder holder, float percentage) {
        Log.d(TAG, "applyProgressPercentage() with percentage = " + percentage);
        LinearLayout.LayoutParams progress = (LinearLayout.LayoutParams) holder.viewProgress.getLayoutParams();
        LinearLayout.LayoutParams antiProgress = (LinearLayout.LayoutParams) holder.viewAntiProgress.getLayoutParams();

        progress.weight = percentage;
        holder.viewProgress.setLayoutParams(progress);

        antiProgress.weight = 1.0f - percentage;
        holder.viewAntiProgress.setLayoutParams(antiProgress);
    }

    @Override
    public int getItemCount() {
        return jcAudioList == null ? 0 : jcAudioList.size();
    }

    public void updateProgress(JcAudio jcAudio, float progress) {
        int position = jcAudioList.indexOf(jcAudio);
        if (position == -1) {
            position = clickPosition;
        }
        Log.d(TAG, "Progress = " + progress);


        progressMap.put(position, progress);
        if (progressMap.size() > 1) {
            for (int i = 0; i < progressMap.size(); i++) {
                if (progressMap.keyAt(i) != position) {
                    Log.d(TAG, "KeyAt(" + i + ") = " + progressMap.keyAt(i));
                    notifyItemChanged(progressMap.keyAt(i));
                    progressMap.delete(progressMap.keyAt(i));
                }
            }
        }
        notifyItemChanged(position);
    }

    // Define the mListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onPlayClicked(int position);

        void onFileDownload(int position, View view);
    }

    static class AudioAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView audioTitle;
        private ImageView downloadImg;
        private ImageView done;
        private ImageView deleteAudio;
        private View viewProgress;
        private View viewAntiProgress;

        public AudioAdapterViewHolder(View view, final Context context) {
            super(view);
            this.audioTitle = view.findViewById(R.id.audio_title);
            this.downloadImg = view.findViewById(R.id.download_audio);
            viewProgress = view.findViewById(R.id.song_progress_view);
            viewAntiProgress = view.findViewById(R.id.song_anti_progress_view);
            done = view.findViewById(R.id.play_audio);
            deleteAudio = view.findViewById(R.id.delete_audio);

           /* btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onFileDownload(getAdapterPosition());
                    }
                }
            });*/

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    audioTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    if (mListener != null) mListener.onItemClick(getAdapterPosition());
                }
            });*/
        }
    }
}
