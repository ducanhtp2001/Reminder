package com.example.workreminder.usecase

import android.app.AlertDialog
import android.content.Context

class CustomDialog {
    companion object {
        fun showConfirmationDialog(context: Context, message: String, onConfirm: () -> Unit) {
            AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Confirm") { dialog, which ->
                    onConfirm()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}