package pt.isel.ls.houses.data.db

import kotlinx.datetime.LocalDateTime
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.repository.BookingRepository

class BookingDataPostgresql(val dataSource: PGSimpleDataSource): BookingRepository {
    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Booking {
        TODO("Not yet implemented")
    }

    override fun getBooking(bid: Int): Booking? {
        TODO("Not yet implemented")
    }

    override fun getBookings(
        hid: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Booking> {
        TODO("Not yet implemented")
    }

    override fun getAvailableHouses(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<House> {
        TODO("Not yet implemented")
    }
}