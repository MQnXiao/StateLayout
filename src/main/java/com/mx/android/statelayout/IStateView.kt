package com.mx.android.statelayout

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
internal interface IStateView {

    fun show(stateLayout: StateLayout)

    fun hide()

    fun getState(): Int

    fun remove(stateLayout: StateLayout)
}