package com.mx.android.statelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
open class BaseStateView : IStateView {

    protected val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    private var attach = false

    protected var view: View? = null
        private set

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


    override fun show(stateLayout: StateLayout) {
        view = view ?: createView(stateLayout.context)
        if (!attach) {
            attach = true
            if (stateLayout.indexOfChild(view) == -1) {
                stateLayout.addView(view, layoutParams)
            }
            onAttach()
        }
        view?.visibility = View.VISIBLE
        onShow()
    }

    override fun hide() {
        view?.visibility = View.GONE
        onHide()
    }

    override fun remove(stateLayout: StateLayout) {
        if (attach && stateLayout.indexOfChild(view) != -1) {
            stateLayout.removeView(view)
            onDetach()
        }
    }

    protected open fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(getLayoutId(), null)
    }

    override fun getState(): Int {
        return mState
    }

    @LayoutRes
    protected open fun getLayoutId(): Int = layoutId

    fun setWidth(width: Int) {
        layoutParams.width = width
        view?.layoutParams = layoutParams
    }

    fun setHeight(height: Int) {
        layoutParams.height = height
        view?.layoutParams = layoutParams
    }

    protected open fun onAttach() {}
    protected open fun onDetach() {}
    protected open fun onShow() {}

    protected open fun onHide() {}
}