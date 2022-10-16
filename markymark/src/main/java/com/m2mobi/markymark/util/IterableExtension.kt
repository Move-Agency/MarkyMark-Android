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

package com.m2mobi.markymark.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Map the iterable items using [mapper] on the [Dispatchers.Default] dispatcher asynchronously and await all.
 * This function respects the order of the iterable.
 */
internal suspend fun <T, R> Iterable<T>.mapAsync(mapper: suspend (T) -> R): List<R> {
    return withContext(Dispatchers.Default) {
        map {
            async { mapper(it) }
        }.awaitAll()
    }
}

/**
 * Map the iterable items using [mapper] on the [Dispatchers.Default] dispatcher asynchronously and await all.
 * This function respects the order of the iterable.
 */
internal suspend fun <T, R> Iterable<T>.mapAsyncIndexed(mapper: suspend (Int, T) -> R): List<R> {
    return withContext(Dispatchers.Default) {
        mapIndexed { index, item ->
            async { mapper(index, item) }
        }.awaitAll()
    }
}
