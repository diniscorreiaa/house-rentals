// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-file
@file:OptIn(ExperimentalUuidApi::class)

package pt.isel.ls.services

import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.LocationParentDoesNotExist
import pt.isel.ls.houses.services.LocationServices
import pt.isel.ls.houses.services.UserServices
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

class LocationServicesTests {
    private val data = DataMem()
    private fun newServices() = LocationServices(data.user, data.location)

    private val token = UserServices(data.user, data.booking).createUser("dummy", "dummy@example.com").token

    @Test
    fun `createLocation without parent persists`() {
        val services = newServices()

        val country = services.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null, token)

        assertEquals(1, country.lid)
        assertEquals(LocationType.COUNTRY, country.type)
        assertEquals(country, services.getLocation(country.lid))
    }

    @Test
    fun `createLocation with non existing parent throws LocationParentDoesNotExist Exception`() {
        val services = newServices()

        assertFailsWith<LocationParentDoesNotExist> {
            services.createLocation(name = "Lisbon", type = LocationType.DISTRICT, parentId = 99, token)
        }
    }

    @Test
    fun `getChildLocations returns children for parent`() {
        val services = newServices()
        val country = services.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null, token)
        val region1 =
            services.createLocation(name = "Region1", type = LocationType.REGION, parentId = country.lid, token)
        val region2 =
            services.createLocation(name = "Region2", type = LocationType.REGION, parentId = country.lid, token)

        val children = services.getChildLocations(country.lid, null, null)

        assertEquals(listOf(region1, region2), children)
    }

    @Test
    fun `getChildLocations for non existing parent throws LocationDoesNotExist Exception`() {
        val services = newServices()

        assertFailsWith<LocationDoesNotExist> {
            services.getChildLocations(lid = 123, null, null)
        }
    }

    @Test
    fun `getHierarchy returns parents for non-country`() {
        val services = newServices()
        val country = services.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null, token)
        val region = services.createLocation(name = "Region", type = LocationType.REGION, parentId = country.lid, token)
        val district =
            services.createLocation(name = "District", type = LocationType.DISTRICT, parentId = region.lid, token)

        assertEquals(listOf(country, region, district), services.getHierarchy(district.lid))
    }

    @Test
    fun `getHierarchy for non existing location throws LocationDoesNotExist Exception`() {
        val services = newServices()

        assertFailsWith<LocationDoesNotExist> {
            services.getHierarchy(lid = 42)
        }
    }
}