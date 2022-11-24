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

package info.ljungqvist.android.activity

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.TextView
import info.ljungqvist.android.widget.BuildConfig
import info.ljungqvist.android.widget.CircularRangeSeekBar
import info.ljungqvist.android.widget.R
import kotlinx.android.synthetic.main.activity_test.*
import mu.KLogging
import org.slf4j.impl.HandroidLoggerAdapter


class TestActivity : Activity() {

    init {
        HandroidLoggerAdapter.DEBUG = BuildConfig.DEBUG;
    }

    private val fromTextView by lazy { findViewById<TextView>(R.id.from) }
    private val toTextView by lazy { findViewById<TextView>(R.id.to) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val bar: CircularRangeSeekBar = findViewById(R.id.circular_range_seek_bar_2)

        bar.seekBarChangeListener = CircularRangeSeekBar.OnSeekChangeListener{ _, p1, p2, fromUser ->
            logger.debug { "$p1 - $p2, from user: $fromUser" }
            fromTextView.text = "From: $p1"
            toTextView.text = "To: $p2"
        }
        // set image view drawable radial gradient
        imageView2.setImageDrawable(radialGradientDrawable())
    }

    companion object : KLogging()

    // method to generate radial gradient drawable
    private fun radialGradientDrawable(): GradientDrawable {
        return GradientDrawable().apply {
            colors = intArrayOf(
                Color.parseColor("#800020"),

                Color.parseColor("#FFFFFF")

            )
            gradientType = GradientDrawable.RADIAL_GRADIENT
            shape = GradientDrawable.OVAL

            gradientRadius = 300F

            // border around drawable
            setStroke(5,Color.parseColor("#FFFFFF"))
        }
    }
}
