package com.shashankbhat.splitbill.util.extension

import com.shashankbhat.splitbill.util.LatLong
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

fun LatLong.findDistance(latLng: LatLong): Double {

    val lon1 = longitude
    val lon2 = latLng.longitude
    val lat1 = latitude
    val lat2 = latLng.latitude

    val theta = lon1 - lon2

    var dist =
        sin(lat1.deg2rad()) * sin(lat2.deg2rad()) + cos(lat1.deg2rad()) * cos(
            lat2.deg2rad()
        ) * cos(theta.deg2rad())
    dist = acos(dist)
    dist = dist.rad2deg()
    dist *= 60 * 1.1515

    dist *= 1.609344
    return dist
}

fun Double.deg2rad(): Double {
    return this * Math.PI / 180.0
}

fun Double.rad2deg(): Double {
    return this * 180.0 / Math.PI
}