package com.vsantander.paymentchallenge.presentation.splash

import android.os.Bundle
import android.os.Handler
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.presentation.base.activity.BaseActivity
import com.vsantander.paymentchallenge.presentation.contactlist.ContactListActivity
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit

@BaseActivity.Animation(BaseActivity.FADE)
class SplashActivity: BaseActivity() {

    companion object {
        private val SPLASH_DELAY = TimeUnit.SECONDS.toMillis(2) // 2 seconds
    }

    private val handler = Handler()

    private val runnable: Runnable = Runnable {
        if (!isFinishing) {
            startActivity<ContactListActivity>()
            finish()
        }
    }

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler.postDelayed(runnable, SPLASH_DELAY)
    }

    public override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

}