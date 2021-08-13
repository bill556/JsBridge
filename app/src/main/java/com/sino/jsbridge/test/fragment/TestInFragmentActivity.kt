package com.sino.jsbridge.test.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sino.jsbridge.test.R

class TestInFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_in_fragment)

        val fragment = supportFragmentManager.findFragmentByTag("TestWebViewFragment")
            ?: TestWebViewFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, fragment, "TestWebViewFragment").commit()
    }
}
