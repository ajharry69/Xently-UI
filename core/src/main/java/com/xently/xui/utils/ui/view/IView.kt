package com.xently.xui.utils.ui.view

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface IView {

    /**
     * Shows [views] by applying [View.VISIBLE] to every [View] in [views]
     * @param views Array of [View]s whose visibility is to be changed to [View.VISIBLE]
     * (Shown)
     */
    fun showViews(vararg views: View?) = views.forEach {
        if (it != null && !it.isVisible) it.visibility = View.VISIBLE
    }

    /**
     * Hides [views] by applying [View.INVISIBLE] to each of them
     * @param views Array of [View]s to be hidden
     */
    fun hideViews(vararg views: View?) = views.forEach { view ->
        view?.let {
            if (it.isVisible) it.visibility = View.INVISIBLE
        }
    }

    /**
     * Hides [views] by applying [View.GONE] to each of them
     * @param views Array of [View]s to be hidden
     */
    fun hideViewsCompletely(vararg views: View?) = views.forEach { view ->
        view?.let {
            if (it.isVisible) it.visibility = View.GONE
        }
    }

    /**
     * Disables all [views]
     * @param views: Array of [View]s to be disabled
     */
    fun disableViews(vararg views: View?) = views.forEach {
        if (it != null && it.isEnabled) it.isEnabled = false
    }

    /**
     * Enables all [views]
     * @param views: Array of [View]s to be enabled
     */
    fun enableViews(vararg views: View?) = views.forEach {
        if (it != null && !it.isEnabled) it.isEnabled = true
    }

    /**
     * Shows [this@showAndDisableViews] and or disables all [views]
     * @receiver this@showAndDisableViews: View to show
     * @param views: Array of [View]s to be disabled
     * @see showViews
     * @see disableViews
     */
    fun View?.showThenDisableViews(vararg views: View?) {
        showViews(this)
        disableViews(*views)
    }

    fun showAndDisableViews(vararg views: View?) {
        views.forEach {
            showViews(it)
            disableViews(it)
        }
    }

    fun showAndEnableViews(vararg views: View?) {
        views.forEach {
            showViews(it)
            enableViews(it)
        }
    }

    /**
     * Hides [this@hideCompletelyAndEnableViews] (applies [View.GONE]) and enable all [views]
     * @receiver this@hideCompletelyAndEnableViews: [View] to be hidden completely
     * @param views: Array of [View]s
     * @see hideViewsCompletely
     * @see enableViews
     */
    fun View?.hideCompletelyThenEnableViews(vararg views: View?) {
        hideViewsCompletely(this)
        enableViews(*views)
    }

    /**
     * Hides [this@hideAndEnableViews] (applies [View.INVISIBLE]) and enable all [views]
     * @receiver this@hideAndEnableViews: View to hide
     * @param views: Array of [View]s to be enabled
     * @see hideViews
     * @see enableViews
     */
    fun View?.hideThenEnableViews(vararg views: View?) {
        hideViews(this)
        enableViews(*views)
    }

    fun hideAndDisableViews(vararg views: View?) {
        views.forEach {
            hideViews(it)
            disableViews(it)
        }
    }

    fun hideAndEnableViews(vararg views: View?) {
        views.forEach {
            hideViews(it)
            enableViews(it)
        }
    }

    /**
     * Clears all [EditText]s provided as parameter
     * @param ets: Array of [EditText]s
     */
    fun clearText(vararg ets: TextView?) = ets.forEach {
        if (it != null) {
            if (it is EditText) it.setText("") else it.text = ""
        }
    }

    /**
     * Removes error texts from supported views
     * @param textFields Array of [View]s that supports error setting ([TextInputLayout],
     * [EditText] or [TextInputEditText])
     */
    fun removeErrors(vararg textFields: TextInputLayout?) = textFields.forEach {
        it?.apply {
            error = null
            isErrorEnabled = false
        }
    }

    /**
     * Set [error] as error message on a [TextInputLayout]
     */
    fun TextInputLayout?.setErrorText(error: CharSequence?) {
        this?.apply {
            removeErrors(this)
            this.error = error
        }
    }

    /**
     * @see setErrorText
     */
    fun TextInputLayout?.setErrorText(context: Context, @StringRes error: Int) {
        this.setErrorText(context.getString(error))
    }

    /**
     * @see setErrorText
     */
    fun TextInputLayout?.setErrorTextAndFocus(error: CharSequence?) {
        this?.apply {
            setErrorText(error)
            editText?.requestFocus()
        }
    }

    val TextInputLayout?.disableEditing: Unit
        get() {
            this?.apply {
                editText?.apply {
                    if (isFocusable) isFocusable = false
                    if (isCursorVisible) isCursorVisible = false
                }
            }
        }

    val TextInputLayout?.enableEditing: Unit
        get() {
            this?.apply {
                editText?.apply {
                    if (!isFocusable) isFocusable = true
                    if (!isCursorVisible) isCursorVisible = true
                }
            }
        }

    /**
     * @see setErrorTextAndFocus
     */
    fun TextInputLayout?.setErrorTextAndFocus(context: Context, @StringRes error: Int) {
        this?.setErrorTextAndFocus(context.getString(error))
    }

    fun EditText?.setErrorText(error: CharSequence?) {
        val et = this ?: return
        et.error = error
    }

    fun EditText?.setErrorText(context: Context, @StringRes error: Int) =
        this.setErrorText(context.getString(error))

    /**
     * @see setErrorText
     */
    fun EditText?.setErrorTextAndFocus(error: CharSequence?) {
        this?.apply {
            setErrorText(error)
            requestFocus()
        }
    }

    fun EditText?.setErrorTextAndFocus(context: Context, @StringRes error: Int) =
        this.setErrorTextAndFocus(context.getString(error))

    fun TextView.useText(text: CharSequence?) {
        text?.let {
            if (this is EditText) {
                this.text = Editable.Factory.getInstance().newEditable(it)
            } else {
                this.text = it
            }
        } ?: clearText()
    }

    fun TextView.useText(context: Context, @StringRes text: Int, vararg args: Any) =
        useText(context.getString(text, *args))

    fun TextView.switchTextOnVisibilityChange(
        textOnVisible: CharSequence?,
        textOnInvisible: CharSequence?
    ) = if (isVisible) useText(textOnVisible) else useText(textOnInvisible)

    fun TextView.switchTextOnVisibilityChange(
        context: Context,
        @StringRes textOnVisible: Int,
        @StringRes textOnInvisible: Int
    ) = switchTextOnVisibilityChange(
        context.getString(textOnVisible),
        context.getString(textOnInvisible)
    )

    fun TextView.switchTextOnClick(
        textOnFirstClick: CharSequence?,
        textOnSecondClick: CharSequence?
    ) {
        setOnClickListener {
            switchText(textOnFirstClick, textOnSecondClick)
        }
    }

    fun TextView.switchText(textOnFirstClick: CharSequence?, textOnSecondClick: CharSequence?) {
        if (text == textOnFirstClick) useText(textOnSecondClick) else useText(textOnFirstClick)
    }

    fun TextView.switchText(
        context: Context,
        @StringRes textOnFirstClick: Int,
        @StringRes textOnSecondClick: Int
    ) {
        switchText(context.getString(textOnFirstClick), context.getString(textOnSecondClick))
    }

    fun TextView.switchTextOnClick(
        context: Context,
        @StringRes textOnFirstClick: Int,
        @StringRes textOnSecondClick: Int
    ) = switchTextOnClick(context.getString(textOnFirstClick), context.getString(textOnSecondClick))

    fun View.alternateViewVisibilityOnClick(view: View) {
        setOnClickListener {
            view.alternateVisibility()
        }
    }

    fun View.alternateVisibility() {
        if (isVisible) {
            hideViewsCompletely(this)
        } else {
            showViews(this)
        }
    }

    fun TextView.switchTextAndAlternateViewVisibilityOnClick(
        context: Context,
        @StringRes textOnFirstClick: Int,
        @StringRes textOnSecondClick: Int,
        view: View
    ) {
        switchTextAndAlternateViewVisibilityOnClick(
            context.getString(textOnFirstClick),
            context.getString(textOnSecondClick),
            view
        )
    }

    fun TextView.switchTextAndAlternateViewVisibilityOnClick(
        textOnFirstClick: CharSequence?,
        textOnSecondClick: CharSequence?,
        view: View
    ) {
        setOnClickListener {
            view.alternateVisibility()
            switchText(textOnFirstClick, textOnSecondClick)
        }
    }

    /**
     * Hides software keyboard
     */
    fun hideKeyboard(context: Context, view: View?): Boolean {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            ?: return false
        return imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**
     * Calls view's [View.OnClickListener] after hiding keyboard
     */
    fun View.setOnClickListenerWithKeyboardHidden(onClick: (view: View) -> Unit) {
        this.setOnClickListener {
            hideKeyboard(it.context, it)
            onClick.invoke(it)
        }
    }

    fun TabLayout.selectTabWithName(name: String) {
        val tabIndices: IntRange = 0 until this.tabCount
        for (tabIndex in tabIndices) {
            val tab = this.getTabAt(tabIndex) ?: continue
            if (tab.text.toString().equals(name, true)) {
                this.selectTab(tab)
                break
            }
        }
    }
}