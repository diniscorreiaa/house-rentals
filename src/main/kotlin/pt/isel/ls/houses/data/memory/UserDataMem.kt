package pt.isel.ls.houses.data.memory

import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.domain.HouseOwner
import pt.isel.ls.houses.repository.HouseRepository
import pt.isel.ls.houses.repository.UserRepository
import kotlin.uuid.Uuid


class UserDataMem(private val houseData: HouseRepository) : UserRepository {
    private val users = mutableListOf<User>()
    private var id = 1

    override fun createUser(name: String, email: String): User {
        val user = User(id++, name, email, Uuid.random())
        users.add(user)
        return user
    }

    override fun getUser(uid: Int): User? = users.find { it.uid == uid }


    override fun getUserByEmail(email: String): User? = users.find { it.email == email }

    override fun getUserByToken(token: Uuid): User? = users.find { it.token == token }

    override fun getHouseOwners(): List<HouseOwner> {
        val housesByOwner = houseData.getHouses(limit = null, skip = null).groupingBy { it.owner }.eachCount()

        return users
            .filter { housesByOwner.containsKey(it.uid) }
            .map {
                HouseOwner(
                    it.name,
                    it.email,
                    housesByOwner[it.uid] ?: 0
                )
            }
    }
}