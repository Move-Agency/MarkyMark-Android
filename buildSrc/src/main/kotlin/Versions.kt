/*
 * Copyright © 2022 M2mobi
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

import org.gradle.api.JavaVersion.VERSION_11

object Versions {

    const val VERSION_CODE = 3_000

    const val KOTLIN = "1.7.10"
    val JVM = VERSION_11
    const val MIN_SDK = 24
    const val TARGET_SDK = 33

    const val GRADLE_PLUGIN = "7.3.0"

    const val COMPOSE = "1.3.0-beta03"
    const val COMPOSE_ACTIVITY = "1.6.0"
    const val ACCOMPANIST = "0.26.4-beta"
    const val KOTLIN_COMPILER_EXTENSION = "1.3.1"
    const val COIL_KT = "2.2.1"
    const val MATERIAL = "1.0.0-beta03"

    const val FLEXMARK = "0.64.0"
}
