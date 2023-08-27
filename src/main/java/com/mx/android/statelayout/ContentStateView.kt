package com.mx.android.statelayout

import android.view.View

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
class ContentStateView(view: View) : BaseStateView(view) {
    override fun getState(): State {
        return State.CONTENT
    }
}