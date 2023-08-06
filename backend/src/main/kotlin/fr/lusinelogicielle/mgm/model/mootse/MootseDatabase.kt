package fr.lusinelogicielle.mgm.model.mootse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MootseDatabase(
    val name: String,

    var containerImage: String = "",

    var password: String = "",

)
