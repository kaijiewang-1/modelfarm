package com.example.modelfarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.material.button.MaterialButton;

/**
 * 摄像头实时直播页面
 * 支持通过推流名和动态密钥拼接RTMP地址并播放
 */
public class LiveStreamActivity extends AppCompatActivity {

    private static final String PREFS_STREAM = "live_stream_prefs";
    private static final String KEY_SECRET = "stream_secret";
    private static final String BASE_STREAM_URL = "rtmp://124.71.97.178/live/";

    private ImageButton btnBack;
    private ImageButton btnRefresh;
    private MaterialButton btnUpdateSecret;
    private MaterialButton btnStopStream;
    private TextView tvDeviceName;
    private TextView tvStreamStatus;
    private TextView tvStreamUrl;
    private TextView tvOverlayMessage;
    private ProgressBar progressBuffering;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    private SharedPreferences preferences;

    private String deviceName;
    private String pushName;
    private String explicitStreamUrl;
    private String initialSecret;

    private final Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onPlaybackStateChanged(int playbackState) {
            if (playbackState == Player.STATE_BUFFERING) {
                progressBuffering.setVisibility(View.VISIBLE);
            } else if (playbackState == Player.STATE_READY) {
                progressBuffering.setVisibility(View.GONE);
                tvStreamStatus.setText(getString(R.string.stream_status_playing));
            } else if (playbackState == Player.STATE_ENDED) {
                progressBuffering.setVisibility(View.GONE);
                tvStreamStatus.setText(getString(R.string.stream_status_stopped));
                showOverlayMessage(getString(R.string.stream_overlay_error));
            }
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            progressBuffering.setVisibility(View.GONE);
            tvStreamStatus.setText(getString(R.string.stream_status_error));
            showOverlayMessage(getString(R.string.stream_overlay_error));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_stream);

        preferences = getSharedPreferences(PREFS_STREAM, MODE_PRIVATE);

        initViews();
        extractIntentData();
        setupClickListeners();
        updateInitialUi();

        attemptPlayback(false);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnUpdateSecret = findViewById(R.id.btnUpdateSecret);
        btnStopStream = findViewById(R.id.btnStopStream);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvStreamStatus = findViewById(R.id.tvStreamStatus);
        tvStreamUrl = findViewById(R.id.tvStreamUrl);
        tvOverlayMessage = findViewById(R.id.tvOverlayMessage);
        progressBuffering = findViewById(R.id.progressBuffering);
        playerView = findViewById(R.id.playerView);
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        deviceName = intent.getStringExtra("device_name");
        pushName = intent.getStringExtra("push_name");
        explicitStreamUrl = intent.getStringExtra("stream_url");
        initialSecret = intent.getStringExtra("stream_secret");

        if (!TextUtils.isEmpty(deviceName)) {
            tvDeviceName.setText(deviceName);
        }

        if (!TextUtils.isEmpty(initialSecret)) {
            saveSecret(initialSecret.trim(), false);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnRefresh.setOnClickListener(v -> attemptPlayback(true));

        btnUpdateSecret.setOnClickListener(v -> showSecretInputDialog());

        btnStopStream.setOnClickListener(v -> {
            stopPlayback(true, getString(R.string.stream_stop_hint));
            Toast.makeText(this, getString(R.string.stream_stop_hint), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateInitialUi() {
        tvStreamStatus.setText(getString(R.string.stream_status_idle));
        tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
        tvOverlayMessage.setVisibility(View.GONE);
        progressBuffering.setVisibility(View.GONE);
    }

    private void attemptPlayback(boolean isManualRefresh) {
        String streamUrl = resolveStreamUrl(null);

        if (TextUtils.isEmpty(streamUrl)) {
            stopPlayback(false, null);
            tvStreamStatus.setText(getString(R.string.stream_status_missing_secret));
            tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
            showOverlayMessage(getString(R.string.stream_overlay_missing_secret));
            return;
        }

        tvStreamUrl.setText(streamUrl);
        tvStreamStatus.setText(getString(R.string.stream_status_connecting));
        progressBuffering.setVisibility(View.VISIBLE);
        hideOverlayMessage();

        releasePlayer(false);

        exoPlayer = new ExoPlayer.Builder(this).build();
        exoPlayer.addListener(playerListener);
        playerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(MediaItem.fromUri(streamUrl));
        exoPlayer.prepare();
        exoPlayer.play();

        if (isManualRefresh) {
            Toast.makeText(this, getString(R.string.stream_manual_refresh_hint), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayback(boolean updateStatus, @Nullable String overlayMessage) {
        releasePlayer(false);
        progressBuffering.setVisibility(View.GONE);
        if (updateStatus) {
            tvStreamStatus.setText(getString(R.string.stream_status_stopped));
        }
        if (!TextUtils.isEmpty(overlayMessage)) {
            showOverlayMessage(overlayMessage);
        } else {
            hideOverlayMessage();
        }
    }

    private void releasePlayer(boolean clearStatus) {
        if (exoPlayer != null) {
            exoPlayer.removeListener(playerListener);
            exoPlayer.release();
            exoPlayer = null;
        }
        if (playerView != null) {
            playerView.setPlayer(null);
        }
        if (clearStatus) {
            progressBuffering.setVisibility(View.GONE);
            tvStreamStatus.setText(getString(R.string.stream_status_idle));
        }
    }

    private void showSecretInputDialog() {
        final String currentSecret = preferences.getString(KEY_SECRET, "");

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_secret_input, null, false);
        final EditText editSecret = dialogView.findViewById(R.id.editSecret);
        editSecret.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if (!TextUtils.isEmpty(currentSecret)) {
            editSecret.setText(currentSecret);
            editSecret.setSelection(currentSecret.length());
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.stream_secret_dialog_title))
                .setView(dialogView)
                .setNegativeButton(getString(R.string.stream_secret_dialog_cancel), null)
                .setPositiveButton(getString(R.string.stream_secret_dialog_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Editable text = editSecret.getText();
                        String secret = text != null ? text.toString().trim() : "";
                        if (TextUtils.isEmpty(secret)) {
                            Toast.makeText(LiveStreamActivity.this, getString(R.string.stream_secret_empty_warning), Toast.LENGTH_LONG).show();
                            return;
                        }
                        saveSecret(secret, true);
                        attemptPlayback(true);
                    }
                })
                .show();
    }

    private void saveSecret(String secret, boolean notify) {
        preferences.edit().putString(KEY_SECRET, secret).apply();
        if (notify) {
            Toast.makeText(this, getString(R.string.stream_secret_saved), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer(true);
    }

    private String resolveStreamUrl(@Nullable String overrideSecret) {
        if (!TextUtils.isEmpty(explicitStreamUrl)) {
            return explicitStreamUrl.trim();
        }
        if (TextUtils.isEmpty(pushName)) {
            return null;
        }
        String secret = !TextUtils.isEmpty(overrideSecret) ? overrideSecret : preferences.getString(KEY_SECRET, "");
        if (TextUtils.isEmpty(secret)) {
            return null;
        }
        return BASE_STREAM_URL + pushName + "?secret=" + secret.trim();
    }

    private void showOverlayMessage(String message) {
        if (tvOverlayMessage == null) return;
        tvOverlayMessage.setVisibility(View.VISIBLE);
        tvOverlayMessage.setText(message);
    }

    private void hideOverlayMessage() {
        if (tvOverlayMessage == null) return;
        tvOverlayMessage.setVisibility(View.GONE);
    }
}

