// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-file
@file:OptIn(ExperimentalUuidApi::class)

package pt.isel.ls.services


import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.BookingDoesNotExist
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.HouseIsNotAvailable
import pt.isel.ls.houses.exceptions.InvalidDate
import pt.isel.ls.houses.services.BookingServices
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.services.LocationServices
import pt.isel.ls.houses.services.UserServices
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class BookingServicesTests {
    lateinit var token: Uuid

    private fun createHouse(data: DataMem): Int {
		val locations = LocationServices(data)
		val houses = HouseServices(data)

		token = UserServices(data).createUser("dummy", "dummy@example.com").token

		val loc = locations.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null, token)
		return houses.createHouse(
			title = "Beach House",
			location = loc.lid,
			areaSqMt = 100.0,
			pricePerNight = 90.0,
			description = "Near the sea",
			token
		).hid
	}

    @Test
	fun `createBooking is successful`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)
		val start = LocalDate(2025, 6, 1)
		val end = LocalDate(2025, 6, 7)


		val booking = bookingServices.createBooking(hid = hid, startDate = start, endDate = end, token)

		assertEquals(1, booking.bid)
		assertEquals(hid, booking.houseId)
		assertEquals(start, booking.startDate)
		assertEquals(end, booking.endDate)
		assertEquals(booking, bookingServices.getBooking(booking.bid))
		assertEquals(listOf(booking), bookingServices.getBookings(hid = hid, startDate = start, endDate = end, null, null))
	}

    @Test
	fun `createBooking with start after end throws InvalidDate Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)

		assertFailsWith<InvalidDate> {
			bookingServices.createBooking(
				hid = hid,
				startDate = LocalDate(2025, 6, 10),
				endDate = LocalDate(2025, 6, 5),
				token
			)
		}
	}

    @Test
	fun `createBooking for unknown house throws HouseDoesNotExist Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)

		token = UserServices(data).createUser("dummy", "dummy@example.com").token

		assertFailsWith<HouseDoesNotExist> {
			bookingServices.createBooking(
				hid = 10,
				startDate = LocalDate(2025, 7, 1),
				endDate = LocalDate(2025, 7, 3),
				token
			)
		}
	}

    @Test
	fun `createBooking on occupied interval throws HouseIsNotAvailable Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)
		val start = LocalDate(2025, 8, 1)
		val end = LocalDate(2025, 8, 5)


		bookingServices.createBooking(hid = hid, startDate = start, endDate = end, token)

		assertFailsWith<HouseIsNotAvailable> {
			bookingServices.createBooking(hid = hid, startDate = start, endDate = end, token)
		}
	}

	@Test
	fun `getBooking for unknown id throws BookingDoesNotExist Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)

		assertFailsWith<BookingDoesNotExist> {
			bookingServices.getBooking(bid = 10)
		}
	}

	@Test
	fun `getBookings with invalid interval throws InvalidDate`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)

		assertFailsWith<InvalidDate> {
			bookingServices.getBookings(
				hid = hid,
				startDate = LocalDate(2025, 9, 10),
				endDate = LocalDate(2025, 9, 5),
				null,
				null
			)
		}
	}

	@Test
	fun `getBookings for unknown house throws HouseDoesNotExist Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)

		assertFailsWith<HouseDoesNotExist> {
			bookingServices.getBookings(
				hid = 8,
				startDate = LocalDate(2025, 10, 1),
				endDate = LocalDate(2025, 10, 5),
				null,
				null
			)
		}
	}

    @Test
	fun `getBookings returns bookings within interval`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)


		val first = bookingServices.createBooking(
			hid = hid,
			startDate = LocalDate(2025, 11, 1),
			endDate = LocalDate(2025, 11, 3),
			token
		)
		bookingServices.createBooking(
			hid = hid,
			startDate = LocalDate(2025, 11, 5),
			endDate = LocalDate(2025, 11, 7),
			token
		)

		val bookings = bookingServices.getBookings(
			hid = hid,
			startDate = LocalDate(2025, 11, 1),
			endDate = LocalDate(2025, 11, 4),
			null,
			null
		)

		assertEquals(listOf(first), bookings)
	}

	@Test
	fun `getAvailableHouses with invalid interval throws InvalidDate Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)

		assertFailsWith<InvalidDate> {
			bookingServices.getAvailableHouses(
				startDate = LocalDate(2025, 12, 10),
				endDate = LocalDate(2025, 12, 5),
				null,
				null
			)
		}
	}

    @Test
	fun `getAvailableHouses returns houses with non overlapping bookings`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val houses = HouseServices(data)

		token = UserServices(data).createUser("dummy", "dummy@example.com").token

		val locId = LocationServices(data).createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null, token).lid
		val busyHouse = houses.createHouse("Busy", locId, 80.0, 70.0, "Booked inside interval", token)
		val freeHouse = houses.createHouse("Free", locId, 90.0, 85.0, "Booked outside interval", token)

		bookingServices.createBooking(busyHouse.hid, LocalDate(2026, 1, 2), LocalDate(2026, 1, 4), token)
		bookingServices.createBooking(freeHouse.hid, LocalDate(2025, 12, 1), LocalDate(2025, 12, 5), token)

		val available = bookingServices.getAvailableHouses(LocalDate(2026, 1, 1), LocalDate(2026, 1, 10), null, null)
		assertEquals(listOf(freeHouse), available)
	}
}