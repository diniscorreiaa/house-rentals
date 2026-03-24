package pt.isel.ls.houses.webapi

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.houses.data.memory.BookingDataMem
import pt.isel.ls.houses.data.memory.HouseDataMem
import pt.isel.ls.houses.services.BookingServices

class BookingWebApi {
    private val bookingWebApi = BookingServices(BookingDataMem(HouseDataMem()), HouseDataMem())

    fun createBooking(request: Request): Response {

        return Response(OK)
    }

    fun getBooking(request: Request): Response {

        return Response(OK)
    }

    fun getBookings(request: Request): Response {

        return Response(OK)
    }

    fun getAvailableHouses(request: Request): Response {

        return Response(OK)
    }
}