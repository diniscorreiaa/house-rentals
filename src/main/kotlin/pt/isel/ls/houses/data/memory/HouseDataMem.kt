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

    override fun getHouses(locId: Int?, name: String?, limit: Int?, skip: Int?): List<House> {
        var result = houses.toList()

        if (locId != null) result = result.filter { it.location == locId }
        if (!name.isNullOrBlank()) result = result.filter { it.title.contains(name, true) }

        if (limit == null || result.isEmpty()) return result

        var lastHouseIdx = (skip ?: 0) + limit
        if (lastHouseIdx > result.size) lastHouseIdx = result.size
        return result.subList(skip ?: 0, lastHouseIdx)
    }
}