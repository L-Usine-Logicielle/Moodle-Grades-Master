package fr.lusinelogicielle.mgm.model.mootse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MootseRunner(
    val name: String,

    val containerImage: String,

    var url: String = "https://mootse.telecom-st-etienne.fr",

    var username: String = "",

    var password: String = "",

    var recipients: String = "",

    var discordWebhookUrl: String = "",

    var scanInterval: Int = 20,

    var mootseMasterUrl: String = "http://10.243.103.188:8090",
)
