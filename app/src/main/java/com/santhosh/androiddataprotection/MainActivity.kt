package com.santhosh.androiddataprotection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREFERENCE_KEY = "TESTING_PROTECTED_PREFERENCE"
    }

    private val preference by lazy { CryptoPreference(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // to write a file.
        writeAFile.setOnClickListener {
            if (content.text.isEmpty()) {
                Toast.makeText(this, "Please Enter content", Toast.LENGTH_SHORT).show()
            } else {
                CryptoFileSystem.encryptFile(this, content.text.toString())
            }
        }

        // to read a file
        readAFile.setOnClickListener {
            if (CryptoFileSystem.isFileExist(CryptoFileSystem.getFile(this))) {
                contentViewer.text = CryptoFileSystem.decryptFile(this)
            } else {
                Toast.makeText(this, "Please create a File with content", Toast.LENGTH_SHORT).show()
            }
        }

        // to write a preference
        writeAPreference.setOnClickListener {
            if (content.text.isEmpty()) {
                Toast.makeText(this, "Please Enter content", Toast.LENGTH_SHORT).show()
            } else {
                preference.getPreferenceEdit().putString(PREFERENCE_KEY, content.text.toString())
                    .apply()
            }
        }

        // to read a preference
        readAPreference.setOnClickListener {
            if (preference.preferences.contains(PREFERENCE_KEY)) {
                contentViewer.text = preference.preferences.getString(PREFERENCE_KEY, "")
            } else {
                Toast.makeText(this, "Please add something in preference", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}