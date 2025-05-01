package data.repositories.authentication

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.repositories.authentication.TokenProviderImpl
import org.baghdad.logic.repositories.TokenProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TokenProviderImplTest {
    lateinit var tokenProvider: TokenProvider
    @BeforeEach
    fun setUp() {
        tokenProvider = TokenProviderImpl()
    }

    @Test
    fun `should return token from user`() {
        val result = tokenProvider.generateToken()
        assertThat(result).isNotNull()
    }
}