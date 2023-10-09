package athallah.neardeal.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import athallah.neardeal.R

private var currentDialog: Dialog? = null

fun Context.showCustomDialog(layoutRes: Int, isCancelAble: Boolean = true): Dialog {
    val view = LayoutInflater.from(this)
        .inflate(layoutRes, null)
    val dialog = AlertDialog.Builder(this)
        .setView(view)
        .setCancelable(isCancelAble)
        .create()
    dialog.show()
    return dialog
}

fun Context.showLoading(message: String) {
    if (currentDialog != null) return

    val dialog = showCustomDialog(R.layout.dialog_loading, false)
    dialog.findViewById<TextView>(R.id.message_tv).text = message
    currentDialog = dialog
}

fun dismissLoading() {
    currentDialog?.dismiss()
    currentDialog = null
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}

fun Context.showAlert(
    title: String = getString(R.string.attention),
    message: String,
    titleBtn: String = getString(R.string.okay),
    isCancelAble: Boolean = true,
    listener: (() -> Unit)? = null) {
    val alertDialog = AlertDialog.Builder(this)
        .setIcon(R.mipmap.ic_launcher)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(isCancelAble)
        .setPositiveButton(titleBtn) { dialog, _ ->
            dialog.dismiss()
            listener?.invoke()
        }
    alertDialog.show()
}