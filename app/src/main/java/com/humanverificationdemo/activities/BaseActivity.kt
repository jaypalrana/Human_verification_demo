package com.humanverificationdemo.activities

import BaseViewModel
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseActivity <T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    private  val TAG = "BaseActivity"
    private lateinit var mViewDataBinding: T



    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun getViewModel(): V
    abstract fun setOnClick()
    abstract fun apiObserve()
    private lateinit var mViewModel: V
    fun getViewBinding(): T {
        return mViewDataBinding
    }
    abstract fun initialize()
    init {
        // Load all native libraries needed by the app.
        System.loadLibrary("mediapipe_jni")
        try {
            System.loadLibrary("opencv_java3")
        } catch (e: Exception) {
            // Some example apps (e.g. template matching) require OpenCV 4.
            System.loadLibrary("opencv_java4")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        getViewBinding()
        initialize()
        setOnClick()
        apiObserve()
    }
    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = getViewModel()
        mViewDataBinding.executePendingBindings()



    }

    fun getVM(): V {
        return mViewModel
    }

}