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
import pt.isel.ls.houses.webapi.dtos.UpdateBookingIn
import pt.isel.ls.houses.webapi.dtos.UpdateBookingOut
import pt.isel.ls.houses.webapi.dtos.UserList
import java.time.format.DateTimeParseException

const val INTERVAL_DELIMITER = ':'

class BookingWebApi(private val bookingServices: BookingServices) {

    fun createBooking(request: Request): Response {
        val token = getToken(request)

        val booking = Json.decodeFromString<CreateBookingIn>(request.bodyString())
        val startDate = parseDateOrNull(booking.startDate) ?: throw WrongFormatDate(booking.startDate)
        val endDate = parseDateOrNull(booking.endDate) ?: throw WrongFormatDate(booking.endDate)

        val newBooking = bookingServices.createBooking(booking.hid, startDate, endDate, token)
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
                        booking.bid,
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
            .body(Json.encodeToString(bookings.map { GetBookingOut(it.bid, it.startDate, it.endDate, it.userId, it.houseId) }))
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
                    it.hid,
                    it.title,
                    it.location,
                    it.area,
                    it.price,
                    it.description,
                    it.owner
                )
            }))
    }

    fun deleteBooking(request: Request): Response {
        val token = getToken(request)

        val bookindId = request.path("bid").let {
            it ?: throw RequiredPathParamNotFound("bid")
            it.toIntOrNull() ?: throw InvalidIdentifier("bid")
        }
        bookingServices.deleteBooking(bookindId, token)
        return Response(NO_CONTENT)
            .header("content-type", "application/json")
    }

    fun updateBooking(request: Request): Response {
        val token = getToken(request)

        val dates = Json.decodeFromString<UpdateBookingIn>(request.bodyString())
        val startDate = parseDateOrNull(dates.startDate) ?: throw WrongFormatDate(dates.startDate)
        val endDate = parseDateOrNull(dates.endDate) ?: throw WrongFormatDate(dates.endDate)

        val bookingId = request.path("bid").let {
            it ?: throw RequiredPathParamNotFound("bid")
            it.toIntOrNull() ?: throw InvalidIdentifier("bid")
        }

        val updatedBooking = bookingServices.updateBooking(bookingId, startDate, endDate, token)
        return Response(OK)
            .header("content-type", "application/json")
            .body(
                Json.encodeToString(
                    UpdateBookingOut(
                        updatedBooking.startDate,
                        updatedBooking.endDate,
                        updatedBooking.userId,
                        updatedBooking.houseId
                    )
                )
            )
    }
    // /bookings/users?date-interval=
    fun getUserListWithBookingsInInterval(request: Request): Response {
        val interval = request.query("date-interval").let { intervalStr ->
            intervalStr ?: throw RequiredQueryParamNotFound("date-interval")
            intervalStr.intervalToDates() ?: throw WrongSeparatorDate(intervalStr)
        }

        val userList = bookingServices.getUserListWithBookingsInInterval(interval.startDate, interval.endDate)
        return Response(OK)
            .header("content-type", "application/json")
            .body(
                Json.encodeToString(
                    userList.map {
                        UserList(
                            it.name,
                            it.email,
                            0
                        )
                    }

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
    } catch (_: IllegalArgumentException) {
        null
    }
}