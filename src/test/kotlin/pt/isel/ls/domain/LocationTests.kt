package pt.isel.ls.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType

class LocationTests {
    private val validLid = 1
    private val validName = "Lisbon"
    private val validType = LocationType.DISTRICT
    private val validParent = 3

    @Test
    fun `Location is created successfully`() {
        val location = Location(
            lid = validLid,
            name = validName,
            type = validType,
            parentLocation = validParent
        )

        assertEquals(validLid, location.lid)
        assertEquals(validName, location.name)
        assertEquals(validType, location.type)
        assertEquals(validParent, location.parentLocation)
    }

    @Test
    fun `Create location without parent should have null parentLocation`() {
        val location = Location(
            lid = validLid,
            name = "DummyName",
            type = LocationType.COUNTRY,
            parentLocation = null
        )

        assertNull(location.parentLocation)
    }

    @Test
    fun `Create location with parentLocation set should not be null`() {
        val location = Location(
            lid = 2,
            name = "Porto",
            type = LocationType.DISTRICT,
            parentLocation = validParent
        )

        assertNotNull(location.parentLocation)
        assertEquals(3, location.parentLocation)
    }

    @Test
    fun `Create location with each LocationType should succeed`() {
        for (type in LocationType.entries) {
            val location = Location(lid = validLid, name = validName, type = type, parentLocation = null)
            assertEquals(type, location.type)
        }
    }
}