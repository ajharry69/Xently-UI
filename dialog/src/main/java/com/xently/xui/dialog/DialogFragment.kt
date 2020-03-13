@file:Suppress("unused")

package com.xently.xui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xently.dialog.ButtonText

/**
 * With this class overridden, there is no need of calling [onCreateDialog] again. In case the
 * intention was to recreate the dialog with a **content area** that shows a custom layout, just
 * override [onCreateDialogFromView] or [dialogContentFromResource]. With either of the 2
 * properties initialized to a non-null value, any initialization of [dialogMessage] or
 * [dialogMessageFromResource] will be ignored i.e. **custom layout** is given a higher priority
 * during [Dialog] creation. To create a message/information showing dialog, use [MessageDialog]
 * instead.
 * ===============================================================================================
 *
 * There are five(5) types of [Dialog]s that extends this class:
 *  1. **Choice**: Use [ChoiceDialog]
 *      1. _**List**_ - without **checkboxes** or **radio buttons**
 *      2. _**Multiple Choice**_ - list items with **checkboxes**
 *      3. _**Single Choice**_ - list items with **radio buttons**
 *  2. **Message** - shows an information in a [Dialog]. Use [MessageDialog]
 *  3. **Custom layout** - shows a custom layout/view in a [Dialog]. To implement, create a subclass
 *  of [DialogFragment] and override [onCreateDialogFromView] or [dialogContentFromResource]
 *  4. **Date picker** - shows date-picker in a [Dialog]. Use [DatePickerDialog]
 *  5. **Time picker** - shows time-picker in a [Dialog]. Use [TimePickerDialog]
 *
 * ===============================================================================================
 * To override all of the [Dialog]'s content and properties, override [setAsDialogProperties] method
 * without calling `dialog.create()`
 */
open class DialogFragment : androidx.fragment.app.DialogFragment() {

    /**
     * Copied from [androidx.fragment.app.DialogFragment]
     */
    /*@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    @IntDef(STYLE_NORMAL, STYLE_NO_TITLE, STYLE_NO_FRAME, STYLE_NO_INPUT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class DialogStyle*/

    enum class LaunchMode { FULLSCREEN, NORMAL }

    /**
     * @see androidx.fragment.app.DialogFragment.STYLE_NORMAL
     * @see androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
     * @see androidx.fragment.app.DialogFragment.STYLE_NO_FRAME
     * @see androidx.fragment.app.DialogFragment.STYLE_NO_INPUT
     */
    enum class Style(val value: Int) {
        /**
         * @see androidx.fragment.app.DialogFragment.STYLE_NORMAL
         */
        NORMAL(STYLE_NORMAL),

        /**
         * @see androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
         */
        NO_TITLE(STYLE_NO_TITLE),

        /**
         * @see androidx.fragment.app.DialogFragment.STYLE_NO_FRAME
         */
        NO_FRAME(STYLE_NO_FRAME),

        /**
         * @see androidx.fragment.app.DialogFragment.STYLE_NO_INPUT
         */
        NO_INPUT(STYLE_NO_INPUT)
    }

    /**
     * Attach listener that responds to [Dialog] button clicks
     * @see onDialogPositiveButtonClick
     * @see onDialogNegativeButtonClick
     * @see onDialogNeutralButtonClick
     */
    interface DialogButtonClickListener {
        /**
         * What to do when [Dialog]'s **positive** button is clicked
         * @param tag set when `Dialog.show(fm: FragmentManager, **tag**: String)` was called. This
         * can be used to identify **same dialog type** initiators. Ex. if a **DatePickerDialog**
         * was shown from different views in the same screen or fragment, inorder to respond to
         * their **button** click you will need to identify them with their **transaction** tags to
         * be able to separate button click behaviours
         * @see DialogButtonClickListener
         */
        fun onDialogPositiveButtonClick(
            dialog: DialogInterface,
            index: Int,
            tag: String?
        )

        /**
         * What to do when [Dialog]'s **negative** button is clicked
         * @param tag set when `Dialog.show(fm: FragmentManager, **tag**: String)` was called. This
         * can be used to identify **same dialog type** initiators. Ex. if a **DatePickerDialog**
         * was shown from different views in the same screen or fragment, inorder to respond to
         * their **button** click you will need to identify them with their **transaction** tags to
         * be able to separate button click behaviours
         * @see DialogButtonClickListener
         */
        fun onDialogNegativeButtonClick(
            dialog: DialogInterface,
            index: Int,
            tag: String?
        ) = dialog.dismiss()

        /**
         * What to do when [Dialog]'s **neutral** button is clicked
         * @param tag set when `Dialog.show(fm: FragmentManager, **tag**: String)` was called. This
         * can be used to identify **same dialog type** initiators. Ex. if a **DatePickerDialog**
         * was shown from different views in the same screen or fragment, inorder to respond to
         * their **button** click you will need to identify them with their **transaction** tags to
         * be able to separate button click behaviours
         * @see DialogButtonClickListener
         */
        fun onDialogNeutralButtonClick(
            dialog: DialogInterface,
            index: Int,
            tag: String?
        ) = dialog.dismiss()
    }

    /**
     * How the dialog should be shown. If [LaunchMode.NORMAL] the default/normal [Dialog] is shown
     * and if [LaunchMode.FULLSCREEN], the dialog is shown in fullscreen
     */
    open var launchMode: LaunchMode = defaultLaunchMode

    /**
     * Style for [setStyle]
     * @see Style
     */
    open var dialogStyle: Style = defaultStyle

    // Resources
    /**
     * Used to create an options menu for [Dialog]s in [LaunchMode.FULLSCREEN] launch-mode
     */
    @MenuRes
    open var dialogOptionsMenuFromResource: Int? = null

    /**
     * What to use as the alert [Dialog]'s theme. If `null`, default app theme is applied
     * @see DialogFragment
     */
    @StyleRes
    open var dialogThemeFromResource: Int? = theme

    /**
     * What to use as the [Dialog]'s **exit** and **entrance** animations
     * @see Window.setWindowAnimations
     */
    @StyleRes
    open var dialogAnimationFromResource: Int? = null

    /**
     * What to use as the alert [Dialog]'s icon. If `null`, no dialogIcon is shown
     * @see DialogFragment
     */
    @DrawableRes
    open var dialogIconFromResource: Int? = null

    /**
     * What to use as the alert [Dialog]'s title. If `null`, no title is shown
     * @see DialogFragment
     */
    @StringRes
    open var dialogTitleFromResource: Int? = null

    /**
     * What to use or show as message of [Dialog]. If `null`, no message is shown
     * @see DialogFragment
     */
    @StringRes
    open var dialogMessageFromResource: Int? = null

    /**
     * What to use as data in the alert dialog's **content area** by calling `dialog.setView(..)`
     * @see onCreateDialogFromView
     * @see DialogFragment
     */
    @LayoutRes
    open var dialogContentFromResource: Int? = null

    // Primitives

    /**
     * What to use as the alert dialog's icon. If `null`, no dialogIcon is shown
     * @see DialogFragment
     */
    open var dialogIcon: Drawable? = null

    /**
     * What to use as the alert [Dialog]'s title. If `null`, no title is shown
     * @see DialogFragment
     */
    open var dialogTitle: String? = null

    /**
     * What to use or show as message of [Dialog]. If `null`, no message is shown
     * @see DialogFragment
     */
    open var dialogMessage: String? = null

    /**
     * Used to set texts to dialog buttons from a strings. If any fields initialization is set to
     * `null` and [setDialogButtonText] implementation is not provided then it's corresponding
     * click implementation is rendered useless. For example, if [ButtonText.positive] = `null`,
     * then **positive** button for the dialog will not be shown
     * @see DialogButtonClickListener
     * @see DialogFragment
     */
    var dialogButtonText: ButtonText? = ButtonText(null)

    /**
     * @see DialogButtonClickListener
     */
    var buttonClickListener: DialogButtonClickListener? = null

    override fun onDetach() {
        buttonClickListener = null
        super.onDetach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (launchMode == LaunchMode.FULLSCREEN) {
            return super.onCreateDialog(savedInstanceState)
                .apply { requestWindowFeature(Window.FEATURE_NO_TITLE) }
        }

        val context = requireContext()

        val dialogButtonText = setDialogButtonText(context)

        setDialogIcon(context)
        setDialogTitle(context)
        setDialogMessage(context)

        val alertDialogBuilder = dialogThemeFromResource?.let {
            MaterialAlertDialogBuilder(context, it)
        } ?: MaterialAlertDialogBuilder(context)

        val view = onCreateView(requireActivity().layoutInflater, null, savedInstanceState)

        with(alertDialogBuilder) {
            // Only add an icon to the dialog iff `dialogIcon` is not null
            dialogIcon?.let { setIcon(it) }
            // Only set a title to the dialog iff `dialogTitle` is not null
            dialogTitle?.let { setTitle(it) }
            if (view != null) {
                // Only set a custom layout to the dialog iff `onCreateDialogFromView` is
                // not null
                setView(view)
            } else {
                // Do not add a message and a custom layout in the content area at the same time
                // Only set a message to the dialog iff `dialogMessage` is not null
                dialogMessage?.let { setMessage(it) }
            }
            // Only show a "positive" button to the dialog and respond to its click iff
            // `dialogPositiveButtonText` is not null
            dialogButtonText?.positive?.let {
                setPositiveButton(it) { d, i ->
                    onDialogPositiveButtonClick(d, i)
                }
            }
            // Only show a "negative" button to the dialog and respond to its click iff
            // `dialogNegativeButtonText` is not null
            dialogButtonText?.negative?.let {
                setNegativeButton(it) { d, i ->
                    onDialogNegativeButtonClick(d, i)
                }
            }
            // Only show a "neutral" button to the dialog and respond to its click iff
            // `dialogNeutralButtonText` is not null
            dialogButtonText?.neutral?.let {
                setNeutralButton(it) { d, i ->
                    onDialogNeutralButtonClick(d, i)
                }
            }
            setAsDialogProperties(this)
        }

        return alertDialogBuilder.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Only enable showing of options menu when in [FULLSCREEN] launch-mode
        if (launchMode == LaunchMode.FULLSCREEN) setHasOptionsMenu(true)
        setStyle(dialogStyle.value, dialogThemeFromResource ?: theme)
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.apply {
                if (launchMode == LaunchMode.FULLSCREEN) {
                    val width = ViewGroup.LayoutParams.MATCH_PARENT
                    val height = ViewGroup.LayoutParams.MATCH_PARENT
                    setLayout(width, height)
                }
                dialogAnimationFromResource?.let { setWindowAnimations(it) }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (launchMode == LaunchMode.FULLSCREEN) {
            if (dialogOptionsMenuFromResource != null) {
                menu.clear()
                dialogOptionsMenuFromResource?.let { inflater.inflate(it, menu) }
            }
        }
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onDialogNegativeButtonClick(this.requireDialog(), Int.MAX_VALUE)
            true
        } else super.onOptionsItemSelected(item)
    }

    /**
     * What to do when **positive** button is clicked. If dialog's **positive** button is initialized
     * `null` through [dialogButtonText]/[setDialogButtonText], then this method's implementation
     * will not be called hence is rendered useless.
     * =======================================================================================
     *
     * **N/B:** Default behaviour is a call to `DialogButtonClickListener.onDialogPositiveButtonClick()`
     * or [Dialog] dismissal **iff** [DialogButtonClickListener] is `null`
     * ([DialogButtonClickListener] is not implemented)
     *
     * =======================================================================================
     * @see dialogButtonText
     * @see DialogFragment
     */
    open fun onDialogPositiveButtonClick(dialog: DialogInterface, index: Int) {
        if (buttonClickListener == null) {
            dialog.dismiss()
            return
        }
        buttonClickListener?.onDialogPositiveButtonClick(dialog, index, tag)
    }

    /**
     * What to do when **negative** button is clicked. If dialog's **negative** button is initialized
     * `null` through [dialogButtonText]/[setDialogButtonText], then this method's implementation
     * will not be called hence is rendered useless.
     * =======================================================================================
     *
     * **N/B:** Default behaviour is a call to `DialogButtonClickListener.onDialogNegativeButtonClick()`
     * or [Dialog] dismissal **iff** [DialogButtonClickListener] is `null`
     * ([DialogButtonClickListener] is not implemented)
     *
     * =======================================================================================
     * @see dialogButtonText
     * @see DialogFragment
     */
    open fun onDialogNegativeButtonClick(dialog: DialogInterface, index: Int) {
        if (buttonClickListener == null) {
            dialog.dismiss()
            return
        }
        buttonClickListener?.onDialogNegativeButtonClick(dialog, index, tag)
    }

    /**
     * What to do when **neutral** button is clicked. If dialog's **neutral** button is initialized
     * `null` through [dialogButtonText]/[setDialogButtonText], then this method's implementation
     * will not be called hence is rendered useless.
     * =======================================================================================
     *
     * **N/B:** Default behaviour is a call to `DialogButtonClickListener.onDialogNeutralButtonClick()`
     * or [Dialog] dismissal **iff** [DialogButtonClickListener] is `null`
     * ([DialogButtonClickListener] is not implemented)
     *
     * =======================================================================================
     * @see DialogFragment
     */
    open fun onDialogNeutralButtonClick(dialog: DialogInterface, index: Int) {
        if (buttonClickListener == null) {
            dialog.dismiss()
            return
        }
        buttonClickListener?.onDialogNeutralButtonClick(dialog, index, tag)
    }

    /**
     * Used to set texts to dialog buttons from a strings. If any fields initialization is set to
     * `null` and [dialogButtonText] implementation is not provided then it's corresponding click
     * implementation is rendered useless. For example, if [ButtonText.positive] = `null`, then
     * **positive** button for the dialog will not be shown
     * @see DialogButtonClickListener
     * @see DialogFragment
     */
    open fun setDialogButtonText(context: Context): ButtonText? = dialogButtonText

    /**
     * To override all of the [Dialog]'s content and it's properties.
     *
     * ==========================================================================================
     *
     * **N/B:** Use this method without calling **`dialog.create()`**. Its already taken care of
     * by the [DialogFragment]
     *
     * ==========================================================================================
     * @see DialogFragment
     */
    open fun setAsDialogProperties(dialog: MaterialAlertDialogBuilder) = Unit

    /**
     * What to use as data in the alert dialog's **content area** by calling `dialog.setView(..)`
     * @see dialogContentFromResource
     * @see DialogFragment
     */
    @Deprecated(
        "To avoid repeatition, this method will be deleted",
        replaceWith = ReplaceWith("onCreateView(requireActivity().layoutInflater, container, savedInstanceState)")
    )
    open fun onCreateDialogFromView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View? = dialogContentFromResource?.let { inflater.inflate(it, container, attachToRoot) }

    /**
     * Initializes [launchMode] and [dialogStyle]
     */
    fun setDialogStyleAndLaunchMode(
        style: Style = defaultStyle,
        launchMode: LaunchMode = defaultLaunchMode
    ) {
        this.launchMode = launchMode
        this.dialogStyle = style
    }

    private fun setDialogIcon(context: Context) {
        if (dialogIcon == null) dialogIcon =
            dialogIconFromResource?.let { ContextCompat.getDrawable(context, it) }
    }

    private fun setDialogTitle(context: Context) {
        if (dialogTitle == null) dialogTitle =
            dialogTitleFromResource?.let { context.getString(it) }
    }

    private fun setDialogMessage(context: Context) {
        if (dialogMessage == null) dialogMessage =
            dialogMessageFromResource?.let { context.getString(it) }
    }

    companion object {
        private val defaultLaunchMode = LaunchMode.NORMAL
        private val defaultStyle = Style.NORMAL
    }
}