// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-file
@file:OptIn(ExperimentalUuidApi::class)

package pt.isel.ls.services

import org.junit.Assert.assertNotEquals
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.exceptions.UserEmailAlreadyExists
import pt.isel.ls.houses.services.UserServices
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

class UserServicesTests {
    private fun newServices() = UserServices(DataMem())

    @Test
    fun `createUser returns user with correct name and email`() {
        val services = newServices()

        val createdUser = services.createUser(name = "Dummy", email = "dummy@email.com")

        assertEquals(1, createdUser.uid)
        assertEquals("Dummy", createdUser.name)
        assertEquals("dummy@email.com", createdUser.email)
        assertEquals(createdUser, services.getUser(createdUser.uid))
    }

    @Test
    fun `createUser assigns UID starting at 1`() {
        val services = newServices()

        val user = services.createUser(name = "Dummy", email = "dummy@email.com")

        assertEquals(1, user.uid)
    }

    @Test
    fun `createUser generates unique tokens for each user`() {
        val services = newServices()

        val user1 = services.createUser(name = "Dummy", email = "dummy@email.com")
        val user2 = services.createUser(name = "Dummy2", email = "dummy2@email.com")

        assertNotEquals(user1.token, user2.token)
    }

    @Test
    fun `createUser with duplicate email throws UserEmailAlreadyExists Exception`() {
        val services = newServices()
        services.createUser(name = "Dummy", email = "dummy@email.com")

        assertFailsWith<UserEmailAlreadyExists> {
            services.createUser(name = "Dummy2", email = "dummy@email.com")
        }
    }

    @Test
    fun `getUser for missing id throws UserDoesNotExist Exception`() {
        val services = newServices()

        assertFailsWith<UserDoesNotExist> {
            services.getUser(uid = 999)
        }
    }

    @Test
    fun `getUser returns correct user among multiple users`() {
        val services = newServices()
        services.createUser(name = "Dummy", email = "dummy@email.com")

        val dummy2 = services.createUser(name = "Dummy2", email = "dummy2@email.com")

        services.createUser(name = "Dummy3", email = "dummy3@email.com")

        val fetched = services.getUser(dummy2.uid)

        assertEquals("Dummy2", fetched.name)
        assertEquals("dummy2@email.com", fetched.email)
    }
}