package com.vsantander.paymentchallenge.presentation.base.activity

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.di.Injectable

abstract class BaseActivity : AppCompatActivity(), Injectable {
    annotation class Animation(@AnimationType val value: Int = NON_VALUE,
                               @AnimationType val enter: Int = NON_VALUE,
                               @AnimationType val exit: Int = NON_VALUE)

    companion object {
        val EXTRA_ANIMATION_TYPE = "EXTRA_ANIMATION_TYPE"
        val EXTRA_ANIMATION_ENTER = "EXTRA_ANIMATION_ENTER"
        val EXTRA_ANIMATION_EXIT = "EXTRA_ANIMATION_EXIT"
        const val NONE = -1
        const val PUSH = 0
        const val FADE = 1
        const val MODAL = 2
        const val POPUP = 3

        private const val NON_VALUE = -2

        @IntDef(PUSH, FADE, MODAL, POPUP, NONE)
        annotation class AnimationType
    }

    @AnimationType
    private var animationTypeEnter = NONE

    @AnimationType
    private var animationTypeExit = NONE


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        intent?.let { intent ->
            intent.extras?.let { bundle ->
                if (bundle.containsKey(EXTRA_ANIMATION_TYPE)) {
                    animationTypeEnter = bundle.getInt(EXTRA_ANIMATION_TYPE)
                    animationTypeExit = bundle.getInt(EXTRA_ANIMATION_TYPE)

                } else if (bundle.containsKey(EXTRA_ANIMATION_ENTER) &&
                        bundle.containsKey(EXTRA_ANIMATION_EXIT)) {

                    animationTypeEnter = bundle.getInt(EXTRA_ANIMATION_ENTER)
                    animationTypeExit = bundle.getInt(EXTRA_ANIMATION_EXIT)
                }
            }
        }

        if (animationTypeEnter == NONE && animationTypeExit == NONE) {
            val annotation = javaClass.getAnnotation(Animation::class.java)
            if (annotation != null) {
                if (annotation.value != NON_VALUE) {
                    animationTypeEnter = annotation.value
                    animationTypeExit = annotation.value

                } else if (annotation.enter != NON_VALUE && annotation.exit != NON_VALUE) {
                    animationTypeEnter = annotation.enter
                    animationTypeExit = annotation.exit
                }
            }
        }
        animationEnter()

        super.onCreate(savedInstanceState)
    }

    override fun finish() {
        super.finish()
        animationExit()
    }

    private fun animationEnter() {
        when (animationTypeEnter) {
            FADE -> overridePendingTransition(R.anim.transition_fade_in, R.anim.transition_no_animation)
            PUSH -> overridePendingTransition(R.anim.transition_enter_right, R.anim.transition_no_animation_activity)
            MODAL -> overridePendingTransition(R.anim.transition_slide_up, R.anim.transition_no_animation_activity)
            POPUP -> overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_no_animation_activity)
        }
    }

    private fun animationExit() {
        when (animationTypeExit) {
            PUSH -> overridePendingTransition(R.anim.transition_no_animation_activity, R.anim.transition_exit_right)
            MODAL -> overridePendingTransition(R.anim.transition_no_animation_activity, R.anim.transition_slide_down)
            POPUP -> overridePendingTransition(R.anim.transition_no_animation_activity, R.anim.transition_zoom_out)
            FADE -> overridePendingTransition(R.anim.transition_no_animation_activity, R.anim.transition_fade_out)
        }
    }
}