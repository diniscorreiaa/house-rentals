package pt.isel.ls.houses.repository

import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.User

interface HouseRepository {
    fun createHouse(title: String, location: Int, areaSqMt: Double, pricePerNight: Double, description: String, owner: Int): House
    fun getHouse(hid: Int): House?
    fun getHouses(limit: Int?, skip: Int?): List<House>
}