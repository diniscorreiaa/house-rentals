package pt.isel.ls.houses.data.memory

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.repository.DataRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DataMem: DataRepository {
    private val user = UserDataMem()
    private val location = LocationDataMem()
    private val house = HouseDataMem()
    private val booking = BookingDataMem(house)

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Booking = booking.createBooking(hid, userId, startDate, endDate)

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

    override fun getChildLocations(lid: Int, limit: Int?, skip: Int?): List<Location> = location.getChildLocations(lid, limit, skip)

    override fun getHierarchy(lid: Int): List<Location> = location.getHierarchy(lid)

    override fun createUser(name: String, email: String): User = user.createUser(name, email)

    override fun getUser(uid: Int): User? = user.getUser(uid)

    override fun getUserByEmail(email: String): User? = user.getUserByEmail(email)

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserByToken(token: Uuid): User? = user.getUserByToken(token)

}