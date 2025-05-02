package org.baghdad.utils

import java.security.MessageDigest

fun String.md5WithSalt(): String =
    MessageDigest
        .getInstance("MD5")
        .digest(this.toByteArray() + SALT.toByteArray())
        .joinToString("") { "%02x".format(it) }

private const val SALT = "Baghdad"
