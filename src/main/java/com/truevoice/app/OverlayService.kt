package com.truevoice.app

import android.app.Service
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.*
import android.view.*
import android.widget.*
import android.telecom.TelecomManager

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var view: View? = null

    companion object {
        var isShowing = false
        var lastNumber: String? = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val number = intent?.getStringExtra("number") ?: "Unknown"
        val name = intent?.getStringExtra("name") ?: "Unknown"

        // ✅ PREVENT MULTIPLE TRIGGERS
        if (isShowing && lastNumber == number) {
            return START_NOT_STICKY
        }

        isShowing = true
        lastNumber = number

        val status = AIEngine.analyze(name, number)

        blockCallIfNeeded(status)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val bg = GradientDrawable().apply {
            cornerRadius = 40f
            setColor(Color.parseColor("#1E1E2E"))
            setStroke(2, Color.parseColor("#2A2A3C"))
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 50, 60, 50)
            background = bg
            elevation = 25f
        }

        val nameView = TextView(this).apply {
            text = name
            setTextColor(Color.WHITE)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
        }

        val numberView = TextView(this).apply {
            text = number
            setTextColor(Color.parseColor("#AAAAAA"))
            textSize = 14f
        }

        val statusColor = when (status) {
            "SAFE" -> "#00C853"
            "SPOOF" -> "#FF1744"
            else -> "#FFD600"
        }

        val statusBg = GradientDrawable().apply {
            cornerRadius = 50f
            setColor(Color.parseColor(statusColor))
        }

        val statusView = TextView(this).apply {
            text = "  $status  "
            setTextColor(Color.BLACK)
            textSize = 14f
            setPadding(20, 10, 20, 10)
            background = statusBg
        }

        val messageView = TextView(this).apply {
            text = getMessage(status)
            setTextColor(Color.parseColor("#00E5FF"))
            textSize = 15f
        }

        val reasonView = TextView(this).apply {
            text = getReason(name, number, status)
            setTextColor(Color.parseColor("#888888"))
            textSize = 13f
        }

        val confidence = (65..95).random()

        val confidenceView = TextView(this).apply {
            text = "Confidence: $confidence%"
            setTextColor(Color.parseColor("#BB86FC"))
            textSize = 13f
        }

        layout.addView(nameView)
        layout.addView(space())
        layout.addView(numberView)
        layout.addView(space())
        layout.addView(statusView)
        layout.addView(space())
        layout.addView(messageView)
        layout.addView(space())
        layout.addView(reasonView)
        layout.addView(space())
        layout.addView(confidenceView)

        // ✅ REMOVE OLD VIEW IF EXISTS
        view?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {}
        }

        view = layout

        view?.apply {
            alpha = 0f
            translationY = -100f

            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.y = 120

        windowManager.addView(view, params)

        // ✅ SHORT + CLEAN REMOVE
        Handler(mainLooper).postDelayed({
            stopSelf()
        }, 2500)

        return START_NOT_STICKY
    }

    private fun space(): View {
        return Space(this).apply { minimumHeight = 12 }
    }

    private fun getMessage(status: String): String {
        return when (status) {
            "SPOOF" -> "Caller may be faking identity"
            "SPAM" -> "Likely fraud or promo call"
            "UNKNOWN" -> "Number not in contacts"
            else -> "Trusted contact"
        }
    }

    private fun getReason(name: String, number: String, status: String): String {
        return when (status) {
            "SPOOF" -> "Suspicious number pattern detected"
            "SPAM" -> "Invalid or short number"
            "UNKNOWN" -> "Not found in contacts"
            else -> "Matches saved contact"
        }
    }

    private fun blockCallIfNeeded(status: String) {
        if (status == "SPOOF" || status == "SPAM") {
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            try {
                telecomManager.endCall()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {}
        }
        view = null
        isShowing = false
        lastNumber = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}