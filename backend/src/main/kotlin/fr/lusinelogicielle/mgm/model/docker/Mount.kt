package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Mount(
        @JsonProperty("Destination")
        val destination: String,

        @JsonProperty("Mode")
        val mode: String,

        @JsonProperty("Propagation")
        val propagation: String,

        @JsonProperty("RW")
        val rw: Boolean,

        @JsonProperty("Source")
        val source: String,

        @JsonProperty("Type")
        val type: String
)