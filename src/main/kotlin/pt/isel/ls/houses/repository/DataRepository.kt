package pt.isel.ls.houses.repository

interface DataRepository : BookingRepository
{
    val user: UserRepository
    val location: LocationRepository
    val house: HouseRepository
}