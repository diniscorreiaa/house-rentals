package pt.isel.ls.houses.repository

interface DataRepository
{
    val user: UserRepository
    val location: LocationRepository
    val house: HouseRepository
    val booking: BookingRepository
}