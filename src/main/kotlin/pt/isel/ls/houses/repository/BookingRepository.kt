package pt.isel.ls.houses.repository

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House

interface BookingRepository {
    fun createBooking(hid: Int, userId: Int, startDate: LocalDateTime, endDate: LocalDateTime): Booking
    fun getBooking(bid: Int): Booking?
    fun getBookings(hid: Int, startDate: LocalDateTime, endDate: LocalDateTime): List<Booking>
    fun getAvailableHouses(startDate: LocalDateTime, endDate: LocalDateTime): List<House>
}