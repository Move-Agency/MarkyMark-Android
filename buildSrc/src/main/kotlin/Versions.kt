/*
 * Copyright © 2024 Move
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import org.gradle.api.JavaVersion

object Versions {

    const val VERSION_CODE = 3_001
    const val VERSION_NAME = "3.0.0-alpha02"

    const val NAMESPACE = "com.moveagency.markymark"

    const val KOTLIN = "1.9.22"
    val JVM = JavaVersion.VERSION_17
    const val MIN_SDK = 24
    const val TARGET_SDK = 34

    const val DETEKT = "1.21.0"

    const val GRADLE_PLUGIN = "8.1.4"

    const val IMMUTABLE = "0.3.5"

    const val COMPOSE_ACTIVITY = "1.8.2"
    const val ACCOMPANIST = "0.32.0"
    const val KOTLIN_COMPILER_EXTENSION = "1.5.8"
    const val COIL_KT = "2.5.0"

    const val FLEXMARK = "0.64.8"
}
