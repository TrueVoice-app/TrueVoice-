package com.truevoice.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {

            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            // ✅ CALL RINGING
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {

                val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) ?: "Unknown"

                val name = getContactName(context, number)

                Toast.makeText(context, "Incoming: $name", Toast.LENGTH_SHORT).show()

                val serviceIntent = Intent(context, OverlayService::class.java)
                serviceIntent.putExtra("number", number)
                serviceIntent.putExtra("name", name)

                context.startService(serviceIntent)
            }

            // ✅ CALL ENDED → REMOVE POPUP
            if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                val stopIntent = Intent(context, OverlayService::class.java)
                context.stopService(stopIntent)
            }
        }
    }

    // 🔥 FIXED CONTACT FETCH (100% RELIABLE)
    private fun getContactName(context: Context, phoneNumber: String): String {

        if (phoneNumber.isEmpty() || phoneNumber == "Unknown") return "Unknown"

        return try {
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
            )

            val cursor = context.contentResolver.query(
                uri,
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                null,
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    return it.getString(0) ?: "Unknown"
                }
            }

            "Unknown"

        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown"
        }
    }
}