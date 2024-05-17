package com.example.balo.shareview.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.balo.utils.Utils

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    protected lateinit var dialog: AlertDialog
    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, null)
        context?.let {
            dialog = Utils.showProgressDialog(it)
        }
        initData()
        initView()
        initListener()
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B

    protected val baseActivity: BaseActivity<*>?
        protected get() = activity as BaseActivity<*>?

    fun toast(content: String?) {
        baseActivity?.toast(content)
    }

    fun backPress() {
        baseActivity?.onBackPressed()
    }
}