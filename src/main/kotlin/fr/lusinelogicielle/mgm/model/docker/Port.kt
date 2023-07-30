package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Port(
        @JsonProperty("IP")
        val ip: String? = null,

        @JsonProperty("PrivatePort")
        val privatePort: Int? = null,

        @JsonProperty("PublicPort")
        val publicPort: Int? = null,

        @JsonProperty("Type")
        val type: String? = null
)