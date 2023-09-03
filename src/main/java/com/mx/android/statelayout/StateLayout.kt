package com.mx.android.statelayout

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 *
 * @author Mx
 * @date 2023/03/08
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mState = State.CONTENT

    private var stateViews = SparseArray<IStateView>(3)

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
            mState = State.CONTENT
        } else {
            stateViews.get(mState).show(this)
        }
    }

    internal fun init(builder: Builder) {
        this.stateViews = builder.stateViews
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

    fun getIStateView(state: Int): IStateView? {
        return stateViews.get(state)
    }


    fun getCurrentStateView() = getIStateView(mState)

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

    fun addStateView(stateView: IStateView) {
        this.stateViews.put(stateView.getState(), stateView)
    }

    fun removeStateView(state: Int) {
        this.stateViews.get(state)?.let {
            this.stateViews.remove(state)
            it.remove(this)
        }
    }


    class Builder(private val target: View) {
        private val mContext = target.context
        private val parent = target.parent as? ViewGroup
        private lateinit var stateLayout: StateLayout
        internal val stateViews = SparseArray<IStateView>(3)

        fun addStateView(stateView: IStateView): Builder {
            stateViews.put(stateView.getState(), stateView)
            return this
        }

        fun addStateView(stateView: BaseStateView): Builder {
            stateViews.put(stateView.getState(), stateView)
            return this
        }

        fun addStateView(state: Int, stateView: View): Builder {
            addStateView(BaseStateView(stateView, state))
            return this
        }

        fun build(): StateLayout {
            if (parent != null) {
                stateLayout = StateLayout(mContext)
                replaceTargetView(parent.indexOfChild(target))
                addStateView(BaseStateView(target, State.CONTENT))
                stateLayout.init(this)
                return stateLayout
            }
            return StateLayout(mContext)
        }

        private fun replaceTargetView(index: Int) {
            val params = target.layoutParams
            parent?.removeView(target)
            target.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            parent?.addView(stateLayout, index, params)
        }

    }


}