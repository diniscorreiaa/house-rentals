package pt.isel.ls.houses.services

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.BookingDoesNotExist
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.HouseIsNotAvailable
import pt.isel.ls.houses.exceptions.InvalidDate
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.repository.UserRepository
import kotlin.uuid.Uuid

class BookingServices(
    private val userData: UserRepository,
    private val data: DataRepository
) {
    fun createBooking(hid: Int, startDate: LocalDate, endDate: LocalDate, token: Uuid): Booking {
        val user = userData.getUserByToken(token) ?: throw Unauthorized()

        if (startDate >= endDate) throw InvalidDate()
        if (data.getHouse(hid) == null) throw HouseDoesNotExist()

        val overlapBooking = data.getBookings(hid, startDate, endDate, null, null)

        if (overlapBooking.isNotEmpty()) throw HouseIsNotAvailable()

        return data.createBooking(hid, user.uid, startDate, endDate)
    }

    fun getBooking(bid: Int) = data.getBooking(bid) ?: throw BookingDoesNotExist()

    fun getBookings(hid: Int, startDate: LocalDate, endDate: LocalDate, limit: Int?, skip: Int?): List<Booking> {
        if (startDate >= endDate) throw InvalidDate()

        if (data.getHouse(hid) == null) throw HouseDoesNotExist()

        return data.getBookings(hid, startDate, endDate, limit, skip)
    }

    fun getAvailableHouses(startDate: LocalDate, endDate: LocalDate, limit: Int?, skip: Int?): List<House> {
        if (startDate >= endDate) throw InvalidDate()
        return data.getAvailableHouses(startDate, endDate, limit, skip)
    }
}