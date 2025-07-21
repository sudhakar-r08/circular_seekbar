# Circular SeekBar

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

A customizable circular SeekBar (progress bar) for Android that supports various shapes including full circles, semi-circles, and custom arcs. Perfect for volume controls, progress indicators, ratings, and other circular UI elements.

## Features

- ✨ **Multiple Shapes**: Full circle, semi-circle, and custom arc angles
- 🎨 **Highly Customizable**: Colors, stroke width, progress animation
- 👆 **Touch Interaction**: Smooth drag and tap to set progress
- 🔄 **Animation Support**: Smooth progress transitions with customizable duration
- 📱 **Android Compatible**: Works on API 16+ (Android 4.1+)
- 🎯 **Easy Integration**: Simple XML attributes and programmatic API
- 🔧 **Flexible Progress**: Support for float progress values
- 📏 **Custom Sizing**: Adaptive to different screen sizes

## Screenshots

<div align="center">
  <img src="https://user-images.githubusercontent.com/17586972/204727946-a5343b70-5186-4c4a-95dd-a24630d05136.png" alt="Circular SeekBar Demo" width="400"/>
</div>

## Installation

### Gradle (Recommended)

Add it to your project-level `build.gradle`:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency in your app-level `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.sudhakar-r08:circular_seekbar:1.0.0'
}
```

### Manual Installation

1. Download the library
2. Import the module into your project
3. Add module dependency in your app's `build.gradle`

## Usage

### Basic XML Implementation

```xml
<com.yourpackage.CircularSeekBar
    android:id="@+id/circularSeekBar"
    android:layout_width="200dp"
    android:layout_height="200dp"
    app:circle_color="#CCCCCC"
    app:circle_progress_color="#FF4081"
    app:circle_stroke_width="20dp"
    app:max="100"
    app:progress="50" />
```

### Programmatic Usage

```java
CircularSeekBar seekBar = findViewById(R.id.circularSeekBar);

// Set progress
seekBar.setProgress(75);

// Set max value
seekBar.setMax(100);

// Set progress change listener
seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        // Handle progress change
        Log.d("CircularSeekBar", "Progress: " + progress);
    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {
        // Called when user starts touching the seek bar
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {
        // Called when user stops touching the seek bar
    }
});
```

### Kotlin Usage

```kotlin
val seekBar = findViewById<CircularSeekBar>(R.id.circularSeekBar)

seekBar.apply {
    progress = 75
    max = 100
    
    setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener {
        override fun onProgressChanged(circularSeekBar: CircularSeekBar, progress: Int, fromUser: Boolean) {
            Log.d("CircularSeekBar", "Progress: $progress")
        }
        
        override fun onStartTrackingTouch(seekBar: CircularSeekBar) {
            // Handle start tracking
        }
        
        override fun onStopTrackingTouch(seekBar: CircularSeekBar) {
            // Handle stop tracking
        }
    })
}
```

## XML Attributes

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `circle_color` | color | #CCCCCC | Background circle color |
| `circle_progress_color` | color | #FF4081 | Progress arc color |
| `circle_stroke_width` | dimension | 20dp | Width of the circle stroke |
| `start_angle` | integer | 0 | Starting angle of the arc (0-360) |
| `sweep_angle` | integer | 360 | Sweep angle of the arc (0-360) |
| `max` | integer | 100 | Maximum progress value |
| `progress` | integer | 0 | Current progress value |
| `touch_disabled` | boolean | false | Disable touch interaction |
| `rounded_edges` | boolean | false | Enable rounded line caps |
| `animation_duration` | integer | 300 | Progress animation duration (ms) |

## Advanced Usage

### Semi-Circle SeekBar

```xml
<com.yourpackage.CircularSeekBar
    android:layout_width="200dp"
    android:layout_height="100dp"
    app:start_angle="180"
    app:sweep_angle="180"
    app:circle_color="#E0E0E0"
    app:circle_progress_color="#2196F3" />
```

### Custom Arc SeekBar

```xml
<com.yourpackage.CircularSeekBar
    android:layout_width="200dp"
    android:layout_height="200dp"
    app:start_angle="135"
    app:sweep_angle="270"
    app:rounded_edges="true"
    app:circle_stroke_width="15dp" />
```

### Disable Touch Interaction (Progress Bar Mode)

```xml
<com.yourpackage.CircularSeekBar
    android:layout_width="150dp"
    android:layout_height="150dp"
    app:touch_disabled="true"
    app:circle_progress_color="#4CAF50" />
```

## API Methods

### Progress Methods
```java
int getProgress()                    // Get current progress
void setProgress(int progress)       // Set progress without animation
void setProgress(int progress, boolean animate)  // Set progress with optional animation
int getMax()                        // Get maximum value
void setMax(int max)                // Set maximum value
```

### Customization Methods
```java
void setCircleColor(int color)              // Set background circle color
void setCircleProgressColor(int color)      // Set progress color
void setCircleStrokeWidth(int width)        // Set stroke width
void setStartAngle(int angle)               // Set start angle
void setSweepAngle(int angle)               // Set sweep angle
void setRoundedEdges(boolean rounded)       // Enable/disable rounded edges
```

### Animation Methods
```java
void setAnimationDuration(int duration)     // Set animation duration
void animateProgress(int toProgress)        // Animate to target progress
```

## Examples

### Volume Control
```java
CircularSeekBar volumeControl = findViewById(R.id.volume_seekbar);
volumeControl.setMax(15); // Max volume level
volumeControl.setProgress(8); // Current volume
volumeControl.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
    @Override
    public void onProgressChanged(CircularSeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }
    }
    // ... other methods
});
```

### Loading Progress
```java
CircularSeekBar loadingProgress = findViewById(R.id.loading_progress);
loadingProgress.setTouchDisabled(true); // Disable user interaction
loadingProgress.setMax(100);

// Simulate loading progress
ValueAnimator animator = ValueAnimator.ofInt(0, 100);
animator.setDuration(3000);
animator.addUpdateListener(animation -> {
    loadingProgress.setProgress((Integer) animation.getAnimatedValue());
});
animator.start();
```

## Requirements

- **Minimum SDK**: API 16 (Android 4.1 Jelly Bean)
- **Target SDK**: API 33+ (recommended)
- **Language**: Java/Kotlin

## Contributing

We welcome contributions! Please feel free to submit issues, fork the repository, and create pull requests.

### Development Setup

1. Clone the repository
```bash
git clone https://github.com/sudhakar-r08/circular_seekbar.git
```

2. Open in Android Studio
3. Build and run the sample app

## Changelog

### Version 1.0.0
- Initial release
- Basic circular SeekBar functionality
- XML attribute support
- Touch interaction
- Progress animation

## License

```
MIT License

Copyright (c) 2025 Sudhakar Raju

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Support

- 📧 **Email**: [sudhakar.r08@gmail.com](sudhakar.r08@gmail.com)
- 🐛 **Issues**: [GitHub Issues](https://github.com/sudhakar-r08/circular_seekbar/issues)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/sudhakar-r08/circular_seekbar/discussions)

## Acknowledgments

- Thanks to the Android community for inspiration and feedback
- Built with ❤️ for Android developers

---

⭐ **If this library helped you, please give it a star!** ⭐
