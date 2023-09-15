package com.mx.android.statelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.core.view.contains
import androidx.core.view.isVisible

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
open class BaseStateView : IStateView {

    private var attach = false

    private var view: View? = null

    private var mState: Int = -1

    @LayoutRes
    private var layoutId = 0

    constructor()
    constructor(view: View, state: Int) {
        this.view = view
        this.mState = state
    }

    constructor(@LayoutRes layoutId: Int, state: Int) {
        this.layoutId = layoutId
        this.mState = state
    }


    final override fun show(stateLayout: StateLayout) {
        view = view ?: getView(stateLayout.context)
        if (!attach) {
            attach = true
            if (stateLayout.indexOfChild(view)==-1) {
                stateLayout.addView(view)
            }
            onAttach()
        }
        view?.visibility = View.VISIBLE
        onShow()
    }

    final override fun hide() {
        view?.visibility = View.GONE
        onHide()
    }

    override fun remove(stateLayout: StateLayout) {
        if (attach&&stateLayout.indexOfChild(view)!=-1) {
            stateLayout.removeView(view)
            onDetach()
        }
    }

    protected open fun getView(context: Context): View {
        return LayoutInflater.from(context).inflate(getLayoutId(), null)
    }

    override fun getState(): Int {
        return mState
    }

    @LayoutRes
    protected open fun getLayoutId(): Int = layoutId

    protected open fun onAttach() {}
    protected open fun onDetach() {}
    protected open fun onShow() {}

    protected open fun onHide() {}
}