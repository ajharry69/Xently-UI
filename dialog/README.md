# Xently Dialog

**Xently Dialog** is an Android utility library to help create most types of (Alert)Dialog(s).

[url-android-context-menu]: https://developer.android.com/guide/topics/ui/menus#context-menu

## Types of Dialogs supported

1. **Message Dialog:**
2. **Choice Dialog:** show a list of options in a dialog. Supports three(3) sub-categories:
   1. _**_List_** - without **checkboxes** or **radio buttons**. Suitable to use as a [context menu][url-android-context-menu]_
   2. _**_Single Choice_** - list items with **radio buttons**_
   3. _**_Multiple Choice_** - list items with **checkboxes**_
3. **DatePicker Dialog:** show date picker(calendar) in a dialog
4. **TimePicker Dialog:** show time picker in a dialog
5. **Custom Dialog:** with a custom layout/view inflated in the content area of dialog

## Quick Start

Add the library to your Android project, then check out the example use case below.

### Gradle Setup

```gradle
// App level gradle file
implementation 'com.xently.xui:dialog:1.0.6'
```

### Maven Setup

```xml
<dependency>
  <groupId>com.xently.xui</groupId>
  <artifactId>dialog</artifactId>
  <version>1.0.6</version>
  <type>pom</type>
</dependency>
```

## Example Use case

A few important tips to help use this library productively

1. Use the `tag` property to differentiate and repond to button clicks of same dialog type(s) sharing same `setOnDialogButtonClickListener(...)` callback implemented on the same activity or fragment class
2. If `setOnDialogButtonClickListener(...)` callback is not implemented when creating a dialog, the default action (dialog dismissal) will be applied when dialog button(s) [is|are] clicked.
3. Use `dialogXently` to differentiate between different dialog types sharing the same `setOnDialogButtonClickListener(...)` callback implemented on the same activity or fragment class
4. Do not call or use `setOnDialogButtonClickListener(...)` with `DatePickerDialog`, `TimePickerDialog` or `ChoiceDialog`. The callbacks will be ignored! Call or use `setOnDatePickerDialogDateSet(...)`, `setOnTimePickerDialogTimeSetListener(...)` or `setOnChoiceDialogItemSelected(...)` respectively instead

### 1. Creating and showing Message Dialog

>```kotlin
>    MessageDialog.getInstance(
>        this,
>        DialogParamsRes(
>            R.string.md_title,
>            R.string.md_message,
>            ButtonTextRes(android.R.string.ok, android.R.string.cancel)
>        )
>    ).apply {
>        setDialogStyleAndLaunchMode(XentlyDialog.Style.NORMAL, XentlyDialog.LaunchMode.NORMAL)
>        onDialogButtonClickListener = this@MainActivity
>    }.show(supportFragmentManager, MESSAGE_DIALOG_TAG)
>```

### 2. Creating and showing Choice Dialog

> #### 1. List - without checkboxes or radio buttons. Suitable to use as a [context menu][url-android-context-menu]
>  
>   >```kotlin
>   >    ChoiceDialog.getInstance(
>   >        choiceDialogData.toList()
>   >    ).apply {
>   >        onChoiceDialogItemSelectedListener = this@MainActivity
>   >    }.show(supportFragmentManager, LIST_CHOICE_DIALOG_TAG)
>   >```
>
> #### 2. Single Choice - list items with radio buttons
>
>   >```kotlin
>   >    ChoiceDialog.getInstance(
>   >        choiceDialogData.toList(),
>   >        ChoiceDialogParams("Title", ButtonText("Hello")),
>   >        type = ChoiceDialog.Type.SINGLE
>   >    ).apply {
>   >        onChoiceDialogItemSelectedListener = this@MainActivity
>   >    }.show(supportFragmentManager, SINGLE_CHOICE_DIALOG_TAG)
>   >```
>
> #### 3. Multiple Choice - list items with checkboxes
>
>   >```kotlin
>   >    ChoiceDialog.getInstance(
>   >        R.array.choice_dialog_entries,
>   >        params = ChoiceDialogParams("Title", ButtonText("Positive111")),
>   >        type = ChoiceDialog.Type.MULTIPLE
>   >    ).apply {
>   >        dialogStyle = XentlyDialog.Style.NO_TITLE
>   >        dialogAnimationFromResource = R.style.AppTheme_Slide
>   >          // type = ChoiceDialog.Type.MULTIPLE
>   >          // entries = choiceDialogData
>   >          // entriesFromResource = R.array.choice_dialog_entries
>   >        onChoiceDialogItemSelectedListener = this@MainActivity
>   >    }.show(supportFragmentManager, MULTIPLE_CHOICE_DIALOG_TAG)
>   >```

### 3. Creating and showing DatePicker Dialog

>```kotlin
>    /*
>     * Do not override `onDialogButtonClickListener(...)` with this type of dialog use
>     * `onDatePickerDialogDateSet(...)` instead
>     * Creates shows a date picker with date set to 2016-05-18
>     */
>     DatePickerDialog.getInstance(initialDate = "  2016-05-18  ").apply {
>         onDatePickerDialogDateSetListener = object :
>             DatePickerDialog.OnDatePickerDialogDateSetListener {
>             override fun onDatePickerDialogDateSet(date: String, tag: String?) {
>                 showSnackBar(date)
>             }
>         }
>     }.show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
>```

### 4. Creating and showing TimePicker Dialog

>```kotlin
>    /*
>     * Do not call `setOnDialogButtonClickListener(...)` with this type of dialog use
>     * `setOnTimePickerDialogTimeSetListener(...)` instead
>     * Creates shows a time picker with time set to 10:23 
>     */
>    TimePickerDialog.getInstance(is24Hours = true, initialTime = "   10:23 ").apply {
>        onTimePickerDialogTimeSetListener = object :
>            TimePickerDialog.OnTimePickerDialogTimeSetListener {
>            override fun onTimePickerDialogTimeSet(time: String, tag: String?) {
>                // Do something with the returned time...
>                showSnackBar(time)
>            }
>        }
>    }.show(supportFragmentManager, TIME_PICKER_DIALOG_TAG)
>```

### 5. Creating and showing Custom Dialog

#### Steps to follow

> - Create a subclass of `XentlyAlertDialog`
> - **Override** `setViewAsDialogContent: View?` or `setResAsDialogContent: Int?`
> - Inflate and return your custom **view** or initialize your **layout** resource

#### Example implementation

>```kotlin
>    package com.xently.xui.dialog
>
>    import android.annotation.SuppressLint
>    import android.text.method.LinkMovementMethod
>    import android.view.View
>    import android.widget.TextView
>    import androidx.core.text.HtmlCompat
>    import com.google.android.material.dialog.MaterialAlertDialogBuilder
>    import com.xently.BuildConfig
>    import com.xently.R
>    import com.xently.xui.dialog.XentlyDialog
>    import org.joda.time.DateTimeFieldType
>    import org.joda.time.Instant
>
>    class AboutDialog : XentlyAlertDialog() {
>
>        override val setResAsTheme: Int?
>            get() = R.style.AppTheme_AlertDialogStyle
>
>        override fun initializeDialogContentFromView(
>            inflater: LayoutInflater,
>            container: ViewGroup?,
>            attachToRoot: Boolean
>        ): View? {
>            val view: View = requireActivity().layoutInflater.inflate(R.layout.fragment_about, null)
>            val tvAppName: TextView = view.findViewById(R.id.tv_fragment_about_app_name)
>            val tvAppVersion: TextView = view.findViewById(R.id.tv_fragment_about_app_version_name)
>            val tvAboutAppText: TextView = view.findViewById(R.id.tv_fragment_about_text)
>            val tvAppCopyright: TextView = view.findViewById(R.id.tv_fragment_about_copyright)
>
>            tvAppName.text = getString(R.string.app_name)
>
>            tvAppVersion.text = getString(R.string.app_version_name, BuildConfig.VERSION_NAME)
>
>            with(tvAboutAppText) {
>                linksClickable = true
>                movementMethod = LinkMovementMethod.getInstance()
>                text = HtmlCompat.fromHtml(
>                    getString(
>                        R.string.about_app,
>                        getString(R.string.app_website),
>                        getString(R.string.app_name)
>                    ),
>                    HtmlCompat.FROM_HTML_MODE_COMPACT
>                )
>            }
>
>            tvAppCopyright.text =
>                getString(R.string.app_footer_copyright, Instant.now()[DateTimeFieldType.year()])
>
>            return view
>        }
>
>        companion object {
>            fun getInstance(): AboutDialog = AboutDialog()
>        }
>
>    }
>```
