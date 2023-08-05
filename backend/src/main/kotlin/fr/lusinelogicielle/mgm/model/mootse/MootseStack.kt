package fr.lusinelogicielle.mgm.model.mootse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MootseStack(
        val name: String,

        val network: String,

        val runner: MootseRunner,

        val database: MootseDatabase
)