package com.dev_musashi.note.util

import android.content.res.Resources
import androidx.annotation.StringRes

sealed class SnackBarMessage {
    class StringSnackBar(val message: String): SnackBarMessage()
    class ResourceSnackBar(@StringRes val message: Int) : SnackBarMessage()

    companion object {
        fun SnackBarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackBar -> this.message
                is ResourceSnackBar -> resources.getString(this.message)
            }
        }
    }
}