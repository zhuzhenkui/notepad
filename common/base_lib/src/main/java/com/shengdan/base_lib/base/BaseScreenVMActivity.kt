package com.shengdan.base_lib.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common_lib.base.BaseActivity
import com.shengdan.base_lib.R
import com.shengdan.base_lib.entity.ErrorEntity
import com.shengdan.base_lib.utils.ToastUtil
import com.shengdan.ui_lib.title.CenterTitleToolbar
import com.shengdan.ui_lib.utils.StatusBarUtil

 abstract class BaseScreenVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity() {
    protected var binding: DB? = null
     @JvmField
    protected var mViewModel: VM? = null
     @JvmField
     protected val TAG: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparentForWindow(this)
        StatusBarUtil.StatusBarLightMode(this)
//        ARouter.getInstance().inject(this)
        if (getLayoutId() != 0){
            binding = DataBindingUtil.setContentView(this,getLayoutId())
        }

        mViewModel = getViewModel()
        mViewModel?.errorData?.observe(this, ::resErrorData)
        mViewModel?.loadData?.observe(this, ::resLoadData)

        getIntentData(intent)
        init(savedInstanceState)
    }

    /**
     * 初始化
     */
    protected abstract fun init(savedInstanceState: Bundle?)

    /**
     * 初始化VM
     *
     * @return
     */
    protected abstract fun getViewModel(): VM?

    /**
     * 获取资源ID
     *
     * @return 布局资源ID
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 提取intent数据
     * @param intent
     */
    protected open fun getIntentData(intent: Intent?) {}

    /**
     * show error info
     */
    fun resErrorData(errorEntity: ErrorEntity) {
        ToastUtil.showNormal(errorEntity.message)
    }

    /**
     * show loading
     */
    private fun resLoadData(b: Boolean?) {

    }

    /**
     * 点击返回按钮
     */
    open fun goBackClick() {
        onBackPressed()
    }
}


