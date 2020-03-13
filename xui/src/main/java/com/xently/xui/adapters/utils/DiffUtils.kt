package com.xently.xui.adapters.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtil<T> : DiffUtil.ItemCallback<T>() {
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}