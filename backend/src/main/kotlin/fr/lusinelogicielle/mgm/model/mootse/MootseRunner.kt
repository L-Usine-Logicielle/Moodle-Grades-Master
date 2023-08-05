package fr.lusinelogicielle.mgm.model.mootse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.stereotype.Component

@JsonIgnoreProperties(ignoreUnknown = true)
data class MootseRunner(
        val name: String,

        val containerImage: String,

        val url: String = "https://mootse.telecom-st-etienne.fr",

        val username: String,

        val password: String,

        val recipients: String = "",

        val discordWebhookUrl: String = "",

        val scanInterval: Int = 20,
)