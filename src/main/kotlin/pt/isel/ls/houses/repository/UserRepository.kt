package pt.isel.ls.houses.repository

import pt.isel.ls.houses.domain.User
import kotlin.uuid.Uuid

interface UserRepository {
    fun createUser(name: String, email: String, password: String): User
    fun getUser(uid: Int): User?
    fun getUserByEmail(email: String): User?
    fun getUserByToken(token: Uuid): User?
}