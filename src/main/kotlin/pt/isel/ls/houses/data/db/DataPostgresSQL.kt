package pt.isel.ls.houses.data.db

import kotlinx.datetime.LocalDate
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.repository.DataRepository
import kotlin.uuid.Uuid

class DataPostgresSQL : DataRepository {

    private val dataSource = PGSimpleDataSource().apply { setUrl(System.getenv("JDBC_DATABASE_URL")) }

    override val user = UserDataPostgresql(dataSource)
    override val location = LocationDataPostgresql(dataSource)
    override val house = HouseDataPostgresql(dataSource)
    private val booking = BookingDataPostgresql(dataSource)

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ) = booking.createBooking(hid, userId, startDate, endDate)

    override fun getBooking(bid: Int): Booking? = booking.getBooking(bid)

    override fun getBookings(
        hid: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?,
    ): List<Booking> = booking.getBookings(hid, startDate, endDate, limit, skip)

    override fun getAvailableHouses(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?,
    ): List<House> = booking.getAvailableHouses(startDate, endDate, limit, skip)
}