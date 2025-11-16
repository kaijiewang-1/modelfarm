package com.example.modelfarm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.material.button.MaterialButton;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.UpdateDeviceRequest;
import com.example.modelfarm.network.services.DeviceApiService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 摄像头实时直播页面
 * 通过调用接口获取后端拼接好的推流地址并播放
 */
public class LiveStreamActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnRefresh;
    private MaterialButton btnStopStream;
    private TextView tvDeviceName;
    private TextView tvStreamStatus;
    private TextView tvStreamUrl;
    private TextView tvOverlayMessage;
    private ProgressBar progressBuffering;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    private DeviceApiService deviceApiService;
    
    private int deviceId;
    private String deviceName;
    private Device currentDevice;
    private String currentStreamUrl;

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
            
            // 显示更详细的错误信息
            String errorMsg = "播放错误";
            if (error != null && error.getMessage() != null) {
                errorMsg = error.getMessage();
                // 简化错误信息显示
                if (errorMsg.contains("timeout")) {
                    errorMsg = "连接超时，请检查网络或稍后重试";
                } else if (errorMsg.contains("Socket")) {
                    errorMsg = "网络连接失败，请检查网络";
                } else if (errorMsg.contains("HttpDataSource")) {
                    errorMsg = "无法连接到视频流服务器";
                }
            }
            showOverlayMessage(errorMsg);
            
            // 记录错误日志
            android.util.Log.e("LiveStream", "Playback error: ", error);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_stream);

        initViews();
        initApiService();
        extractIntentData();
        setupClickListeners();
        updateInitialUi();

        loadStreamUrl();
    }
    
    private void initApiService() {
        deviceApiService = RetrofitClient.create(this, DeviceApiService.class);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnStopStream = findViewById(R.id.btnStopStream);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvStreamStatus = findViewById(R.id.tvStreamStatus);
        tvStreamUrl = findViewById(R.id.tvStreamUrl);
        tvOverlayMessage = findViewById(R.id.tvOverlayMessage);
        progressBuffering = findViewById(R.id.progressBuffering);
        playerView = findViewById(R.id.playerView);
        
        // 设置更新密钥按钮
        MaterialButton btnUpdateSecret = findViewById(R.id.btnUpdateSecret);
        if (btnUpdateSecret != null) {
            btnUpdateSecret.setVisibility(View.VISIBLE);
            btnUpdateSecret.setOnClickListener(v -> showUpdateSecretDialog());
        }
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        deviceId = intent.getIntExtra("device_id", -1);
        deviceName = intent.getStringExtra("device_name");

        if (!TextUtils.isEmpty(deviceName)) {
            tvDeviceName.setText(deviceName);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnRefresh.setOnClickListener(v -> loadStreamUrl());

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

    /**
     * 从接口加载推流地址
     */
    private void loadStreamUrl() {
        if (deviceId <= 0) {
            stopPlayback(false, null);
            tvStreamStatus.setText(getString(R.string.stream_status_error));
            tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
            showOverlayMessage("设备ID无效");
            return;
        }

        tvStreamStatus.setText("正在获取推流地址...");
        progressBuffering.setVisibility(View.VISIBLE);
        hideOverlayMessage();

        deviceApiService.getDevice(deviceId).enqueue(new Callback<ApiResponse<Device>>() {
            @Override
            public void onResponse(Call<ApiResponse<Device>> call, Response<ApiResponse<Device>> response) {
                runOnUiThread(() -> {
                    progressBuffering.setVisibility(View.GONE);
                    
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        Device device = response.body().getData();
                        currentDevice = device;
                        if (device != null && !TextUtils.isEmpty(device.getUrl())) {
                            String streamUrl = device.getUrl();
                            currentStreamUrl = streamUrl;
                            // 显示推流信息
                            displayStreamInfo(streamUrl);
                            tvStreamUrl.setText(streamUrl);
                            attemptPlayback(streamUrl);
                        } else {
                            stopPlayback(false, null);
                            tvStreamStatus.setText(getString(R.string.stream_status_error));
                            tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
                            showOverlayMessage("设备未配置推流地址");
                        }
                    } else {
                        stopPlayback(false, null);
                        tvStreamStatus.setText(getString(R.string.stream_status_error));
                        tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
                        String errorMsg = response.body() != null ? response.body().getMessage() : "获取推流地址失败";
                        showOverlayMessage(errorMsg);
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Device>> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBuffering.setVisibility(View.GONE);
                    stopPlayback(false, null);
                    tvStreamStatus.setText(getString(R.string.stream_status_error));
                    tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
                    showOverlayMessage("网络错误: " + t.getMessage());
                });
            }
        });
    }

    /**
     * 开始播放推流（使用后端提供的完整URL）
     */
    private void attemptPlayback(String streamUrl) {
        if (TextUtils.isEmpty(streamUrl)) {
            stopPlayback(false, null);
            tvStreamStatus.setText(getString(R.string.stream_status_error));
            tvStreamUrl.setText(getString(R.string.stream_url_placeholder));
            showOverlayMessage("推流地址为空");
            return;
        }

        tvStreamStatus.setText(getString(R.string.stream_status_connecting));
        progressBuffering.setVisibility(View.VISIBLE);
        hideOverlayMessage();

        releasePlayer(false);

        // 配置LoadControl，增加缓冲时间以减少超时问题
        LoadControl loadControl = new DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                50000,  // minBufferMs: 最小缓冲50秒
                120000, // maxBufferMs: 最大缓冲120秒
                2500,   // bufferForPlaybackMs: 开始播放前缓冲2.5秒
                5000    // bufferForPlaybackAfterRebufferMs: 重新缓冲后等待5秒
            )
            .setBackBuffer(50000, true) // 保留50秒的后台缓冲
            .build();

        // 创建ExoPlayer实例，使用自定义的LoadControl
        exoPlayer = new ExoPlayer.Builder(this)
            .setRenderersFactory(new DefaultRenderersFactory(this))
            .setLoadControl(loadControl)
            .build();
        
        exoPlayer.addListener(playerListener);
        playerView.setPlayer(exoPlayer);
        
        // 创建MediaItem，直接使用后端提供的完整URL
        MediaItem mediaItem = MediaItem.fromUri(streamUrl);
        
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
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


    private void showOverlayMessage(String message) {
        if (tvOverlayMessage == null) return;
        tvOverlayMessage.setVisibility(View.VISIBLE);
        tvOverlayMessage.setText(message);
    }

    private void hideOverlayMessage() {
        if (tvOverlayMessage == null) return;
        tvOverlayMessage.setVisibility(View.GONE);
    }

    /**
     * 解析并显示推流信息
     */
    private void displayStreamInfo(String streamUrl) {
        try {
            URI uri = new URI(streamUrl);
            String protocol = uri.getScheme(); // rtmp
            String host = uri.getHost(); // 124.71.97.178
            String path = uri.getPath(); // /live/songzan
            String query = uri.getQuery(); // secret=0d0a59bd36404152beab3f19e74e7813
            
            // 解析路径获取应用名和流名称
            String[] pathParts = path != null ? path.split("/") : new String[0];
            String appName = pathParts.length > 1 ? pathParts[1] : "";
            String streamName = pathParts.length > 2 ? pathParts[2] : "";
            
            // 解析查询参数获取密钥
            String secret = "";
            if (!TextUtils.isEmpty(query) && query.contains("secret=")) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("secret=")) {
                        secret = param.substring(7);
                        break;
                    }
                }
            }
            
            // 构建信息字符串
            StringBuilder info = new StringBuilder();
            info.append("协议: ").append(protocol != null ? protocol.toUpperCase() : "-").append("\n");
            info.append("服务器IP: ").append(host != null ? host : "-").append("\n");
            info.append("应用名: ").append(appName).append("\n");
            info.append("流名称: ").append(streamName).append("\n");
            if (!TextUtils.isEmpty(secret)) {
                info.append("密钥: ").append(secret);
            }
            
            // 可以在这里添加一个TextView来显示详细信息，或者通过Toast显示
            android.util.Log.d("LiveStream", "推流信息:\n" + info.toString());
        } catch (URISyntaxException e) {
            android.util.Log.e("LiveStream", "解析推流URL失败: " + e.getMessage());
        }
    }

    /**
     * 显示更新密钥对话框
     */
    private void showUpdateSecretDialog() {
        if (currentDevice == null || TextUtils.isEmpty(currentStreamUrl)) {
            Toast.makeText(this, "请先加载推流地址", Toast.LENGTH_SHORT).show();
            return;
        }

        // 从当前URL中提取密钥
        String currentSecret = extractSecretFromUrl(currentStreamUrl);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_update_secret, null);
        EditText etSecret = dialogView.findViewById(R.id.et_secret);
        if (!TextUtils.isEmpty(currentSecret)) {
            etSecret.setText(currentSecret);
        }

        new AlertDialog.Builder(this)
            .setTitle("更新流密钥")
            .setView(dialogView)
            .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newSecret = etSecret.getText().toString().trim();
                    if (TextUtils.isEmpty(newSecret)) {
                        Toast.makeText(LiveStreamActivity.this, "请输入密钥", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updateStreamSecret(newSecret);
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 从URL中提取密钥
     */
    private String extractSecretFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (!TextUtils.isEmpty(query) && query.contains("secret=")) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("secret=")) {
                        return param.substring(7);
                    }
                }
            }
        } catch (URISyntaxException e) {
            android.util.Log.e("LiveStream", "解析URL失败: " + e.getMessage());
        }
        return "";
    }

    /**
     * 更新推流密钥
     * 注意：这里需要根据后端API的实际实现来更新
     * 可能需要更新设备的properties中的secret字段，或者通过其他方式
     */
    private void updateStreamSecret(String newSecret) {
        if (currentDevice == null) {
            Toast.makeText(this, "设备信息不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建新的推流URL（使用新密钥）
        String newStreamUrl = buildStreamUrlWithSecret(currentStreamUrl, newSecret);
        
        // 更新设备的properties，将新密钥存储
        Map<String, Object> properties = currentDevice.getProperties() != null ? 
            new HashMap<>(currentDevice.getProperties()) : new HashMap<>();
        properties.put("secret", newSecret);
        
        // 构建更新请求
        UpdateDeviceRequest request = new UpdateDeviceRequest(
            currentDevice.getId(),
            currentDevice.getSiteId(),
            currentDevice.getName(),
            currentDevice.getPushName(),
            currentDevice.getType(),
            currentDevice.getStatus(),
            properties
        );

        deviceApiService.updateDevice(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        Toast.makeText(LiveStreamActivity.this, "密钥更新成功，请重新加载推流", Toast.LENGTH_SHORT).show();
                        // 重新加载推流地址（后端会返回新的URL）
                        loadStreamUrl();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "更新密钥失败";
                        Toast.makeText(LiveStreamActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(LiveStreamActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 使用新密钥构建推流URL
     */
    private String buildStreamUrlWithSecret(String originalUrl, String newSecret) {
        try {
            URI uri = new URI(originalUrl);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            String path = uri.getPath();
            int port = uri.getPort();
            
            // 构建新URL，替换密钥参数
            StringBuilder newUrl = new StringBuilder();
            newUrl.append(scheme).append("://");
            if (host != null) {
                newUrl.append(host);
            }
            if (port != -1) {
                newUrl.append(":").append(port);
            }
            if (path != null) {
                newUrl.append(path);
            }
            newUrl.append("?secret=").append(newSecret);
            
            return newUrl.toString();
        } catch (URISyntaxException e) {
            android.util.Log.e("LiveStream", "构建URL失败: " + e.getMessage());
            return originalUrl;
        }
    }
}

