package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.exceptions.UserEmailAlreadyExists
import pt.isel.ls.houses.repository.UserRepository

class UserServices(
    private val userData: UserRepository
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

    fun userBookings(uid: Int): List<Booking>{
        if(data.getUser(uid) == null) throw UserDoesNotExist()
        return data.userBookings(uid)
    }
}

