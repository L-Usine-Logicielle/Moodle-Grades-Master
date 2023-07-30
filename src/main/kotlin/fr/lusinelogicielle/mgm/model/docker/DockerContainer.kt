package fr.lusinelogicielle.mgm.model.docker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class DockerContainer(
        @JsonProperty("Command")
        val command: String,

        @JsonProperty("Created")
        val created: Long,

        @JsonProperty("HostConfig")
        val hostConfig: HostConfig,

        @JsonProperty("Id")
        val id: String,

        @JsonProperty("Image")
        val image: String,

        @JsonProperty("ImageID")
        val imageID: String,

        @JsonProperty("Labels")
        val labels: Map<String, String>,

        @JsonProperty("Mounts")
        val mounts: List<Mount>,

        @JsonProperty("Names")
        val names: List<String>,

        @JsonProperty("NetworkSettings")
        val networkSettings: NetworkSettings,

        @JsonProperty("Ports")
        val ports: List<Port>,

        @JsonProperty("State")
        val state: String,

        @JsonProperty("Status")
        val status: String
)