import com.tjcoding.funtimer.data.local.type_converter.Converters
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ConvertersTest {

    @Test
    fun `fromLocalDateTime should convert LocalDateTime to Long`() {
        // Arrange
        val converters = Converters()
        val localDateTime = LocalDateTime.of(2022, 1, 14, 12, 0, 0) // Example LocalDateTime

        // Act
        val result = converters.fromLocalDateTime(localDateTime)

        // Assert
        assertEquals(localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(), result)
    }

    @Test
    fun `toLocalDateTime should convert Long to LocalDateTime`() {
        // Arrange
        val converters = Converters()
        val unixTime = 1642159200L // Example Unix time

        // Act
        val result = converters.toLocalDateTime(unixTime)

        // Assert
        assertEquals(
            Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDateTime(),
            result
        )
    }

    // Add more test cases as needed

}
