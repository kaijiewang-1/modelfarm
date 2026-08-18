package com.example.modelfarm.network

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.example.modelfarm.network.models.OssMediaProperty
import com.example.modelfarm.network.models.OssSignatureItem
import com.example.modelfarm.network.models.OssSignatureRequest
import com.example.modelfarm.network.services.OssApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.Executors

/**
 * 先调 /oss/signature，再按预签名 URL 以 PUT 上传（与 OBS 文档一致）
 */
object OssUploadHelper {

    interface Callback {
        fun onSuccess(objectKeys: List<String>)
        fun onError(message: String)
    }

    private val ioExecutor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val plainClient = OkHttpClient.Builder().build()

    @JvmStatic
    fun uploadUris(context: Context, uris: List<Uri>, callback: Callback) {
        if (uris.isEmpty()) {
            callback.onSuccess(emptyList())
            return
        }
        ioExecutor.execute {
            try {
                val props = ArrayList<OssMediaProperty>()
                val bytesList = ArrayList<ByteArray>()
                for (uri in uris) {
                    val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
                    val fileType = if (mime.startsWith("video/")) "video" else "image"
                    val input = context.contentResolver.openInputStream(uri)
                        ?: throw IOException("无法读取文件")
                    val bytes = input.use { it.readBytes() }
                    bytesList.add(bytes)
                    val fileName = "mobile_${System.currentTimeMillis()}_${props.size}"
                    props.add(
                        OssMediaProperty(
                            fileType = fileType,
                            mimeType = mime,
                            fileSize = bytes.size.toLong(),
                            fileName = fileName
                        )
                    )
                }

                val service = RetrofitClient.create(context, OssApiService::class.java)
                val resp = service.getUploadSignatures(OssSignatureRequest(props.size, props)).execute()
                if (!resp.isSuccessful) {
                    postError(callback, "获取上传签名失败")
                    return@execute
                }
                val body = resp.body()
                if (body == null || body.code != 200 || body.data == null) {
                    postError(callback, body?.message ?: "获取上传签名失败")
                    return@execute
                }
                val items: List<OssSignatureItem> = body.data
                if (items.size != bytesList.size) {
                    postError(callback, "签名数量与文件不一致")
                    return@execute
                }
                val keys = ArrayList<String>()
                for (i in items.indices) {
                    val item = items[i]
                    val mediaType = item.contentType.toMediaTypeOrNull()
                    val requestBody = bytesList[i].toRequestBody(mediaType)
                    val putReq = Request.Builder()
                        .url(item.uploadUrl)
                        .put(requestBody)
                        .header("Content-Type", item.contentType)
                        .build()
                    val putResp = plainClient.newCall(putReq).execute()
                    if (!putResp.isSuccessful) {
                        postError(callback, "上传文件失败: ${putResp.code}")
                        return@execute
                    }
                    keys.add(item.objectKey)
                }
                mainHandler.post { callback.onSuccess(keys) }
            } catch (e: Exception) {
                postError(callback, e.message ?: e.toString())
            }
        }
    }

    private fun postError(callback: Callback, msg: String) {
        mainHandler.post { callback.onError(msg) }
    }
}
