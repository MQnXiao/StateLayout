package com.mx.android.statelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mx.android.statelayout.databinding.CommonDefStateViewBinding

/**
 *
 * @author Mx
 * @date 2023/03/10
 */
open class DefaultStateView : BaseStateView() {
    protected lateinit var binding: CommonDefStateViewBinding
    override fun getView(context: Context): View {
        binding = CommonDefStateViewBinding.inflate(LayoutInflater.from(context), null, false)
        return binding.root
    }
}