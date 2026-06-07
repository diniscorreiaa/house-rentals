package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.exceptions.InvalidLogin
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.exceptions.UserEmailAlreadyExists
import pt.isel.ls.houses.repository.BookingRepository
import pt.isel.ls.houses.repository.UserRepository

class UserServices(
    private val userData: UserRepository,
    private val bookingData: BookingRepository
) {
    fun createUser(name: String, email: String, password: String): User {
        if (userData.getUserByEmail(email) != null) throw UserEmailAlreadyExists()
        if (password.isEmpty()) throw InvalidLogin()

        val user = userData.createUser(name, email, password)
        return user
    }

    fun getUser(uid: Int): User {
        val user = userData.getUser(uid) ?: throw UserDoesNotExist()
        return user
    }

    fun userBookings(uid: Int): List<Booking>{
        if(userData.getUser(uid) == null) throw UserDoesNotExist()
        return bookingData.userBookings(uid)
    }

    fun login(username: String, password: String): User {
        return userData.login(username, password) ?: throw InvalidLogin() //new Exception
    }
}

