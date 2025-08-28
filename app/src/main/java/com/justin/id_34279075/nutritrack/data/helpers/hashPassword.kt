package com.justin.id_34279075.nutritrack.data.helpers

import java.security.MessageDigest

// Hashes password using the SHA-256 algorithm.
fun hashPassword(password: String): String {
    val bytes = password.toByteArray()
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val digest = messageDigest.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}