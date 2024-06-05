package com.shengdan.base_lib.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common_lib.base.BaseActivity
import com.shengdan.ui_lib.R
import com.shengdan.base_lib.entity.ErrorEntity
import com.shengdan.base_lib.utils.ToastUtil
import com.shengdan.ui_lib.title.CenterTitleToolbar
import com.shengdan.ui_lib.utils.StatusBarUtil

abstract class BaseVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity() {
    protected var binding: DB? = null
    @JvmField
    protected var mViewModel: VM? = null
    @JvmField
    protected val TAG: String = javaClass.simpleName

    private var toolbar: CenterTitleToolbar? = null
    private var tvTitle: TextView? = null
    private val top_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.StatusBarLightMode(this)
//        ARouter.getInstance().inject(this)
//        binding = DataBindingUtil.setContentView(this, getLayoutId());
        setContentView(R.layout.activity_base_toolbar)
        toolbar = findViewById(R.id.toolbar)
        tvTitle = findViewById(R.id.tv_title)
        val layout:FrameLayout = findViewById(R.id.base_activity_content)
        if (getLayoutId() != 0){
            binding = DataBindingUtil.inflate<DB>(LayoutInflater.from(layout.context),getLayoutId(),layout,true)
        }

        mViewModel = getViewModel()
        mViewModel?.errorData?.observe(this, ::resErrorData)
        mViewModel?.loadData?.observe(this, ::resLoadData)

        getIntentData(intent)
        init(savedInstanceState)
        initToolBar()
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
     * 是否显示返回键
     * @return
     */
    protected abstract fun isCanGoBack(): Boolean

    /**
     * 标题
     * @return
     */
    protected abstract fun toolbarTitleRes(): Int

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
     * 初始化标题栏
     */
    open fun initToolBar() {
        if (toolbar != null) {
            if (toolbarTitleRes() == 0) {
                tvTitle!!.visibility = View.GONE
                toolbar!!.visibility = View.GONE
            } else {
                toolbar!!.setTitleGravity(getTitleGravity())
                tvTitle!!.setText(toolbarTitleRes())
                toolbar!!.setTitle(toolbarTitleRes())
                toolbar!!.setTitleTextColor(ContextCompat.getColor(this, com.shengdan.base_lib.R.color.title_font_color))
                if (toolbar!!.textTitle != null) {
//                    toolbar.getTextTitle().getPaint().setFakeBoldText(true);
                    toolbar!!.textTitle.setTextSize(17f)
                    val tp = toolbar!!.textTitle.paint
                    tp.isFakeBoldText = true
                }
                getToolRight()?.let {
                    toolbar!!.setRightIcon(it)
                }
            }
            if (isCanGoBack()) {
                toolbar!!.setNavigationIcon(backGetDrawable())
                toolbar!!.setNavigationOnClickListener { v -> goBackClick() }
            } else {
                toolbar!!.navigationIcon = null
            }
        }
    }

    open fun getTitleGravity(): Int {
        return Gravity.CENTER
    }

    open fun getToolRight(): View? {
        return null
    }

    /**
     * 当前toolBar
     */
    open fun backGetDrawable(): Int {
        return com.shengdan.base_lib.R.drawable.app_title_back_normal
    }

    /**
     * 点击返回按钮
     */
    open fun goBackClick() {
        onBackPressed()
    }
}


