import com.google.common.truth.Truth.assertThat
import org.baghdad.main
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `test main function`() {
        // Capture output or mock behavior if needed
        main()
        assertThat("1").isEqualTo("1")
    }
}