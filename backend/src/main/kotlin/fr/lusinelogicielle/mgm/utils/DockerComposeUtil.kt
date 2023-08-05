package fr.lusinelogicielle.mgm.utils

import fr.lusinelogicielle.mgm.model.mootse.MootseStack

fun forgeDockerComposeFile(
        mootseStack: MootseStack,
        mailServer: String,
        mailPort: String,
        mailUsername: String,
        mailPassword: String
): String {
    return """
    version: '2'
    services:
      mootse-mariadb:
        container_name: ${mootseStack.database.name}
        image: ${mootseStack.database.containerImage}
        restart: always
        environment:
          - MYSQL_ROOT_PASSWORD=${mootseStack.database.password}
        healthcheck:
          test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'
        networks:
          - ${mootseStack.network}
      mootse-runner:
        container_name: ${mootseStack.runner.name}
        image: ${mootseStack.runner.containerImage}
        depends_on:
          mootse-mariadb:
            condition: service_healthy
        restart: always
        networks:
          - ${mootseStack.network}
        environment:
          - SCAN_INTERVAL=${mootseStack.runner.scanInterval}
          - MOOTSE_URL=${mootseStack.runner.url}
          - MOOTSE_USERNAME=${mootseStack.runner.username}
          - MOOTSE_PASSWORD=${mootseStack.runner.password}
          - MAIL_USERNAME=$mailUsername
          - MAIL_PASSWORD=$mailPassword
          - MAIL_SERVER=$mailServer
          - MAIL_PORT=$mailPort
          - MAIL_RECIPIENTS=${mootseStack.runner.recipients}
          - DISCORD_WEBHOOK_URL=${mootseStack.runner.discordWebhookUrl}
          - DB_HOST=${mootseStack.database.name}
          - DB_USER=root
          - DB_PASSWORD=${mootseStack.database.password}
          - DB_PORT=3306
          - PROMO=dbPromo
        healthcheck:
          test: 'python healthcheck.py'
    networks:
      ${mootseStack.network}:
        """.trimIndent()
}