package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.exceptions.UserEmailAlreadyExists
import pt.isel.ls.houses.repository.DataRepository

class UserServices(
    private val data: DataRepository
) {
    fun createUser(name: String, email: String): User {
        if (data.getUserByEmail(email) != null) throw UserEmailAlreadyExists()

        val user = data.createUser(name, email)
        return user
    }

    fun getUser(uid: Int): User {
        val user = data.getUser(uid) ?: throw UserDoesNotExist()
        return user
    }
}

