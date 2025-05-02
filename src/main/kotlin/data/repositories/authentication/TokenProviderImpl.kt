package org.baghdad.data.repositories.authentication

import org.baghdad.logic.repositories.TokenProvider
import java.util.UUID

class TokenProviderImpl: TokenProvider {
    override fun generateToken(): String {
        return UUID.randomUUID().toString()
    }
}