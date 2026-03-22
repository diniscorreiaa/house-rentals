package pt.isel.ls.houses.exceptions

import org.http4k.core.Status

open class HttpException(val status: Status, override val message: String): Throwable()

class Unauthorized : HttpException(Status.UNAUTHORIZED, "Invalid auth token")

class UserEmailAlreadyExists : HttpException(Status.BAD_REQUEST, "User email already exists")
class UserDoesNotExist : HttpException(Status.NOT_FOUND, "User does not exist")

class LocationDoesNotExist : HttpException(Status.NOT_FOUND, "Location does not exist")

class HouseDoesNotExist : HttpException(Status.NOT_FOUND, "House does not exist")
class HouseIsNotAvailable : HttpException(Status.CONFLICT, "House is not available")

class BookingDoesNotExist : HttpException(Status.NOT_FOUND, "Booking does not exist")

class InvalidDate : HttpException(Status.BAD_REQUEST, "Invalid date")
