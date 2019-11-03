package com.content.mercy.util

import java.nio.ByteBuffer

/**
 * Created by rapsealk on 2019-11-02..
 */
fun ByteBuffer.toByteArray(): ByteArray {
    rewind()    // Rewind buffet position to 0
    val data = ByteArray(remaining())
    get(data)   // Copy bytebuffer to byte array
    return data
}