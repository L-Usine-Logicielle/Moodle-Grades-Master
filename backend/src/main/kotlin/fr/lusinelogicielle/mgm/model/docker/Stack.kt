package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Stack(
        @JsonProperty("Id")
        val id: Int,

        @JsonProperty("Name")
        val name: String,

        @JsonProperty("Type")
        val type: Int,

        @JsonProperty("EndpointId")
        val endpointId: Int,

        @JsonIgnoreProperties
        var fileContent: FileContentResponse?
)