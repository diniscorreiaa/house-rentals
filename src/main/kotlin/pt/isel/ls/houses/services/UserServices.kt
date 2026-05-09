package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.domain.HouseOwner
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.exceptions.UserEmailAlreadyExists
import pt.isel.ls.houses.repository.BookingRepository
import pt.isel.ls.houses.repository.UserRepository

class UserServices(
    private val userData: UserRepository,
    private val bookingData: BookingRepository
) {
    fun createUser(name: String, email: String): User {
        if (userData.getUserByEmail(email) != null) throw UserEmailAlreadyExists()

        val user = userData.createUser(name, email)
        return user
    }

    fun getUser(uid: Int): User {
        val user = userData.getUser(uid) ?: throw UserDoesNotExist()
        return user
    }

    fun userBookings(uid: Int): List<Booking> {
        if (userData.getUser(uid) == null) throw UserDoesNotExist()
        return bookingData.userBookings(uid)
    }

    fun getUsersWithHouses(): List<HouseOwner> = userData.getHouseOwners()
}
