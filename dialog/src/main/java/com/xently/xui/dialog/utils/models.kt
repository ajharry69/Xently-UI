package com.xently.dialog

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes

data class ButtonText(
    val positive: String?,
    val negative: String? = null,
    val neutral: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(positive)
        parcel.writeString(negative)
        parcel.writeString(neutral)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ButtonText> {
        override fun createFromParcel(parcel: Parcel): ButtonText = ButtonText(parcel)

        override fun newArray(size: Int): Array<ButtonText?> = arrayOfNulls(size)

        fun fromStringResource(context: Context, @StringRes positive: Int?, @StringRes negative: Int? = null, @StringRes neutral: Int? = null): ButtonText {
            val pos = positive?.let { context.getString(it) }
            val neg = negative?.let { context.getString(it) }
            val neu = neutral?.let { context.getString(it) }
            return ButtonText(pos, neg, neu)
        }
    }
}

data class DialogParams(
    val title: String?,
    val message: String?,
    val buttonText: ButtonText? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ButtonText::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeParcelable(buttonText, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DialogParams> {
        override fun createFromParcel(parcel: Parcel): DialogParams = DialogParams(parcel)

        override fun newArray(size: Int): Array<DialogParams?> = arrayOfNulls(size)

        fun fromStringResource(
            context: Context, @StringRes title: Int?, @StringRes message: Int?,
            buttonText: ButtonText?
        ): DialogParams = DialogParams(
            title?.let { context.getString(it) },
            message?.let { context.getString(it) },
            buttonText
        )
    }
}

data class ChoiceDialogParams(val title: String? = null, val buttonText: ButtonText? = null) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(ButtonText::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeParcelable(buttonText, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ChoiceDialogParams> {
        override fun createFromParcel(parcel: Parcel): ChoiceDialogParams =
            ChoiceDialogParams(parcel)

        override fun newArray(size: Int): Array<ChoiceDialogParams?> = arrayOfNulls(size)

        fun fromStringResource(
            context: Context, @StringRes title: Int?,
            buttonText: ButtonText?
        ): ChoiceDialogParams = ChoiceDialogParams(title?.let { context.getString(it) }, buttonText)
    }
}

data class DateTimePickerParams(val title: String? = null, val buttonText: ButtonText? = null) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(ButtonText::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeParcelable(buttonText, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DateTimePickerParams> {
        override fun createFromParcel(parcel: Parcel): DateTimePickerParams =
            DateTimePickerParams(parcel)

        override fun newArray(size: Int): Array<DateTimePickerParams?> = arrayOfNulls(size)

        fun fromStringResource(
            context: Context, @StringRes title: Int?,
            buttonText: ButtonText? = null
        ): DateTimePickerParams =
            DateTimePickerParams(title?.let { context.getString(it) }, buttonText)
    }
}