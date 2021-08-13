package com.sino.jsbridge.handler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.sino.jsbridge.bridge.JsCallBacker
import com.sino.jsbridge.bridge.JsCallBacker.Companion.CODE_ERR_FAIL
import com.sino.jsbridge.util.Utils
import org.json.JSONArray
import org.json.JSONObject


class ImageJsHandler : BaseJsApiHandler() {

    companion object {
        const val REQUEST_ALBUM = 10000
    }

    override fun handleApi(apiName: String, params: String, jsCallBacker: JsCallBacker): Boolean {
        when (apiName) {
            "choosePhotos" -> {
                val activity = getActivity() ?: return true
                val fragment = getFragment()
                val albumIntent = Intent(Intent.ACTION_PICK)
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                if (fragment != null) {
                    fragment.startActivityForResult(albumIntent, REQUEST_ALBUM)
                } else {
                    activity.startActivityForResult(albumIntent, REQUEST_ALBUM)
                }
                saveJsCallBacker("choosePhotos_$REQUEST_ALBUM", jsCallBacker)
                return true
            }
            "previewPhotos" -> {
                try {
                    val activity = getActivity() ?: return true

                    val jsonObject = JSONObject(params)
                    val urls = jsonObject.optJSONArray("urls")
                    val index = jsonObject.optInt("index", 0)

                    val intent = Intent(Intent.ACTION_VIEW)

                    val filePath = urls.getString(index)

                    val uri = Uri.parse(filePath)
                    intent.setDataAndType(
                        uri,
                        MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePath))
                    )
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    jsCallBacker.fail(JsCallBacker.CODE_ERR_FAIL, e.toString())
                }
                return true
            }
            "uploadPhotos" -> {
            }
        }
        return false
    }

    override fun onContainerResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        when (requestCode) {
            REQUEST_ALBUM -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val get7RemoveJsCallBacker =
                            get7RemoveJsCallBacker("choosePhotos_$REQUEST_ALBUM") ?: return true
                        val result = JSONArray()

                        val item = JSONObject()
                        item.put("nativeResourceUrl", data?.dataString ?: "")
                        result.put(item)

                        get7RemoveJsCallBacker.success(jsonArray = result)
                    }
                    Activity.RESULT_CANCELED -> {
                        val get7RemoveJsCallBacker =
                            get7RemoveJsCallBacker("choosePhotos_$REQUEST_ALBUM") ?: return true
                        get7RemoveJsCallBacker.cancel()
                    }
                    else -> {
                        val get7RemoveJsCallBacker =
                            get7RemoveJsCallBacker("choosePhotos_$REQUEST_ALBUM") ?: return true
                        get7RemoveJsCallBacker.fail(CODE_ERR_FAIL, "用户选择相册图片失败")
                    }
                }
                return true
            }
        }
        return super.onContainerResult(requestCode, resultCode, data)
    }
}