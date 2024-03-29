import com.tjcoding.funtimer.utility.Util
import com.tjcoding.funtimer.utility.Util.addInOrder
import com.tjcoding.funtimer.utility.Util.formatTo24HourAndMinute
import com.tjcoding.funtimer.utility.Util.formatToTimeRemaining
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.time.LocalDateTime

class UtilTest {

    @Test
    fun addInOrder_shouldAddAndSortNumbersInOrder() {
        val numbers = mutableListOf(3, 1, 4, 2)
        numbers.addInOrder(5)
        assertEquals(listOf(1, 2, 3, 4, 5), numbers)
    }

    @Test
    fun `formatTo24HourAndMinute should format LocalDateTime to HH-mm format`() {
        // Arrange
        val inputDateTime = LocalDateTime.of(2024, 1, 14, 15, 30) // Example date and time

        // Act
        val formattedTime = inputDateTime.formatTo24HourAndMinute()

        // Assert
        assertEquals("15:30", formattedTime)
    }

    @Test
    fun `formatToTimeRemaining should format LocalDateTime to mm-ss format`() {
        // Arrange
        val currentTime = LocalDateTime.now()
        val futureTime = currentTime.plusMinutes(5) // Example: 5 minutes into the future

        // Act
        val formattedTimeRemaining = futureTime.formatToTimeRemaining()

        // Assert
        assertEquals("05:00", formattedTimeRemaining)
    }

    @Test
    fun `formatToTimeRemaining should handle negative duration`() {
        // Arrange
        val currentTime = LocalDateTime.now()
        val pastTime = currentTime.minusMinutes(5) // Example: 5 minutes in the past

        // Act
        val formattedTimeRemaining = pastTime.formatToTimeRemaining()

        // Assert
        assertEquals("00:00", formattedTimeRemaining)
    }

    @Test
    fun shouldRetry_shouldReturnTrueForIOExceptionAndWithinMaxAttempts() = runBlocking {
        val ioException = IOException("Simulated IOException")
        val shouldRetry = Util.shouldRetry(ioException, 2)
        assertTrue(shouldRetry)
    }

    @Test
    fun shouldRetry_shouldReturnFalseForIOExceptionAfterMaxAttempts() = runBlocking {
        val ioException = IOException("Simulated IOException")
        val shouldRetry = Util.shouldRetry(ioException, 4)
        assertFalse(shouldRetry)
    }

    @Test
    fun shouldRetry_shouldReturnFalseForNonIOException() = runBlocking {
        val exception = RuntimeException("Simulated exception")
        val shouldRetry = Util.shouldRetry(exception, 2)
        assertFalse(shouldRetry)
    }

}
