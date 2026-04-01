package pt.isel.ls.services

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.BookingDoesNotExist
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.HouseIsNotAvailable
import pt.isel.ls.houses.exceptions.InvalidDate
import pt.isel.ls.houses.services.BookingServices
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.services.LocationServices
import kotlinx.datetime.LocalDateTime

class BookingServicesTests {
	private fun createHouse(data: DataMem): Int {
		val locations = LocationServices(data)
		val houses = HouseServices(data)
		val loc = locations.createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null)
		return houses.createHouse(
			title = "Beach House",
			location = loc.lid,
			areaSqMt = 100.0,
			pricePerNight = 90.0,
			description = "Near the sea"
		).hid
	}

	@Test
	fun `createBooking is successful`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)
		val start = LocalDate(2025, 6, 1)
		val end = LocalDate(2025, 6, 7)

		val booking = bookingServices.createBooking(hid = hid, uid = 1, startDate = start, endDate = end)

		assertEquals(1, booking.bid)
		assertEquals(hid, booking.houseId)
		assertEquals(start, booking.startDate)
		assertEquals(end, booking.endDate)
		assertEquals(booking, bookingServices.getBooking(booking.bid))
		assertEquals(listOf(booking), bookingServices.getBookings(hid = hid, startDate = start, endDate = end))
	}

	@Test
	fun `createBooking with start after end throws InvalidDate Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val hid = createHouse(data)

		assertFailsWith<InvalidDate> {
			bookingServices.createBooking(
				hid = hid,
				uid = 1,
				startDate = LocalDate(2025, 6, 10),
				endDate = LocalDate(2025, 6, 5)
			)
		}
	}

	@Test
	fun `createBooking for unknown house throws HouseDoesNotExist Exception`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)

		assertFailsWith<HouseDoesNotExist> {
			bookingServices.createBooking(
				hid = 10,
				uid = 1,
				startDate = LocalDate(2025, 7, 1),
				endDate = LocalDate(2025, 7, 3)
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
		bookingServices.createBooking(hid = hid, uid = 1, startDate = start, endDate = end)

		assertFailsWith<HouseIsNotAvailable> {
			bookingServices.createBooking(hid = hid, uid = 2, startDate = start, endDate = end)
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
				endDate = LocalDate(2025, 9, 5)
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
				endDate = LocalDate(2025, 10, 5)
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
			uid = 1,
			startDate = LocalDate(2025, 11, 1),
			endDate = LocalDate(2025, 11, 3)
		)
		bookingServices.createBooking(
			hid = hid,
			uid = 2,
			startDate = LocalDate(2025, 11, 5),
			endDate = LocalDate(2025, 11, 7)
		)

		val bookings = bookingServices.getBookings(
			hid = hid,
			startDate = LocalDate(2025, 11, 1),
			endDate = LocalDate(2025, 11, 4)
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
				endDate = LocalDate(2025, 12, 5)
			)
		}
	}

	@Test
	fun `getAvailableHouses returns houses with non overlapping bookings`() {
		val data = DataMem()
		val bookingServices = BookingServices(data)
		val houses = HouseServices(data)
		val locId = LocationServices(data).createLocation(name = "Portugal", type = LocationType.COUNTRY, parentId = null).lid
		val busyHouse = houses.createHouse("Busy", locId, 80.0, 70.0, "Booked inside interval")
		val freeHouse = houses.createHouse("Free", locId, 90.0, 85.0, "Booked outside interval")

		bookingServices.createBooking(busyHouse.hid, 1, LocalDate(2026, 1, 2), LocalDate(2026, 1, 4))
		bookingServices.createBooking(freeHouse.hid, 2, LocalDate(2025, 12, 1), LocalDate(2025, 12, 5))

		val available = bookingServices.getAvailableHouses(LocalDate(2026, 1, 1), LocalDate(2026, 1, 10))
		assertEquals(listOf(freeHouse), available)
	}
}