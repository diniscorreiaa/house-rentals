// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-file
@file:OptIn(ExperimentalUuidApi::class)

package pt.isel.ls.dataMem

import pt.isel.ls.houses.data.memory.HouseDataMem
import pt.isel.ls.houses.data.memory.UserDataMem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.uuid.ExperimentalUuidApi

class UserDataMemTests {
    private fun newRepo(): Pair<UserDataMem, HouseDataMem> {
        val houseRepo = HouseDataMem()
        return Pair(UserDataMem(houseRepo), houseRepo)
    }

    @Test
    fun `createUser assigns sequential UIDS`() {
        val (repo, _) = newRepo()

        val firstUser = repo.createUser(name = "Dummy", email = "dummy@email.com")
        val secondUser = repo.createUser(name = "Dummy2", email = "dummy2@email.com")

        assertEquals(1, firstUser.uid)
        assertEquals(2, secondUser.uid)
    }

    @Test
    fun `getUser returns created user successfully`() {
        val (repo, _) = newRepo()
        val created = repo.createUser(name = "Dummy", email = "dummy@email.com")

        val fetched = repo.getUser(created.uid)

        assertNotNull(fetched)
        assertEquals(created, fetched)
    }

    @Test
    fun `getUserByEmail returns created user successfully`() {
        val (repo, _) = newRepo()
        val created = repo.createUser(name = "Dummy", email = "dummy@email.com")

        val fetched = repo.getUserByEmail(created.email)

        assertNotNull(fetched)
        assertEquals(created, fetched)
    }

    @Test
    fun `getUserByToken returns created user successfully`() {
        val (repo, _) = newRepo()
        val created = repo.createUser(name = "Dummy", email = "dummy@email.com")

        val fetched = repo.getUserByToken(created.token)

        assertNotNull(fetched)
        assertEquals(created, fetched)
    }

    @Test
    fun `createUser generates unique tokens`() {
        val (repo, _) = newRepo()

        val first = repo.createUser(name = "Dummy", email = "dummy@email.com")
        val second = repo.createUser(name = "Dummy2", email = "dummy2@email.com")

        assertNotEquals(first.token, second.token)
    }

    @Test
    fun `getUsersWithHouses returns only owners with total number of houses`() {
        val (repo, houseRepo) = newRepo()

        val user1 = repo.createUser(name = "Dummy", email = "dummy@email.com")
        val user2 = repo.createUser(name = "Dummy2", email = "dummy2@email.com")
        repo.createUser(name = "Dummy3", email = "dummy3@email.com")

        houseRepo.createHouse("House 1", 1, 10.0, 20.0, "beautiful", user1.uid)
        houseRepo.createHouse("House 2", 1, 10.0, 20.0, "pretty", user1.uid)
        houseRepo.createHouse("House 3", 1, 10.0, 20.0, "beautiful", user2.uid)

        val owners = repo.getHouseOwners()

        assertEquals(2, owners.size)
        assertEquals("Dummy", owners[0].name)
        assertEquals("dummy@email.com", owners[0].email)
        assertEquals(2, owners[0].totalHouses)

        assertEquals("Dummy2", owners[1].name)
        assertEquals("dummy2@email.com", owners[1].email)
        assertEquals(1, owners[1].totalHouses)
    }
}