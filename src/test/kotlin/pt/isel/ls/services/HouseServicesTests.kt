// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-file
@file:OptIn(ExperimentalUuidApi::class)

package pt.isel.ls.services

import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.services.LocationServices
import pt.isel.ls.houses.services.UserServices
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HouseServicesTests {
    lateinit var token: Uuid

    private fun locationServices(data: DataMem) = LocationServices(data.user, data.location)

    private fun houseServices(data: DataMem) = HouseServices(data.user, data.location, data.house)

    private fun userServices(data: DataMem) = UserServices(data.user, data.booking)

    private fun createParentLocations(locationServices: LocationServices) {
        locationServices.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null, token)
        locationServices.createLocation(name = "Lisboa", type = LocationType.DISTRICT, parentId = 1, token)
        locationServices.createLocation(name = "Oeiras", type = LocationType.MUNICIPALITY, parentId = 2, token)
    }

    @Test
    fun `createHouse with existing location is created successfully`() {
        val data = DataMem()
        val locations = locationServices(data)
        val houses = houseServices(data)

        token = userServices(data).createUser("dummy", "dummy@example.com").token

        createParentLocations(locationServices(data))
        val loc = locations.createLocation(name = "Paço de Arcos", type = LocationType.LOCALITY, parentId = 3, token)

        val house = houses.createHouse(
            title = "Beach House",
            location = loc.lid,
            areaSqMt = 120.0,
            pricePerNight = 95.0,
            description = "Near the sea",
            token
        )

        assertEquals(1, house.hid)
        assertEquals("Beach House", house.title)
        assertEquals(loc.lid, house.location)
        assertEquals(120.0, house.area)
        assertEquals(95.0, house.price)
        assertEquals("Near the sea", house.description)
        assertEquals(house, houses.getHouse(house.hid))
        assertEquals(listOf(house), houses.getHouses(null, null))
    }

    @Test
    fun `createHouse for non existing location throws LocationDoesNotExist Exception`() {
        val data = DataMem()
        val houses = houseServices(data)

        token = userServices(data).createUser("dummy", "dummy@example.com").token

        assertFailsWith<LocationDoesNotExist> {
            houses.createHouse(
                title = "Lonely House",
                location = 404,
                areaSqMt = 80.0,
                pricePerNight = 70.0,
                description = "No location",
                token
            )
        }
    }

    @Test
    fun `getHouse for unknown id throws HouseDoesNotExist Exception`() {
        val data = DataMem()
        val locations = locationServices(data)
        val houses = houseServices(data)

        token = userServices(data).createUser("dummy", "dummy@example.com").token

        createParentLocations(locationServices(data))
        val loc = locations.createLocation(name = "Paço de Arcos", type = LocationType.LOCALITY, parentId = 3, token)
        houses.createHouse(
            title = "House",
            location = loc.lid,
            areaSqMt = 100.0,
            pricePerNight = 90.0,
            description = "desc",
            token
        )

        assertFailsWith<HouseDoesNotExist> {
            houses.getHouse(hid = 123)
        }
    }

    @Test
    fun `getHouses returns all created houses`() {
        val data = DataMem()
        val locations = locationServices(data)
        val houses = houseServices(data)

        token = userServices(data).createUser("dummy", "dummy@example.com").token

        createParentLocations(locationServices(data))
        val loc = locations.createLocation(name = "Paço de Arcos", type = LocationType.LOCALITY, parentId = 3, token)

        val house1 = houses.createHouse(
            title = "House1",
            location = loc.lid,
            areaSqMt = 100.0,
            pricePerNight = 90.0,
            description = "description1",
            token
        )
        val house2 = houses.createHouse(
            title = "House2",
            location = loc.lid,
            areaSqMt = 200.0,
            pricePerNight = 190.0,
            description = "description2",
            token
        )

        assertEquals(listOf(house1, house2), houses.getHouses(null, null))
    }
}