package fr.lusinelogicielle.mgm.controller.mootse

data class MootseInfo(
        val networkName: String,
        val mariaDbName: Any?,
        val mariaDbContainerName: String,
        val mariaDbImage: String,
        val mariaDbRestartPolicy: String,
        val mariaDbRootPassword: String,
        val runnerName: Any?,
        val runnerContainerName: String,
        val runnerImage: String,
        val runnerRestartPolicy: String,
        val runnerScanInterval: String,
        val runnerMootseUsername: String,
        val runnerMootsePassword: String,
        val runnerMailRecipients: String,
        val runnerDiscordWebhookUrl: String,
        val runnerDbHost: String,
        val runnerDbUser: String,
        val runnerDbPassword: String,
        val runnerDbPort: String,
        val runnerPromo: String
)