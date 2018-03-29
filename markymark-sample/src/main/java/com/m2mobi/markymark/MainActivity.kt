/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.m2mobi.markymarkandroid.MarkyMarkAndroid
import com.m2mobi.markymarkcontentful.ContentfulFlavor
import java.io.BufferedReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layout = findViewById<LinearLayout>(R.id.layout)

        val markDown = loadMarkDownFromAsset(this, "contentful.txt")

        val mark = MarkyMarkAndroid.getMarkyMark(this, ContentfulFlavor())
        mark.parseMarkDown(markDown).forEach { layout.addView(it) }
    }

    private fun loadMarkDownFromAsset(pContext: Context, pFileName: String): String {
        return pContext.assets.open(pFileName).bufferedReader().use(BufferedReader::readText)
    }
}
