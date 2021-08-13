package com.sino.jsbridge.test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sino.jsbridge.SinoJsBridge
import com.sino.jsbridge.test.fragment.TestInFragmentActivity
import com.sino.jsbridge.test.x5.TestX5WebViewActivity
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPermission();

        //init x5
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(applicationContext, object : PreInitCallback {
            override fun onCoreInitFinished() {
                Log.d("onCoreInitFinished", "onCoreInitFinished")
            }

            override fun onViewInitFinished(b: Boolean) {
                Log.d("onViewInitFinished", "onViewInitFinished$b")
            }
        })

        SinoJsBridge.init(debug = true)

        setContentView(R.layout.activity_main)
    }

    val RC_PHONE = 0x1102
    private fun initPermission() {
        if (EasyPermissions.hasPermissions(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            //todo
        } else {
            EasyPermissions.requestPermissions(
                this,
                "请求获取权限",
                RC_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    fun testX5WebViewActivity(view: View) {
        startActivity(Intent(this, TestX5WebViewActivity::class.java))
    }

    fun testInFragment(view: View) {
        startActivity(Intent(this, TestInFragmentActivity::class.java))
    }
}