package com.sliide.usermanager.core

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.sliide.usermanager.R

object AlertDialogFactory {

    fun create(
        context: Context,
        type: Type,
        message: String = "",
        positiveAction: () -> Unit
    ): AlertDialog {
        return when (type) {
            Type.USER_SERVICE_ERROR -> UserAlert.UserServiceError(context)
                .createAlert(message, positiveAction)
            Type.NO_USERS_ERROR -> UserAlert.NoUsersError(context)
                .createAlert(message, positiveAction)
            Type.DELETE_USER_ALERT -> UserAlert.DeleteUserAlert(context)
                .createAlert(message, positiveAction)
        }
    }

    enum class Type {
        USER_SERVICE_ERROR,
        NO_USERS_ERROR,
        DELETE_USER_ALERT
    }
}

sealed class UserAlert(context: Context) : AlertDialog(context) {
    abstract fun createAlert(message: String, positiveAction: () -> Unit): AlertDialog

    class UserServiceError(context: Context) : UserAlert(context) {

        override fun createAlert(message: String, positiveAction: () -> Unit): AlertDialog {
            return Builder(context)
                .setTitle(context.getString(R.string.alert_dialog_title_user_api_error))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.alert_dialog_positive_retry)) { _, _ -> positiveAction.invoke() }
                .setNegativeButton(context.getString(R.string.alert_dialog_negative_cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
        }
    }

    class NoUsersError(context: Context) : UserAlert(context) {

        override fun createAlert(message: String, positiveAction: () -> Unit): AlertDialog {
            return Builder(context)
                .setTitle(context.getString(R.string.alert_dialog_title_no_users))
                .setMessage(context.getString(R.string.alert_dialog_message_no_users))
                .setPositiveButton(context.getString(R.string.alert_dialog_positive_ok)) { dialogInterface, _ -> dialogInterface.dismiss() }
                .setNegativeButton(context.getString(R.string.alert_dialog_negative_cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
        }
    }

    class DeleteUserAlert(context: Context) : UserAlert(context) {

        override fun createAlert(message: String, positiveAction: () -> Unit): AlertDialog {
            return Builder(context)
                .setTitle(context.getString(R.string.alert_dialog_title_delete_users))
                .setMessage(context.getString(R.string.alert_dialog_message_delete_users))
                .setPositiveButton(context.getString(R.string.alert_dialog_positive_ok)) { _, _ -> positiveAction.invoke() }
                .setNegativeButton(context.getString(R.string.alert_dialog_negative_cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
        }
    }
}