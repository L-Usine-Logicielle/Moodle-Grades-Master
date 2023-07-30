package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Network(
        @JsonProperty("EndpointID")
        val endpointID: String,

        @JsonProperty("Gateway")
        val gateway: String,

        @JsonProperty("IPAddress")
        val ipAddress: String,

        @JsonProperty("IPPrefixLen")
        val ipPrefixLen: Int,

        @JsonProperty("Links")
        val links: List<String>? = null,

        @JsonProperty("MacAddress")
        val macAddress: String,

        @JsonProperty("NetworkID")
        val networkID: String
)