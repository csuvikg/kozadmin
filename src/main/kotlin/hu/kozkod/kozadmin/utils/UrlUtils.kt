package hu.kozkod.kozadmin.utils

import io.ktor.http.RequestConnectionPoint

fun buildLocationFromOrigin(origin: RequestConnectionPoint): String {
    val hasTrailingSlash = origin.uri.endsWith("/")
    return "${origin.scheme}://${origin.host}:${origin.port}${origin.uri}${if (hasTrailingSlash) "" else "/"}"
}