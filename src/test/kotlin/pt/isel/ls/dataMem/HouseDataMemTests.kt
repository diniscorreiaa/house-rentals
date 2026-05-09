/*package pt.isel.ls.dataMem

import pt.isel.ls.houses.data.memory.HouseDataMem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HouseDataMemTests {
    private fun newRepo() = HouseDataMem()

    @Test
    fun `createHouse assigns sequential HIDs`() {
        val repo = newRepo()

        val first = repo.createHouse(
            title = "First",
            location = 1,
            areaSqMt = 80.0,
            pricePerNight = 50.0,
            description = "First house"
        )
        val second = repo.createHouse(
            title = "Second",
            location = 2,
            areaSqMt = 90.0,
            pricePerNight = 60.0,
            description = "Second house"
        )

        assertEquals(1, first.hid)
        assertEquals(2, second.hid)
    }

    @Test
    fun `createHouse stores provided fields`() {
        val repo = newRepo()

        val created = repo.createHouse(
            title = "Loft",
            location = 3,
            areaSqMt = 65.5,
            pricePerNight = 120.0,
            description = "Skyline view"
        )

        assertEquals("Loft", created.title)
        assertEquals(3, created.location)
        assertEquals(65.5, created.area)
        assertEquals(120.0, created.price)
        assertEquals("Skyline view", created.description)
    }

    @Test
    fun `getHouse returns created house successfully`() {
        val repo = newRepo()
        val created = repo.createHouse(
            title = "Cabin",
            location = 4,
            areaSqMt = 70.0,
            pricePerNight = 75.0,
            description = "Cozy cabin"
        )

        val fetched = repo.getHouse(created.hid)

        assertNotNull(fetched)
        assertEquals(created, fetched)
    }

    @Test
    fun `getHouse returns null for unknown hid`() {
        val repo = newRepo()

        val fetched = repo.getHouse(999)

        assertEquals(null, fetched)
    }

    @Test
    fun `getHouses returns all houses`() {
        val repo = newRepo()
        val first = repo.createHouse(
            title = "One",
            location = 5,
            areaSqMt = 55.0,
            pricePerNight = 45.0,
            description = "One desc"
        )
        val second = repo.createHouse(
            title = "Two",
            location = 6,
            areaSqMt = 65.0,
            pricePerNight = 55.0,
            description = "Two desc"
        )
        val third = repo.createHouse(
            title = "Three",
            location = 7,
            areaSqMt = 75.0,
            pricePerNight = 65.0,
            description = "Three desc"
        )

        val houses = repo.getHouses(limit = null, skip = null)

        assertEquals(listOf(first, second, third), houses)
    }

    @Test
    fun `getHouses returns empty list when none created`() {
        val repo = newRepo()

        assertTrue(repo.getHouses(limit = null, skip = null).isEmpty())
    }

    @Test
    fun `getHouses applies skip and limit safely`() {
        val repo = newRepo()
        val first = repo.createHouse("A", 8, 80.0, 80.0, "A House")
        val second = repo.createHouse("B", 9, 90.0, 90.0, "B House")
        val third = repo.createHouse("C", 10, 100.0, 100.0, "C House")

        val paged = repo.getHouses(limit = 2, skip = 1)
        assertEquals(listOf(second, third), paged)

    }
}*/