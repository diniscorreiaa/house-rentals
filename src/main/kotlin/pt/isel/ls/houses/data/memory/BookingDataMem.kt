package pt.isel.ls.houses.data.memory

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.BookingDoesNotExist
import pt.isel.ls.houses.repository.BookingRepository
import java.time.YearMonth

class BookingDataMem(private val houseDataMem: HouseDataMem) : BookingRepository {
    private val bookings = mutableListOf<Booking>()
    private var id = 1

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Booking {
        val booking = Booking(id++, startDate, endDate, userId, hid)
        bookings.add(booking)
        return booking
    }

    override fun getBooking(bid: Int): Booking? = bookings.find { it.bid == bid }

    override fun getBookings(
        hid: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?,
    ): List<Booking> {
        val bookings = bookings.filter { it.houseId == hid && (it.startDate >= startDate && it.endDate <= endDate) }
        if (limit == null || bookings.isEmpty()) return bookings
        else {
            var lastHouseIdx = (skip ?: 0) + limit
            if (lastHouseIdx > bookings.size) lastHouseIdx = bookings.size
            return bookings.subList(skip ?: 0, lastHouseIdx)
        }
    }

    override fun getAvailableHouses(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?
    ): List<House> {
         val occupiedHIds = bookings.filter { it.startDate <= endDate && it.endDate >= startDate}.map { it.houseId }
         val houses = houseDataMem.getHouses(null, null,null, null).filter { it.hid !in occupiedHIds }
         if (limit != null) return houses.subList(skip ?: 0, (skip ?: 0) + limit)
         return houses
     }

    override fun userBookings(uid: Int): List<Booking> {
        return bookings.filter { it.userId == uid }
    }

    override fun deleteBooking(bid: Int) {
        val booking = bookings.find { it.bid == bid } ?: throw BookingDoesNotExist()
        bookings.remove(booking)
    }

    override fun updateBooking(bid: Int, startDate: LocalDate, endDate: LocalDate): Booking {
        val booking = bookings.find { it.bid == bid } ?: throw BookingDoesNotExist()
        val updatedBooking = booking.copy(startDate = startDate, endDate = endDate)
        bookings[bookings.indexOf(booking)] = updatedBooking
        return updatedBooking
    }

    override fun getAvailableDays(hid: Int, month: Int, year: Int, limit: Int?, skip: Int?): List<Int> {
        val monthLength = YearMonth.of(year, month).lengthOfMonth() //to get number of days
        val monthStart = LocalDate(year, month, 1)
        val monthEnd = LocalDate(year, month, monthLength)

        val unavailableDays = mutableSetOf<Int>()

        bookings
            .filter { it.houseId == hid && it.startDate <= monthEnd && it.endDate >= monthStart }
            .forEach { booking ->
                val start = if (booking.startDate > monthStart) booking.startDate else monthStart
                val end = if (booking.endDate < monthEnd) booking.endDate else monthEnd

                for (day in start.dayOfMonth..end.dayOfMonth) {
                    unavailableDays.add(day)
                }
            }

        val availableDays = (1..monthLength).filter { it !in unavailableDays }
        return if (limit == null || availableDays.isEmpty()) availableDays
        else {
            var lastDay = (skip ?: 0) + limit
            if (lastDay > monthLength) lastDay = monthLength
            availableDays.subList(skip ?: 0, lastDay)
        }
    }
}
