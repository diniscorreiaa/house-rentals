package pt.isel.ls.houses.webapi

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import pt.isel.ls.houses.data.memory.BookingDataMem
import pt.isel.ls.houses.data.memory.HouseDataMem
import pt.isel.ls.houses.exceptions.BookingDoesNotExist
import pt.isel.ls.houses.exceptions.RequiredQueryParamNotFound
import pt.isel.ls.houses.exceptions.WrongFormatDate
import pt.isel.ls.houses.exceptions.WrongSeparatorDate
import pt.isel.ls.houses.services.BookingServices
import pt.isel.ls.houses.webapi.dtos.CreateBookingIn
import pt.isel.ls.houses.webapi.dtos.CreateBookingOut
import pt.isel.ls.houses.webapi.dtos.GetBookingOut
import pt.isel.ls.houses.webapi.dtos.GetHouseOut
import java.time.format.DateTimeParseException

const val INTERVAL_DELIMITER = ':'

class BookingWebApi {
    private val bookingServices = BookingServices(BookingDataMem(HouseDataMem()), HouseDataMem())

    fun createBooking(request: Request): Response {
        val booking = Json.decodeFromString<CreateBookingIn>(request.bodyString())
        val newBooking = bookingServices.createBooking(booking.bid, booking.startDate, booking.endDate)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(CreateBookingOut(newBooking.bid)))
    }

    fun getBooking(request: Request): Response {
        val bookingId = request.path("bid")?.toIntOrNull() ?: throw BookingDoesNotExist()
        val booking = bookingServices.getBooking(bookingId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GetBookingOut(booking.startDate, booking.endDate, booking.userId, booking.houseId)))
    }

    fun getBookings(request: Request): Response {
        val houseId = request.query("hid")?.toIntOrNull() ?: throw BookingDoesNotExist() // TODO: De certeza que faz sentido ser passado no path???
        val interval = request.query("date-interval").let { intervalStr ->
            if (intervalStr == null) throw RequiredQueryParamNotFound("date-interval")
            intervalStr.intervalToDates().let {
                if (it == null) throw WrongSeparatorDate(intervalStr)
                it
            }
        }

        val startDate = interval.startDate
        val endDate = interval.endDate

        val bookings = bookingServices.getBookings(houseId, startDate, endDate)

        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(bookings.map{ GetBookingOut(it.startDate, it.endDate, it.userId, it.houseId) }))
    }

    fun getAvailableHouses(request: Request): Response {
        val startDate = request.query("startDate").let { dateStr ->
            if (dateStr == null) throw RequiredQueryParamNotFound("startDate")
            parseDateOrNull(dateStr).let { date ->
                if (date == null) throw WrongFormatDate(dateStr)
                date
            }
        }

        val endDate = request.query("endDate").let { dateStr ->
            if (dateStr == null) throw RequiredQueryParamNotFound("endDate")
            parseDateOrNull(dateStr).let { date ->
                if (date == null) throw WrongFormatDate(dateStr)
                date
            }
        }

        val available = bookingServices.getAvailableHouses(startDate, endDate)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(available.map { GetHouseOut(it.title, it.location, it.area, it.price, it.description) }))
    }
}

private class Interval(val startDate: LocalDateTime, val endDate: LocalDateTime)

// TODO: refactor later
private fun String.intervalToDates(): Interval? {
    val split = this.split(INTERVAL_DELIMITER)
    if (split.size != 2) return null

    val start = parseDateOrNull(split.first()) ?: throw WrongFormatDate(split.first())
    val end = parseDateOrNull(split.last()) ?: throw WrongFormatDate(split.last())
    return Interval(start, end)
}

private fun parseDateOrNull(date: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(date)
    } catch (_: DateTimeParseException) {
        null
    }
}