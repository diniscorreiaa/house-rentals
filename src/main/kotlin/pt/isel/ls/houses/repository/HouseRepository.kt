package pt.isel.ls.houses.repository

import pt.isel.ls.houses.domain.House

interface HouseRepository {
    fun createHouse(title: String, location: Int, areaSqMt: Double, pricePerNight: Double, description: String, owner: Int): House
    fun getHouse(hid: Int): House?
    fun getHouses(locId: Int?, name: String?, limit: Int?, skip: Int?): List<House>
}