package pt.isel.ls.houses.data.db

import kotlinx.datetime.LocalDate
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.repository.DataRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DataPostgresSQL: DataRepository {

    private val dataSource = PGSimpleDataSource().apply { setUrl(System.getenv("JDBC_DATABASE_URL")) }

    private val user = UserDataPostgresql(dataSource)
    private val location = LocationDataPostgresql(dataSource)
    private val house = HouseDataPostgresql(dataSource)
    private val booking = BookingDataPostgresql(dataSource, house)

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ) = TODO()

    override fun getBooking(bid: Int): Booking? {
        TODO("Not yet implemented")
    }

    override fun getBookings(
        hid: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Booking> {
        TODO("Not yet implemented")
    }

    override fun getAvailableHouses(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<House> {
        TODO("Not yet implemented")
    }

    override fun createHouse(
        title: String,
        location: Int,
        areaSqMt: Double,
        pricePerNight: Double,
        description: String
    ): House = house.createHouse(title, location, areaSqMt, pricePerNight, description)

    override fun getHouse(hid: Int): House? = house.getHouse(hid)

    override fun getHouses(): List<House> = house.getHouses()

    override fun createLocation(
        name: String,
        type: LocationType,
        parentId: Int?
    ): Location {
        TODO("Not yet implemented")
    }

    override fun getLocation(lid: Int): Location? {
        TODO("Not yet implemented")
    }

    override fun getChildLocations(lid: Int): List<Location> {
        TODO("Not yet implemented")
    }

    override fun getHierarchy(lid: Int): List<Location> {
        TODO("Not yet implemented")
    }

    override fun createUser(name: String, email: String) = user.createUser(name, email)

    override fun getUser(uid: Int): User? = getUser(uid)

    override fun getUserByEmail(email: String): User? = getUserByEmail(email)

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserByToken(token: Uuid): User? = getUserByToken(token)

}