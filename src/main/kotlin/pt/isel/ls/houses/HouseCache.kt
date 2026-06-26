package pt.isel.ls.houses

import pt.isel.ls.houses.domain.House


class HouseCache(private val n: Int) {
    private val cache: MutableList<House> = mutableListOf()

    init {
        require(n > 0) { "Cache must hold at least 1 value" }
    }

    fun getHouse(id: Int): House? {
        val house = cache.find { it.hid == id }

        if (house != null) {
            cache.remove(house) // remove the house to update its position in the cache
            cache.add(house)             // here it is in the end => "most recently used"
            return house
        }
        return null
    }

    fun putHouse(house: House) {
        val existed = cache.removeIf { it.hid == house.hid } // remove the house if it already exists to update its position in the cache
        if (existed){
            cache.add(house) // make newest
            return
        }

        if (cache.size >= n) cache.removeAt(0) // remove the oldest one if cache is full
        cache.add(house)
    }
}