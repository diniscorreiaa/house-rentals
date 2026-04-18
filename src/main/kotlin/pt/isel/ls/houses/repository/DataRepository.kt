package pt.isel.ls.houses.repository

interface DataRepository : BookingRepository, HouseRepository
{
    val user: UserRepository
    val location: LocationRepository
}