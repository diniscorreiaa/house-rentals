package pt.isel.ls.houses.webapi

import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import pt.isel.ls.houses.exceptions.InvalidIdentifier
import pt.isel.ls.houses.exceptions.RequiredPathParamNotFound
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

class BookingWebApi(private val bookingServices: BookingServices) {

    fun createBooking(request: Request): Response {
        val booking = Json.decodeFromString<CreateBookingIn>(request.bodyString())
        val token = getToken(request)

        val newBooking = bookingServices.createBooking(booking.hid, booking.startDate, booking.endDate, token)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(CreateBookingOut(newBooking.bid)))
    }

    fun getBooking(request: Request): Response {
        val bookingId = request.path("bid").let {
            it ?: throw RequiredPathParamNotFound("bid")
            it.toIntOrNull() ?: throw InvalidIdentifier("bid")
        }
        val booking = bookingServices.getBooking(bookingId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(
                Json.encodeToString(
                    GetBookingOut(
                        booking.startDate,
                        booking.endDate,
                        booking.userId,
                        booking.houseId
                    )
                )
            )
    }

    fun getBookings(request: Request): Response {
        // TODO: De certeza que faz sentido ser passado no path???
        val houseId = request.query("hid").let {
            it ?: throw RequiredQueryParamNotFound("hid")
            it.toIntOrNull() ?: throw InvalidIdentifier("hid")
        }
        val interval = request.query("date-interval").let { intervalStr ->
            intervalStr ?: throw RequiredQueryParamNotFound("date-interval")
            intervalStr.intervalToDates() ?: throw WrongSeparatorDate(intervalStr)
        }

        val startDate = interval.startDate
        val endDate = interval.endDate
        val paging = getLimitAndSkip(request)
        val bookings = bookingServices.getBookings(houseId, startDate, endDate, paging.limit, paging.skip)

        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(bookings.map { GetBookingOut(it.startDate, it.endDate, it.userId, it.houseId) }))
    }

    fun getAvailableHouses(request: Request): Response {
        val startDate = request.query("startDate").let {
            it ?: throw RequiredQueryParamNotFound("startDate")
            parseDateOrNull(it) ?: throw WrongFormatDate(it)
        }

        val endDate = request.query("endDate").let {
            it ?: throw RequiredQueryParamNotFound("endDate")
            parseDateOrNull(it) ?: throw WrongFormatDate(it)
        }

        val paging = getLimitAndSkip(request)
        val available = bookingServices.getAvailableHouses(startDate, endDate, paging.limit, paging.skip)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(available.map {
                GetHouseOut(
                    it.title,
                    it.location,
                    it.area,
                    it.price,
                    it.description
                )
            }))
    }

    fun deleteBooking(request: Request): Response {
        val bookindId = request.path("bid").let {
            it ?: throw RequiredPathParamNotFound("bid")
            it.toIntOrNull() ?: throw InvalidIdentifier("bid")
        }
        bookingServices.deleteBooking(bookindId)
        return Response(NO_CONTENT)
            .header("content-type", "application/json")
    }

    fun updateBooking(request: Request): Response {
        val bookingId = request.path("bid").let {
            it ?: throw RequiredPathParamNotFound("bid")
            it.toIntOrNull() ?: throw InvalidIdentifier("bid")
        }

        val interval = request.query("date-interval").let { intervalStr ->
            intervalStr ?: throw RequiredQueryParamNotFound("date-interval")
            intervalStr.intervalToDates() ?: throw WrongSeparatorDate(intervalStr)
        }
        val startDate = interval.startDate
        val endDate = interval.endDate

        val updatedBooking = bookingServices.updateBooking(bookingId, startDate, endDate)
        return Response(OK)
            .header("content-type", "application/json")
            .body(
                Json.encodeToString(
                GetBookingOut(
                    updatedBooking.startDate,
                    updatedBooking.endDate,
                    updatedBooking.userId,
                    updatedBooking.houseId
                    )
                )
            )
    }
}

private class Interval(val startDate: LocalDate, val endDate: LocalDate)

// TODO: refactor later
private fun String.intervalToDates(): Interval? {
    val split = this.split(INTERVAL_DELIMITER)
    if (split.size != 2) return null

    val start = parseDateOrNull(split.first()) ?: throw WrongFormatDate(split.first())
    val end = parseDateOrNull(split.last()) ?: throw WrongFormatDate(split.last())
    return Interval(start, end)
}

private fun parseDateOrNull(date: String): LocalDate? {
    return try {
        LocalDate.parse(date)
    } catch (_: DateTimeParseException) {
        null
    }
}