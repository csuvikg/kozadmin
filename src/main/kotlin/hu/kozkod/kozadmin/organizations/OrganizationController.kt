package hu.kozkod.kozadmin.organizations

import hu.kozkod.kozadmin.organizations.model.OrganizationCreateCommand
import hu.kozkod.kozadmin.utils.buildLocationFromOrigin
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.organizations() {
    route("/v1") {
        route("/organizations") {
            val organizationController = OrganizationService()

            get("/") {
                val organizations = organizationController.listAll()
                call.respond(HttpStatusCode.OK, organizations)
            }

            post("/") {
                val organization = call.receive<OrganizationCreateCommand>()
                val newOrganization = organizationController.create(organization)
                val origin = this.context.request.origin
                val location = "${buildLocationFromOrigin(origin)}${newOrganization.id}"

                call.response.headers.append("Location", location)
                call.respond(HttpStatusCode.Created, newOrganization)
            }

            get("/{id}") {
                val id: Int = call.parameters["id"]!!.toInt()
                val organization = organizationController.getById(id)

                if (organization != null) {
                    call.respond(HttpStatusCode.OK, organization)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/{id}/users") {
                val id: Int = call.parameters["id"]!!.toInt()
                val users = organizationController.getUsers(id)

                if (users != null) {
                    call.respond(HttpStatusCode.OK, users)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/{id}/admins") {
                val id: Int = call.parameters["id"]!!.toInt()
                call.respond(HttpStatusCode.OK, organizationController.getAdmins(id))
            }
        }
    }
}