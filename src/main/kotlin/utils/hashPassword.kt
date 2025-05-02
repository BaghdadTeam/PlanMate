package org.baghdad.utils

import java.security.MessageDigest


fun String.md5WithSalt() = MessageDigest.getInstance("MD5")
    .digest(this.toByteArray() + SALT.toByteArray())
    .joinToString("") { "%02x".format(it) }

const val SALT = "Baghdad"
