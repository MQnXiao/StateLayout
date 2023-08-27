package com.mx.android.statelayout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
abstract class BaseStateView(protected var view: View? = null) : IStateView {

    private var attach = false

    private var mState: State? = null

    final override fun show(stateLayout: StateLayout) {
        view = view ?: getView(stateLayout.context)
        if (!attach) {
            attach = true
            stateLayout.addView(view)
            onAttach()
        }
        view?.isVisible = true
        onShow()
    }

    final override fun hide(stateLayout: StateLayout) {
        view?.isVisible = false
        onHide()
    }

    protected open fun getView(context: Context): View {
        return LayoutInflater.from(context).inflate(getLayoutId(), null)
    }

    override fun getState(): State {
        return mState ?: let {
            val state = State.create(getViewState())
            mState = state
            return state
        }
    }

    protected open fun getViewState(): Int = State.EMPTY.value

    @LayoutRes
    protected open fun getLayoutId(): Int = 0

    protected open fun onAttach() {}
    protected open fun onShow() {}

    protected open fun onHide() {}
}