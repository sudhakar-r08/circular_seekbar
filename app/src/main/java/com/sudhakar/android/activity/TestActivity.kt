/*
 * Copyright 2013-2017 Petter Ljungqvist (petter@ljungqvist.info)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sudhakar.android.activity

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.sudhakar.android.widget.CircularRangeSeekBar
import com.sudhakar.android.widget.R
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : Activity() {
    val TAG = "TestActivity"


    private val fromTextView by lazy { findViewById<TextView>(R.id.from) }
    private val toTextView by lazy { findViewById<TextView>(R.id.to) }
    private val colorWhite = Color.parseColor("#FFFFFF")
    private val colorGreen = Color.parseColor("#12AD03")
    private val colorYellow = Color.parseColor("#FEC400")
    private val colorBlue = Color.parseColor("#00A9FE")
    private val colorMagenta = Color.parseColor("#FE00A2")
    private val greenArray = intArrayOf(colorGreen, colorGreen, colorWhite)
    private val yellowArray = intArrayOf(colorYellow, colorYellow, colorWhite)
    private val blueArray = intArrayOf(colorBlue, colorBlue, colorWhite)
    private val magentaArray = intArrayOf(colorMagenta, colorMagenta, colorWhite)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val bar: CircularRangeSeekBar = findViewById(R.id.circular_range_seek_bar_2)

        bar.seekBarChangeListener =
            CircularRangeSeekBar.OnSeekChangeListener { _, p1, p2, fromUser ->
                Log.d(TAG, "$p1 - $p2, from user: $fromUser")
                fromTextView.text = "From: $p1"
                toTextView.text = "To: $p2"
            }

        btnBlue.setOnClickListener { updateGradient(blueArray) }
        btnGreen.setOnClickListener { updateGradient(greenArray) }
        btnYellow.setOnClickListener { updateGradient(yellowArray) }
        btnMegenta.setOnClickListener { updateGradient(magentaArray) }
        updateGradient(greenArray)


    }

    private fun updateGradient(colorArray: IntArray) {
        // set image view drawable radial gradient
        imageView2.setImageDrawable(radialGradientDrawable(colorArray))

    }


    // method to generate radial gradient drawable
    private fun radialGradientDrawable(colorArray: IntArray): GradientDrawable {
        return GradientDrawable().apply {
            colors = colorArray
            gradientType = GradientDrawable.RADIAL_GRADIENT
            shape = GradientDrawable.OVAL

            gradientRadius = 300F

            // border around drawable
            setStroke(1, Color.parseColor("#FFFFFF"))
        }
    }
}
