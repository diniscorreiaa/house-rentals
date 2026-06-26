package pt.isel.ls.houses.webapi.dtos

import kotlinx.serialization.Serializable

/////////// Input ///////////
@Serializable
data class CreateHouseIn(
    val title: String,
    val location: Int,
    val areaSqMt: Double,
    val pricePerNight: Double,
    val description: String
)


/////////// Output ///////////
@Serializable
data class CreateHouseOut(val hid: Int)

@Serializable
data class GetHouseOut(
    val hid: Int,
    val title: String,
    val location: Int,
    val areaSqMt: Double,
    val pricePerNight: Double,
    val description: String,
    val owner: Int
)

@Serializable
data class PredictPriceOut(
    val predictedPricePerNight: Double,
    val predictedTotalPrice: Double
)
