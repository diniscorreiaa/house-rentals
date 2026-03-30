package pt.isel.ls.domain

import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pt.isel.ls.houses.domain.Booking

class BookingTests {
    private val validBid = 1
    private val validStart = LocalDateTime(2025, 6, 1, 14, 0)
    private val validEnd = LocalDateTime(2025, 6, 7, 11, 0)
    private val validUserId = 1
    private val validHouseId = 3

    private fun booking(
        bid: Int = validBid,
        startDate: LocalDateTime = validStart,
        endDate: LocalDateTime = validEnd,
        userId: Int = validUserId,
        houseId: Int = validHouseId
    ) = Booking(bid, startDate, endDate, userId, houseId)

    @Test
    fun `Booking is created successfully`() {
        val booking = booking()
        assertEquals(validBid, booking.bid)
        assertEquals(validStart, booking.startDate)
        assertEquals(validEnd, booking.endDate)
        assertEquals(validUserId, booking.userId)
        assertEquals(validHouseId, booking.houseId)
    }

    @Test
    fun `create booking where endDate is after startDate should succeed`() {
        val b = booking(
            startDate = LocalDateTime(2025, 1, 1, 10, 0),
            endDate   = LocalDateTime(2025, 1, 10, 10, 0)
        )
        assertTrue(b.endDate > b.startDate)
    }

    @Test
    fun `two bookings with same data should be the same`() {
        assertEquals(booking(), booking())
    }

    @Test
    fun `two bookings with different bid should not be the same`() {
        assertNotEquals(booking(bid = 1), booking(bid = 2))
    }
}