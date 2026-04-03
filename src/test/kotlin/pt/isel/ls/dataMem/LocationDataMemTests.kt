package pt.isel.ls.dataMem

import pt.isel.ls.houses.data.memory.LocationDataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class LocationDataMemTests {

	private fun newRepo() = LocationDataMem()

	@Test
	fun `createLocation assigns sequential LIDs and stores fields successfully`() {
		val repo = newRepo()

		val country = repo.createLocation("Portugal", LocationType.COUNTRY, null)
		val region = repo.createLocation("Porto", LocationType.DISTRICT, country.lid)

		assertEquals(1, country.lid)
		assertEquals(2, region.lid)
		assertEquals("Porto", region.name)
		assertEquals(LocationType.DISTRICT, region.type)
		assertEquals(country.lid, region.parentLocation)
	}

	@Test
	fun `getLocation returns created location and null for non existing id`() {
		val repo = newRepo()
		val country = repo.createLocation("Country", LocationType.COUNTRY, null)
		val region = repo.createLocation("Region", LocationType.REGION, country.lid)

		val fetched = repo.getLocation(region.lid)

		assertNotNull(fetched)
		assertEquals(region, fetched)
		assertEquals(null, repo.getLocation(10))
	}

	@Test
	fun `getChildLocations returns only direct children`() {
		val repo = newRepo()
		val country = repo.createLocation("Portugal", LocationType.COUNTRY, null)
		val region1 = repo.createLocation("Region", LocationType.REGION, country.lid)
		val region2 = repo.createLocation("Region2", LocationType.REGION, country.lid)
		repo.createLocation("Lisbon District", LocationType.DISTRICT, region1.lid)

		val children = repo.getChildLocations(country.lid, limit = null, skip = null)

		assertEquals(listOf(region1, region2), children)
	}

	@Test
	fun `getChildLocations applies skip and limit`() {
		val repo = newRepo()
		val country = repo.createLocation("Portugal", LocationType.COUNTRY, null)
		val region1 = repo.createLocation("Region", LocationType.REGION, country.lid)
		val region2 = repo.createLocation("Region2", LocationType.REGION, country.lid)
		val region3 = repo.createLocation("Region3", LocationType.REGION, country.lid)

		val paged = repo.getChildLocations(country.lid, limit = 2, skip = 1)

		assertEquals(listOf(region2, region3), paged)
	}

	@Test
	fun `getChildLocations throws LocationDoesNotExist Exception when parent does not exist`() {
		val repo = newRepo()

		assertFailsWith<LocationDoesNotExist> {
			repo.getChildLocations(5, limit = null, skip = null)
		}
	}

	@Test
	fun `getHierarchy returns ordered path from root to node`() {
		val repo = newRepo()
		val country = repo.createLocation("Country", LocationType.COUNTRY, null)
		val region = repo.createLocation("Region", LocationType.REGION, country.lid)
		val district = repo.createLocation("District", LocationType.DISTRICT, region.lid)
		val municipality = repo.createLocation("Municipality", LocationType.MUNICIPALITY, district.lid)
		val locality = repo.createLocation("Locality", LocationType.LOCALITY, municipality.lid)
		val expected = listOf(country, region, district, municipality, locality)

		val hierarchy = repo.getHierarchy(locality.lid)

		assertEquals(expected, hierarchy)
	}

	@Test
	fun `getHierarchy throws LocationDoesNotExist Exception when location does not exist`() {
		val repo = newRepo()

		assertFailsWith<LocationDoesNotExist> {
			repo.getHierarchy(10)
		}
	}
}