package pt.isel.ls.houses.data.memory

import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.repository.HouseRepository

class HouseDataMem : HouseRepository {
    private val houses = mutableListOf<House>()
    private var id = 1

    override fun createHouse(
        title: String,
        location: Int,
        areaSqMt: Double,
        pricePerNight: Double,
        description: String,
        owner: Int
    ): House {
        val house = House(id++, title, location, areaSqMt, pricePerNight, description, owner)
        houses.add(house)
        return house
    }

    override fun getHouse(hid: Int): House? = houses.find { it.hid == hid }

    override fun getHouses(limit: Int?, skip: Int?): List<House> {
        if (limit == null || houses.isEmpty()) return houses
        else {
            var lastHouseIdx = (skip ?: 0) + limit
            if (lastHouseIdx > houses.size) lastHouseIdx = houses.size
            return houses.subList(skip ?: 0, lastHouseIdx)
        }
    }
}