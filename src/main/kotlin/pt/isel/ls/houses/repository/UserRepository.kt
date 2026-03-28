package pt.isel.ls.houses.repository

import pt.isel.ls.houses.domain.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserRepository {
    @OptIn(ExperimentalUuidApi::class)
    fun createUser(name: String, email: String): User
    fun getUser(uid: Int): User?
    fun getUserByEmail(email: String): User?
    @OptIn(ExperimentalUuidApi::class)
    fun getUserByToken(token: Uuid) : User?
}