package com.example.qiitaclient.presentation.viewmodel.common

import android.app.Activity
import androidx.appcompat.app.AlertDialog

fun showErrorDialog(
    activity: Activity,
    retryFetch: () -> Unit
) {
    AlertDialog.Builder(activity)
        .setTitle("エラー")
        .setMessage("エラーが発生しました")
        .setPositiveButton("リトライ") { dialog, _ ->
            retryFetch()
            dialog.dismiss()
        }
        .show()
}