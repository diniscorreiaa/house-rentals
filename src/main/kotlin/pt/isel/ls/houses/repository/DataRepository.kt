package pt.isel.ls.houses.repository

interface DataRepository : BookingRepository, HouseRepository, LocationRepository
{
    val user: UserRepository
}