package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.exceptions.UserEmailAlreadyExists
import pt.isel.ls.houses.repository.UserRepository

class UserServices(
    private val usersRepository: UserRepository
) {
    fun createUser(name: String, email: String): User {
        if(usersRepository.getUserByEmail(email) != null) {
            throw UserEmailAlreadyExists()
        }
        val user = usersRepository.createUser(name, email)
        return user
    }

    fun getUser(uid: Int): User {
        val user = usersRepository.getUser(uid) ?: throw UserDoesNotExist()
        return user
    }
}

