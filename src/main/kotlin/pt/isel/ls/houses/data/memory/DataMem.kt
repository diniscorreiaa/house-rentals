package pt.isel.ls.houses.data.memory

import pt.isel.ls.houses.repository.DataRepository

class DataMem : DataRepository {
    override val house = HouseDataMem()
    override val user = UserDataMem(house)
    override val location = LocationDataMem()
    override val booking = BookingDataMem(house)
}