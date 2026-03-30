package pt.isel.ls.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import pt.isel.ls.houses.domain.House

class HouseTests {
    private val validHid = 1
    private val validTitle = "Comfortable Apartment"
    private val validLocationId = 10
    private val validArea = 75.0
    private val validPrice = 1500.0
    private val validDescription = "A cool place to live"

    private fun house(
        hid: Int = validHid,
        title: String = validTitle,
        location: Int = validLocationId,
        area: Double = validArea,
        price: Double = validPrice,
        description: String = validDescription
    ) = House(hid, title, location, area, price, description)

    private val delta = 0.0001 // precisamos disto porque estamos a fazer um assertEquals de um type Double
    @Test
    fun `House is created successfully`() {
        val house = house()
        assertEquals(validHid, house.hid)
        assertEquals(validTitle, house.title)
        assertEquals(validLocationId, house.location)
        assertEquals(validArea, house.area, delta)
        assertEquals(validPrice, house.price, delta)
        assertEquals(validDescription, house.description)
    }

    @Test
    fun `Two houses with same data should be the same`() {
        assertEquals(house(), house())
    }

    @Test
    fun `two houses with different HID should not be the same`() {
        assertNotEquals(house(hid = 1), house(hid = 2))
    }
}