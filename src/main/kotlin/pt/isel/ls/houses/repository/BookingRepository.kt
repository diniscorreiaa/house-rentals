package pt.isel.ls.houses.repository

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House

interface BookingRepository {
    fun createBooking(hid: Int, userId: Int, startDate: LocalDate, endDate: LocalDate): Booking
    fun getBooking(bid: Int): Booking?
    fun getBookings(hid: Int, startDate: LocalDate, endDate: LocalDate, limit: Int?, skip: Int?): List<Booking>
    fun getAvailableHouses(startDate: LocalDate, endDate: LocalDate, limit: Int?, skip: Int?): List<House>
    fun usersBooking(uid: Int): List<Booking>
    fun deleteBooking(bid: Int): Boolean
    fun updateBooking(bid: Int, startDate: LocalDate, endDate: LocalDate): Booking?
}