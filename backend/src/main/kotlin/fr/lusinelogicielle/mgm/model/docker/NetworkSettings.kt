package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class NetworkSettings(
        @JsonProperty("Networks")
        val networks: Map<String, Network>
)