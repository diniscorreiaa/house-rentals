package pt.isel.ls.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.services.LocationServices

class HouseServicesTests {
    @Test
    fun `createHouse with existing location is created successfully`() {
        val data = DataMem()
        val locations = LocationServices(data)
        val houses = HouseServices(data)
        val loc = locations.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)

        val house = houses.createHouse(
            title = "Beach House",
            location = loc.lid,
            areaSqMt = 120.0,
            pricePerNight = 95.0,
            description = "Near the sea"
        )

        assertEquals(1, house.hid)
        assertEquals("Beach House", house.title)
        assertEquals(loc.lid, house.location)
        assertEquals(120.0, house.area)
        assertEquals(95.0, house.price)
        assertEquals("Near the sea", house.description)
        assertEquals(house, houses.getHouse(house.hid))
        assertEquals(listOf(house), houses.getHouses())
    }

    @Test
    fun `createHouse for non existing location throws LocationDoesNotExist Exception`() {
        val data = DataMem()
        val houses = HouseServices(data)

        assertFailsWith<LocationDoesNotExist> {
            houses.createHouse(
                title = "Lonely House",
                location = 404,
                areaSqMt = 80.0,
                pricePerNight = 70.0,
                description = "No location"
            )
        }
    }

    @Test
    fun `getHouse for unknown id throws HouseDoesNotExist Exception`() {
        val data = DataMem()
        val locations = LocationServices(data)
        val houses = HouseServices(data)
        val loc = locations.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)
        houses.createHouse(
            title = "House",
            location = loc.lid,
            areaSqMt = 100.0,
            pricePerNight = 90.0,
            description = "desc"
        )

        assertFailsWith<HouseDoesNotExist> {
            houses.getHouse(hid = 123)
        }
    }

    @Test
    fun `getHouses returns all created houses`() {
        val data = DataMem()
        val locations = LocationServices(data)
        val houses = HouseServices(data)
        val loc = locations.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)

        val house1 = houses.createHouse(
            title = "House1",
            location = loc.lid,
            areaSqMt = 100.0,
            pricePerNight = 90.0,
            description = "description1"
        )
        val house2 = houses.createHouse(
            title = "House2",
            location = loc.lid,
            areaSqMt = 200.0,
            pricePerNight = 190.0,
            description = "description2"
        )

        assertEquals(listOf(house1, house2), houses.getHouses())
    }
}