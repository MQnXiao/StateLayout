package com.mx.android.statelayout

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
open class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mState = State.CONTENT

    private var stateViews = SparseArray<BaseStateView>(4)

    constructor(context: Context, builder: Builder) : this(context) {
        this.stateViews = builder.stateViews
    }

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        val contentLayoutId = typedArray.getResourceId(R.styleable.StateLayout_content_layout, 0)
        val emptyLayoutId = typedArray.getResourceId(R.styleable.StateLayout_empty_layout, 0)
        val errorLayoutId = typedArray.getResourceId(R.styleable.StateLayout_error_layout, 0)
        val loadingLayoutId = typedArray.getResourceId(R.styleable.StateLayout_loading_layout, 0)

        if (contentLayoutId != 0) {
            stateViews.put(State.CONTENT, BaseStateView(contentLayoutId, State.CONTENT))
        }
        if (emptyLayoutId != 0) {
            stateViews.put(State.EMPTY, BaseStateView(emptyLayoutId, State.EMPTY))
        }
        if (loadingLayoutId != 0) {
            stateViews.put(State.LOADING, BaseStateView(loadingLayoutId, State.LOADING))
        }
        if (errorLayoutId != 0) {
            stateViews.put(State.ERROR, BaseStateView(errorLayoutId, State.ERROR))
        }
        mState = typedArray.getInt(R.styleable.StateLayout_default_State, State.CONTENT)
        typedArray.recycle()

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            stateViews.put(State.CONTENT, BaseStateView(getChildAt(0), State.CONTENT))
        }
        stateViews.get(mState).show(this)
    }

    fun showStateView(state: Int) {
        if (state != mState) {
            val stateView = stateViews.get(state) ?: return
            stateView.show(this)
            stateViews.get(mState)?.hide()
            mState = state
        }
    }

    fun getStateView(state: Int): BaseStateView? {
        return stateViews.get(state)
    }

    fun getCurrentStateView() = getStateView(mState)

    fun showContent() {
        showStateView(State.CONTENT)
    }

    fun showEmpty() {
        showStateView(State.EMPTY)
    }

    fun showLoading() {
        showStateView(State.LOADING)
    }

    fun showError() {
        showStateView(State.ERROR)
    }

    fun addStateView(stateView: BaseStateView) {
        stateViews.put(stateView.getState(), stateView)
    }

    fun addStateView(state: Int, stateView: View) {
        if (getStateView(state) != null) {
            removeStateView(state)
        }
        addStateView(BaseStateView(stateView, state))
    }

    fun addStateView(state: Int, @LayoutRes layoutId: Int) {
        if (getStateView(state) != null) {
            removeStateView(state)
        }
        addStateView(BaseStateView(layoutId, state))
    }


    fun removeStateView(state: Int) {
        this.stateViews.get(state)?.let {
            this.stateViews.remove(state)
            it.remove(this)
        }
    }


    class Builder(private val target: View) {
        internal val stateViews = SparseArray<BaseStateView>(3)

        fun addStateView(stateView: BaseStateView): Builder {
            stateViews.put(stateView.getState(), stateView)
            return this
        }

        fun addStateView(state: Int, stateView: View): Builder {
            addStateView(BaseStateView(stateView, state))
            return this
        }

        fun addStateView(state: Int, @LayoutRes layoutId: Int): Builder {
            addStateView(BaseStateView(layoutId, state))
            return this
        }

        fun build(): StateLayout? {
            val parent = target.parent as? ViewGroup ?: return null
            addStateView(BaseStateView(target, State.CONTENT))
            val stateLayout = StateLayout(target.context, this)
            stateLayout.layoutParams = target.layoutParams
            val index = parent.indexOfChild(target)
            parent.removeView(target)
            parent.addView(stateLayout, index)
            stateLayout.getStateView(State.CONTENT)?.show(stateLayout)
            return stateLayout
        }
    }
}