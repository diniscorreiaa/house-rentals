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
    private val location = LocationDataPostgresql(dataSource)
    private val house = HouseDataPostgresql(dataSource)
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

    override fun createHouse(
        title: String,
        location: Int,
        areaSqMt: Double,
        pricePerNight: Double,
        description: String
    ): House = house.createHouse(title, location, areaSqMt, pricePerNight, description)

    override fun getHouse(hid: Int): House? = house.getHouse(hid)

    override fun getHouses(limit: Int?, skip: Int?): List<House> = house.getHouses(limit, skip)

    override fun createLocation(
        name: String,
        type: LocationType,
        parentId: Int?
    ): Location = location.createLocation(name, type, parentId)

    override fun getLocation(lid: Int): Location? = location.getLocation(lid)

    override fun getChildLocations(lid: Int, limit: Int?, skip: Int?): List<Location> =
        location.getChildLocations(lid, limit, skip)

    override fun getHierarchy(lid: Int): List<Location> = location.getHierarchy(lid)
}