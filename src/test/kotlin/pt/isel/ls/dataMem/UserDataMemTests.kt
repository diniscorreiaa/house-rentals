// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-file
@file:OptIn(ExperimentalUuidApi::class)

package pt.isel.ls.dataMem

import pt.isel.ls.houses.data.memory.UserDataMem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.uuid.ExperimentalUuidApi

class UserDataMemTests {
	private fun newRepo() = UserDataMem()

	@Test
	fun `createUser assigns sequential UIDS`() {
		val repo = newRepo()

		val firstUser = repo.createUser(name = "Dummy", email = "dummy@email.com")
		val secondUser = repo.createUser(name = "Dummy2", email = "dummy2@email.com")

		assertEquals(1, firstUser.uid)
		assertEquals(2, secondUser.uid)
	}

	@Test
	fun `getUser returns created user successfully`() {
		val repo = newRepo()
		val created = repo.createUser(name = "Dummy", email = "dummy@email.com")

		val fetched = repo.getUser(created.uid)

		assertNotNull(fetched)
		assertEquals(created, fetched)
	}

	@Test
	fun `getUserByEmail returns created user successfully`() {
		val repo = newRepo()
		val created = repo.createUser(name = "Dummy", email = "dummy@email.com")

		val fetched = repo.getUserByEmail(created.email)

		assertNotNull(fetched)
		assertEquals(created, fetched)
	}

	@Test
	fun `getUserByToken returns created user successfully`() {
		val repo = newRepo()
		val created = repo.createUser(name = "Dummy", email = "dummy@email.com")

		val fetched = repo.getUserByToken(created.token)

		assertNotNull(fetched)
		assertEquals(created, fetched)
	}

	@Test
	fun `createUser generates unique tokens`() {
		val repo = newRepo()

		val first = repo.createUser(name = "Dummy", email = "dummy@email.com")
		val second = repo.createUser(name = "Dummy2", email = "dummy2@email.com")

		assertNotEquals(first.token, second.token)
	}
}