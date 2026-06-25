package pt.isel.ls.houses

import kotlin.math.roundToLong

/*
 Linear Regression in Kotlin
 Predict house price given area in m²
*/

// ===============================
// 1. Data structure
// ===============================

// Represents a house with area (m²) and price (euros)
data class HousePrediction(val area: Double, val price: Double)

// Original data with actual empty intervals
val houses = listOf(
    HousePrediction(35.0, 120000.0),
    HousePrediction(52.0, 155000.0),
    HousePrediction(70.0, 210000.0),
    HousePrediction(95.0, 260000.0),
    // gap: no data between 95 and 140
    HousePrediction(140.0, 340000.0),
    // large gap
    HousePrediction(220.0, 480000.0)
)

// ===============================
// 2. Normalization
// ===============================

// Scale of values to normalize
class Scale(values: List<Double>) {
    val min: Double = values.min()  // Minimum value of the scale
    val max: Double = values.max()  // Maximum value of the scale
    val delta: Double = max - min   // Length of the scale ONLY NEEDED TO CHANGE THIS to minus

    // Normalize a value to the [0, 1] interval
    fun normalize(value: Double) = (value - min) / delta

    // Denormalize a value back to the original scale
    fun denormalize(value: Double) = value * delta + min
}

// Represents normalized data + normalization scales
data class NormalizedData(
    val areas: Scale,   // Scale for areas
    val prices: Scale,  // Scale for prices
    val data: List<HousePrediction> // Normalized data
)

// Normalize data
fun List<HousePrediction>.normalize(): NormalizedData {
    val areas = Scale(map { it.area })
    val prices = Scale(map { it.price })
    return NormalizedData(areas, prices, data = map {
        HousePrediction(areas.normalize(it.area), prices.normalize(it.price))
    })
}

// ===============================
// 3. Model
// ===============================

// Model parameters: weight (w) and bias (b)
data class Params(val w: Double, val b: Double)

operator fun Params.plus(other: Params) = Params(w + other.w, b + other.b)

// Hypothesis function: y = weight x + bias
fun predict(x: Double, p: Params): Double = p.w * x + p.b

// ===============================
// 4. Helper functions
// ===============================

// Compute simple error
fun error(yPred: Double, yReal: Double): Double =
    yPred - yReal

// Compute gradients (derivatives of MSE)
fun gradients(x: Double, error: Double, n: Int) = Params(
    (2.0 / n) * error * x,  // weight delta
    (2.0 / n) * error,      // bias delta
)

// Update parameters using gradient descent
fun updateParams(p: Params, delta: Params, lr: Double) = Params(
    p.w - lr * delta.w,    // weight
    p.b - lr * delta.b,    // bias
)

// ===============================
// 5. Training
// ===============================

// Main training function
fun train(
    data: List<HousePrediction>, // normalized
    epochs: Int = 3000,
    lr: Double = 0.05
): Params {

    var params = Params(w = 0.0, b = 0.0)

    // Learning loop
    repeat(epochs) {
        val total = data.fold(Params(0.0, 0.0)) { p, house ->
            val yPred = predict(house.area, params)
            val e = error(yPred, house.price)
            p + gradients(house.area, e, data.size)
        }
        params = updateParams(params, total, lr)
    }
    return params
}

// ===============================
// 6. Execution
// ===============================

fun predictPrice(housesToPredict: List<HousePrediction>, predictArea: Double): Double{
    val (areas, prices, data) = housesToPredict.normalize()

    val params = train(data)

    fun getPriceForArea(area: Double): Double {
        val areaNorm = areas.normalize(area)
        val priceNorm = predict(areaNorm, params)
        return prices.denormalize(priceNorm)
    }

    return getPriceForArea(predictArea)
}

fun main() {
    // Normalize data
    val (areas, prices, data) = houses.normalize()

    // Train model
    val params = train(data)

    println("=== Trained model ===")
    println("weight = %.3f | bias = %.3f".format(params.w, params.b))

    fun getPriceForArea(area: Int): Long {
        val areaNorm = areas.normalize(area.toDouble())
        val priceNorm = predict(areaNorm, params)
        return prices.denormalize(priceNorm).roundToLong()
    }

    // Prediction for area inside a gap
    val area = 110
    val price = getPriceForArea(area)
    println("Predicted price for a house of $area m²: €$price")
}
