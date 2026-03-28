package pt.isel.ls.houses.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.BookingDoesNotExist
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.HouseIsNotAvailable
import pt.isel.ls.houses.exceptions.InvalidDate
import pt.isel.ls.houses.repository.BookingRepository
import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.repository.HouseRepository

class BookingServices(
    private val data: DataRepository
) {
    fun createBooking(hid: Int, uid: Int, startDate: LocalDateTime, endDate: LocalDateTime): Booking {
        if (startDate >= endDate) throw InvalidDate()
        if (data.getHouse(hid) == null) throw HouseDoesNotExist()

        val overlapBooking = data.getBookings(hid, startDate, endDate)

        if (overlapBooking.isNotEmpty()) throw HouseIsNotAvailable()

        return data.createBooking(hid, uid, startDate, endDate)
    }

    fun getBooking(bid: Int) = data.getBooking(bid) ?: throw BookingDoesNotExist()

    fun getBookings(hid: Int, startDate: LocalDateTime, endDate: LocalDateTime): List<Booking> {
        if (startDate >= endDate) throw InvalidDate()

        if (data.getHouse(hid) == null) throw HouseDoesNotExist()

        return data.getBookings(hid, startDate, endDate)
    }

    fun getAvailableHouses(startDate: LocalDateTime, endDate: LocalDateTime): List<House> {
        if (startDate >= endDate) throw InvalidDate()
        return data.getAvailableHouses(startDate, endDate)
    }
}