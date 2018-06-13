package hu.kozkod.kozadmin.organizations.model

data class OrganizationCreateCommand(val name: String,
                                     val url: String,
                                     val administratorIds: List<Int>)