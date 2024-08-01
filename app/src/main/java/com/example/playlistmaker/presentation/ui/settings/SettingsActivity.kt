package com.example.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.api.settings.SettingsInteractor

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        settingsInteractor = Creator.provideSettingsInteractor(applicationContext as App)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backFromSettings = binding.backFromSettings
        backFromSettings.setOnClickListener {
            finish()
        }

        val themeSwitcher = binding.themeSwitcher

        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val shareButton = binding.shareButton
        shareButton.setOnClickListener {
            val shareMessage = getString(R.string.share_message)
            val shareButtonIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareButtonIntent, null))
        }

        val supportButton = binding.supportButton
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

        val agreementButton = binding.agreementButton
        agreementButton.setOnClickListener {
            val agreement = getString(R.string.yp_agreement)
            val agreementButtonIntent =Intent(Intent.ACTION_VIEW)
            agreementButtonIntent.data = Uri.parse(agreement)
            startActivity(agreementButtonIntent)
        }
    }
}