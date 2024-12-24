package com.akexorcist.ruammij.ui.osslicense

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.ruammij.ui.theme.RuamMijTheme

class OpensourceLicenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuamMijTheme {
                OpensourceLicenseScreen()
            }
        }
    }
}
