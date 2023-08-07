package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Container(
    @JsonProperty("Id")
    val id: String,

    @JsonProperty("Created")
    val created: String,

    @JsonProperty("Image")
    val containerImage: String,

    @JsonProperty("Names")
    val names: List<String>,

    @JsonProperty("State")
    val state: String,

    @JsonProperty("Status")
    val status: String,
)
