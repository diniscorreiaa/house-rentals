package pt.isel.ls.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.services.LocationServices

class LocationServicesTests {
	private fun newServices() = LocationServices(DataMem())

	@Test
	fun `createLocation without parent persists`() {
		val services = newServices()

		val country = services.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)

		assertEquals(1, country.lid)
		assertEquals(LocationType.COUNTRY, country.type)
		assertEquals(country, services.getLocation(country.lid))
	}

	@Test
	fun `createLocation with non existing parent throws LocationDoesNotExist Exception`() {
		val services = newServices()

		assertFailsWith<LocationDoesNotExist> {
			services.createLocation(name = "Lisbon", type = LocationType.DISTRICT, parentId = 99)
		}
	}

	@Test
	fun `getChildLocations returns children for parent`() {
		val services = newServices()
		val country = services.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)
		val region1 = services.createLocation(name = "Region1", type = LocationType.REGION, parentId = country.lid)
		val region2 = services.createLocation(name = "Region2", type = LocationType.REGION, parentId = country.lid)

		val children = services.getChildLocations(country.lid)
		println(children)

		assertEquals(listOf(region1, region2), children)
	}

	@Test
	fun `getChildLocations for non existing parent throws LocationDoesNotExist Exception`() {
		val services = newServices()

		assertFailsWith<LocationDoesNotExist> {
			services.getChildLocations(lid = 123)
		}
	}

	@Test
	fun `getHierarchy returns parents for non-country`() {
		val services = newServices()
		val country = services.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)
		val region = services.createLocation(name = "Region", type = LocationType.REGION, parentId = country.lid)
		val district = services.createLocation(name = "District", type = LocationType.DISTRICT, parentId = region.lid)

		assertEquals(listOf(region), services.getHierarchy(district.lid))
	}

	@Test
	fun `getHierarchy for non existing location throws LocationDoesNotExist Exception`() {
		val services = newServices()

		assertFailsWith<LocationDoesNotExist> {
			services.getHierarchy(lid = 42)
		}
	}
}