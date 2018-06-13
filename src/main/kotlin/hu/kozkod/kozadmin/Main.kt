package hu.kozkod.kozadmin

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import hu.kozkod.kozadmin.organizations.OrganizationService
import hu.kozkod.kozadmin.organizations.model.OrganizationCreateCommand
import hu.kozkod.kozadmin.organizations.organizations
import hu.kozkod.kozadmin.utils.buildLocationFromOrigin
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import java.text.DateFormat

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8000) {
        install(Compression)
        install(CORS) {
            anyHost()
        }
        install(DefaultHeaders)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
                serializeNulls()
            }
        }
        initDB()
        install(Routing) {
            route("/api") {
                organizations()
            }
        }
    }.start(wait = true)
}