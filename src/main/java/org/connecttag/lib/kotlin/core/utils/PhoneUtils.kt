package org.connecttag.lib.kotlin.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.net.toUri

/**
 * Utility for phone-related actions like calls, SMS, and contacts.
 */
object PhoneUtils {

    /**
     * Opens the dialer with the specified [phoneNumber].
     */
    fun makeCall(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri())
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Opens the SMS app with the specified [phoneNumber] and [message].
     */
    fun sendSms(context: Context, phoneNumber: String, message: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO, "smsto:$phoneNumber".toUri()).apply {
                if (message.isNotEmpty()) {
                    putExtra("sms_body", message)
                }
            }
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Opens the "Add Contact" screen.
     */
    fun addContact(context: Context, name: String, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                type = ContactsContract.Contacts.CONTENT_TYPE
                putExtra(ContactsContract.Intents.Insert.NAME, name)
                putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
            }
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Extension to add a contact.
 */
fun Context.addContact(name: String, phoneNumber: String) {
    PhoneUtils.addContact(this, name, phoneNumber)
}

/**
 * Extension to make a call.
 */
fun Context.makeCall(phoneNumber: String) {
    PhoneUtils.makeCall(this, phoneNumber)
}

/**
 * Extension to send an SMS.
 */
fun Context.sendSms(phoneNumber: String, message: String = "") {
    PhoneUtils.sendSms(this, phoneNumber, message)
}
