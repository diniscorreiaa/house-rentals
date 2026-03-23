package pt.isel.ls.houses.data.memory

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.repository.DataRepository

class DataMem: DataRepository {
    val user = UserDataMem()
    val location = LocationDataMem()
    val house = HouseDataMem()
    val booking = BookingDataMem(house)

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Booking = booking.createBooking(hid, userId, startDate, endDate)

    override fun getBooking(bid: Int): Booking? = booking.getBooking(bid)

    override fun getBookings(
        hid: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Booking> = booking.getBookings(hid, startDate, endDate)

    override fun getAvailableHouses(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<House> = booking.getAvailableHouses(startDate, endDate)

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
    ): Location = location.createLocation(name, type, parentId)

    override fun getLocation(lid: Int): Location? = location.getLocation(lid)

    override fun getChildLocations(lid: Int): List<Location> = location.getChildLocations(lid)

    override fun getHierarchy(lid: Int): List<Location> = location.getHierarchy(lid)

    override fun createUser(name: String, email: String): User = user.createUser(name, email)

    override fun getUser(uid: Int): User? = user.getUser(uid)

    override fun getUserByEmail(email: String): User? = user.getUserByEmail(email)

}