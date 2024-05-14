package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val backFromSettings = findViewById<ImageView>(R.id.back_from_settings)
        backFromSettings.setOnClickListener {
            val backFromSettingsIntent = Intent(this, MainActivity::class.java)
            startActivity(backFromSettingsIntent)
        }

        val shareButton = findViewById<ImageButton>(R.id.shareButton)
        shareButton.setOnClickListener {
            val shareMessage = getString(R.string.share_message)
            val shareButtonIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareButtonIntent, null))
        }

        val supportButton = findViewById<ImageButton>(R.id.supportButton)
        supportButton.setOnClickListener {
            val recipient = getString(R.string.support_mail_recipent)
            val subject = getString(R.string.support_mail_subject)
            val mailBody = getString(R.string.support_mail_body)
            val supportButtonIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, mailBody)
            }
            startActivity(supportButtonIntent)
        }

        val agreementButton = findViewById<ImageButton>(R.id.agreementButton)
        agreementButton.setOnClickListener {
            val agreement = getString(R.string.yp_agreement)
            val agreementButtonIntent =Intent(Intent.ACTION_VIEW)
            agreementButtonIntent.data = Uri.parse(agreement)
            startActivity(agreementButtonIntent)
        }
    }
}