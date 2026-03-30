package pt.isel.ls.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import pt.isel.ls.houses.domain.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserTests {
    private val validUid = 1
    private val validName = "DummyName"
    private val validEmail = "dummyemail@gmail.com"
    @OptIn(ExperimentalUuidApi::class)
    private val validToken = Uuid.random()

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `User is created successfully`() {
        val user = User(
            uid = validUid,
            name = validName,
            email = validEmail,
            token = validToken
        )

        assertEquals(validUid, user.uid)
        assertEquals(validName, user.name)
        assertEquals(validEmail, user.email)
        assertEquals(validToken, user.token)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `Two users with different UID must be different`() {
        val token = Uuid.random()
        val user1 = User(uid = 1, name = "Alice", email = "alice@example.com", token = token)
        val user2 = User(uid = 2, name = "Alice", email = "alice2@example.com", token = token)
        assertNotEquals(user1, user2)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `Two users with different names and emails must be different`() {
        val token = Uuid.random()
        val user1 = User(uid = 1, name = "Alice", email = "alice@example.com", token = token)
        val user2 = User(uid = 1, name = "Bob", email = "bob@example.com", token = token)
        assertNotEquals(user1, user2)
    }
}