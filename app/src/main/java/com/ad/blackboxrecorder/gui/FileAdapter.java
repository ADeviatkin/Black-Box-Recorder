package com.ad.blackboxrecorder.gui;

import static com.ad.blackboxrecorder.MainActivity.directory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.ad.blackboxrecorder.MainActivity;
import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.gui.tabs.ListenPage;
import com.ad.blackboxrecorder.playing.Decryptor;
import com.ad.blackboxrecorder.recording.Record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import linc.com.pcmdecoder.PCMDecoder;

public class FileAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> files;
    private SeekBar currentSeekBar = null;
    private AudioTrack currentAudioTrack = null;
    private ImageButton currentV = null;

    public FileAdapter(Context context, ArrayList<String> files) {
        super(context, R.layout.list_item, files);
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        if(currentSeekBar != null) {
            currentSeekBar.setVisibility(View.GONE);
            currentSeekBar = null;
        }
        if(currentAudioTrack != null) {
            currentAudioTrack.stop();
            currentAudioTrack = null;
        }
        if(currentV != null){
            currentV.setPressed(false);
            currentV = null;
        }
        TextView textView = convertView.findViewById(R.id.textView);
        TextView textView2 = convertView.findViewById(R.id.textView2);
        ImageButton playButton = convertView.findViewById(R.id.playButton);
        Record record = new Record(files.get(position));

        textView.setText(record.getTitle().split("\\.")[0]);
        SeekBar seekBar = convertView.findViewById(R.id.seek_bar);

        // Initially hide the SeekBar
        seekBar.setVisibility(View.GONE);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()){
                    if(currentSeekBar != null) {
                        currentSeekBar.setVisibility(View.GONE);
                        currentSeekBar = null;
                    }
                    if(currentAudioTrack != null) {
                        currentAudioTrack.stop();
                        currentAudioTrack = null;
                    }
                    currentV = (ImageButton) v;
                    v.setSelected(true);
                    seekBar.setVisibility(View.VISIBLE);
                    byte[] pcmData = Decryptor.decrypt(record.getFile());
                    int audioTrackDuration = 60;

                    int sampleRate = 44100;
                    int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
                    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

                    int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);

                    AudioTrack audioTrack = new AudioTrack(
                            AudioManager.STREAM_MUSIC,
                            sampleRate,
                            channelConfig,
                            audioFormat,
                            Math.max(minBufferSize, pcmData.length),
                            AudioTrack.MODE_STATIC);

                    audioTrack.write(pcmData, 0, pcmData.length);
                    currentSeekBar = seekBar;
                    currentAudioTrack =audioTrack;
                    seekBar.setMax(audioTrackDuration);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (audioTrack != null && fromUser) {
                                int seekPos = progress * audioTrack.getSampleRate() * audioTrack.getChannelCount();
                                audioTrack.setPlaybackHeadPosition(seekPos);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            if (audioTrack != null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                                audioTrack.pause();
                            }
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            if (audioTrack != null) {
                                audioTrack.play();
                            }
                        }
                    });
                }
                else{
                    if(currentSeekBar != null) {
                        currentSeekBar.setVisibility(View.GONE);
                        currentSeekBar = null;
                    }
                    if(currentAudioTrack != null) {
                        currentAudioTrack.stop();
                        currentAudioTrack = null;
                    }
                    v.setSelected(false);
                }

            }
        });
        ImageView buttonMore = convertView.findViewById(R.id.buttonMore);
        if (!record.isPermanent){
            textView2.setText("min. before removal: " +record.getStatus());
            View finalConvertView = convertView;
            buttonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_option1:
                                    record.makePermanent();
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.action_option2:
                                    byte[] pcm = Decryptor.decrypt(record.getFile());

                                    File pcm_file = null;
                                    try {
                                        pcm_file = new File(directory + "/temp");
                                        FileOutputStream fos = new FileOutputStream(pcm_file);
                                        fos.write(pcm);
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    PCMDecoder.encodeToMp3(
                                            pcm_file.getPath(),     // Input PCM file
                                            1,                                            // Number of channels
                                            96000,                                        // Bit rate
                                            22000,                                        // Sample rate
                                            directory + "/temp.mp3" // Output MP3 file
                                    );
                                    File mp3_file = new File(directory + "/temp.mp3");

                                    pcm_file.delete();
                                    Uri fileUri = FileProvider.getUriForFile(context, "com.ad.blackboxrecorder", mp3_file);

                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                                    shareIntent.setType("audio/mpeg");  // The MIME type for MP3 files is "audio/mpeg"
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of your email or share text");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");

                                    context.startActivity(Intent.createChooser(shareIntent, "Share file via"));
                                    mp3_file.delete();
                                    return true;
                                case R.id.action_option3:
                                    record.removeRecording();
                                    File[] fileList = directory.listFiles();

                                    // Check if the file list is not null
                                    if (fileList != null) {
                                        // Clear the ArrayList and add the names of the files
                                        files.clear();
                                        for (File file : fileList) {
                                            if (file.getName().split("\\.")[1].equals("encraud"))
                                                files.add(file.getName());
                                        }
                                        // Notify the ArrayAdapter that the data has changed
                                       notifyDataSetChanged();
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });
        } else{
            textView2.setText(null);
            buttonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu_permanent, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_option1:
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    // Assuming 'file' is a File object of the file you want to share
                                    Uri fileUri = FileProvider.getUriForFile(context, "com.ad.blackboxrecorder", record.getFile());
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                                    // Mime type of the file. For example, if it's a JPEG image, use "image/jpeg".
                                    // If you don't specifically know the mime type, you can use "*/*"
                                    shareIntent.setType("image/jpeg");
                                    // This is needed for apps targeting Android 10 (API level 29) and higher,
                                    // it grants temporary access permissions to the URI for the receiving app.
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    // You can optionally add a subject and text to the Intent for apps that support it like email clients.
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of your email or share text");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Body of your email or share text");
                                    context.startActivity(Intent.createChooser(shareIntent, "Share file via"));
                                    return true;
                                case R.id.action_option2:
                                    record.removeRecording();
                                    File[] fileList = directory.listFiles();

                                    // Check if the file list is not null
                                    if (fileList != null) {
                                        // Clear the ArrayList and add the names of the files
                                        files.clear();
                                        for (File file : fileList) {
                                            if (file.getName().split("\\.")[1].equals("encraud"))
                                                files.add(file.getName());
                                        }
                                        // Notify the ArrayAdapter that the data has changed
                                        notifyDataSetChanged();
                                    }
                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                }
            });
        }
        return convertView;
    }
}