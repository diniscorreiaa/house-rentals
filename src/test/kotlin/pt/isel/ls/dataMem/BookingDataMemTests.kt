package pt.isel.ls.dataMem

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.data.memory.BookingDataMem
import pt.isel.ls.houses.data.memory.HouseDataMem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BookingDataMemTests {
    @Test
    fun `create booking is successful`() {
        val houseRepo = HouseDataMem()
        val bookingRepo = BookingDataMem(houseRepo)
        val house = houseRepo.createHouse("House 1", 1, 101.0, 51.0, "Description 1", 1)

        val firstBooking = bookingRepo.createBooking(house.hid, 42, LocalDate(2025, 1, 1), LocalDate(2025, 1, 5))
        val secondBooking = bookingRepo.createBooking(house.hid, 43, LocalDate(2025, 1, 6), LocalDate(2025, 1, 8))

        assertEquals(1, firstBooking.bid)
        assertEquals(2, secondBooking.bid)
        assertEquals(house.hid, firstBooking.houseId)
        assertEquals(42, firstBooking.userId)
        assertEquals(firstBooking, bookingRepo.getBooking(firstBooking.bid))
        assertNotNull(bookingRepo.getBooking(secondBooking.bid))
    }

    @Test
    fun `getBooking returns null when bid is non existing`() {
        val houseRepo = HouseDataMem()
        val bookingRepo = BookingDataMem(houseRepo)

        assertNull(bookingRepo.getBooking(10))
    }

    @Test
    fun `getBookings with skip and limit`() {
        val houseRepo = HouseDataMem()
        val bookingRepo = BookingDataMem(houseRepo)
        val house = houseRepo.createHouse("House 1", 1, 101.0, 51.0, "Description 1", 1)

        val booking1 = bookingRepo.createBooking(house.hid, 1, LocalDate(2025, 1, 1), LocalDate(2025, 1, 2))
        val booking2 = bookingRepo.createBooking(house.hid, 2, LocalDate(2025, 1, 3), LocalDate(2025, 1, 4))
        val booking3 = bookingRepo.createBooking(house.hid, 3, LocalDate(2025, 1, 5), LocalDate(2025, 1, 6))

        val middle =
            bookingRepo.getBookings(house.hid, LocalDate(2025, 1, 1), LocalDate(2025, 1, 31), limit = 1, skip = 1)
        val tail =
            bookingRepo.getBookings(house.hid, LocalDate(2025, 1, 1), LocalDate(2025, 1, 31), limit = 1, skip = 2)

        assertEquals(listOf(booking2), middle)
        assertEquals(listOf(booking3), tail)
    }

    @Test
    fun `getAvailableHouses filters out overlapping bookings`() {
        val houseRepo = HouseDataMem()
        val bookingRepo = BookingDataMem(houseRepo)
        val house1 = houseRepo.createHouse("House 1", 1, 101.0, 51.0, "Description 1", 1)
        val house2 = houseRepo.createHouse("House 2", 2, 102.0, 52.0, "Description 2", 2)
        val house3 = houseRepo.createHouse("House 3", 3, 103.0, 53.0, "Description 3", 3)

        bookingRepo.createBooking(house1.hid, 1, LocalDate(2025, 3, 10), LocalDate(2025, 3, 12))
        bookingRepo.createBooking(house2.hid, 2, LocalDate(2025, 4, 1), LocalDate(2025, 4, 3))

        val available =
            bookingRepo.getAvailableHouses(LocalDate(2025, 3, 11), LocalDate(2025, 3, 15), limit = null, skip = null)
        val paged = bookingRepo.getAvailableHouses(LocalDate(2025, 3, 11), LocalDate(2025, 3, 15), limit = 1, skip = 1)

        assertEquals(listOf(house2, house3), available)
        assertEquals(listOf(house3), paged)
    }
}