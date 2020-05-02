package com.xently.xui.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import androidx.core.graphics.drawable.DrawableCompat
import com.xently.xui.utils.Log.Type.*

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal object Log {
    enum class Type {
        ASSERT,
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        VERBOSE
    }

    /**
     * Shows debug (Log.d(...)) if build type is DEBUG
     * @param tag: Log TAG
     * @param message: Log message
     * @param throwable: Exception to accompany the log
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun show(
        tag: String,
        message: String?,
        throwable: Throwable? = null,
        type: Type = DEBUG
    ) {
        if (message != null) {
            when (type) {
                DEBUG -> {
                    if (throwable == null) {
                        Log.d(tag, message)
                        return
                    }
                    Log.d(tag, message, throwable)
                }
                INFO -> {
                    if (throwable == null) {
                        Log.i(tag, message)
                        return
                    }
                    Log.i(tag, message, throwable)
                }
                WARNING -> {
                    if (throwable == null) {
                        Log.w(tag, message)
                        return
                    }
                    Log.w(tag, message, throwable)
                }
                ERROR -> {
                    if (throwable == null) {
                        Log.e(tag, message)
                        return
                    }
                    Log.e(tag, message, throwable)
                }
                VERBOSE -> {
                    if (throwable == null) {
                        Log.v(tag, message)
                        return
                    }
                    Log.v(tag, message, throwable)
                }
                ASSERT -> {
                    if (throwable == null) {
                        Log.wtf(tag, message)
                        return
                    }
                    Log.wtf(tag, message, throwable)
                }
            }
        }
    }

    /**
     * @see show
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun show(tag: String, message: Any?, throwable: Throwable? = null, type: Type = DEBUG) {
        show(tag, "$message", throwable, type)
    }
}

/**
 * Creates and or Returns a [SharedPreferences] by the name [name] in [Context.MODE_PRIVATE]
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun getSharedPref(context: Context, name: String): SharedPreferences =
    context.getSharedPreferences(name, Context.MODE_PRIVATE)

@ColorInt
fun Context.getThemedColor(@AttrRes themeResId: Int): Int {
    val a = obtainStyledAttributes(null, intArrayOf(themeResId))
    try {
        return a.getColor(0, 9)
    } finally {
        a.recycle()
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Drawable.tintDrawable(@ColorInt tint: Int): Drawable {
    return DrawableCompat.wrap(this).mutate().apply {
        setTint(tint)
    }
}

fun Context.isDarkTheme(): Boolean = resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

@JvmOverloads
fun <T, M> Iterable<T>.merge(l1: List<M>, shuffle: Boolean = true): List<Any> =
    arrayListOf<Any>().apply {
        this@merge.forEach {
            add(it as Any)
        }
        if (l1.isEmpty()) return this@apply
        if (shuffle) {
            val offset = size / l1.size + 1
            var index = 0
            for (ad in l1) {
                add(index, ad as Any)
                index += offset
            }
        } else {
            l1.forEach {
                add(it as Any)
            }
        }
    }

operator fun <T, M> Iterable<T>.plus(l1: Iterable<M>): List<Any> = arrayListOf<Any>().apply {
    this@plus.forEach { add(it as Any) }
    l1.forEach { add(it as Any) }
}