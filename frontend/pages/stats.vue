<template>
  <div class="px-3">
    <div class="bg-transparent">
      <template v-if="refreshingData">
        <button class="btn btn-square hover:bg-base-200">
          <span class="loading loading-spinner"></span>
        </button>
      </template>
      <template v-else>
        <button class="btn bg-transparent hover:bg-base-100" @click="refreshData">
          <i class="fa-xl fa-solid fa-arrows-rotate"></i>
        </button>
      </template>
    </div>
    <div class="py-3">
      <div class="stats shadow w-full text-center bg-base-300">
        <div class="stat">
          <div class="stat-figure text-blue-600">
            <i class="fa-solid fa-tower-broadcast fa-2xl"></i>
          </div>
          <div class="stat-title">Nombre total de connexions à Moodle</div>
          <div class="stat-value">{{ moodleAccessCount }}</div>
          <div class="stat-desc">Uniquement les connexions réussies</div>
        </div>
      </div>
      <div class="mt-4 stats shadow w-full text-center bg-base-300">
        <div class="stat">
          <div class="stat-figure text-blue-600">
            <i class="fa-solid fa-envelope-circle-check fa-2xl"></i>
          </div>
          <div class="stat-title">Nombre total de mails envoyés</div>
          <div class="stat-value">{{ mailSentCount }}</div>
        </div>
      </div>
      <div class="mt-4 stats shadow w-full text-center bg-base-300">
        <div class="stat">
          <div class="stat-figure text-blue-600">
            <i class="fa-brands fa-discord fa-2xl"></i>
          </div>
          <div class="stat-title">Nombre total d'appels de webhook Discord</div>
          <div class="stat-value">{{ discordWebhookCount }}</div>
        </div>
      </div>
      <div v-for="(value, key) in topicMetrics" :key="key" class="collapse collapse-plus bg-base-300 mt-4">
        <input type="checkbox" />
        <div class="collapse-title text-xl text-center font-medium">
          <i class="fa-solid fa-gauge-high mr-2"></i>
          Afficher les métriques de {{ key }}
        </div>
        <div class="collapse-content">
          <div v-for="obj in value" :key="obj.id">
              <div class="stat text-center">
                <div class="stat-title">{{ extractPrefix(obj.name) }}</div>
                <div class="stat-value">{{ obj.value }}</div>
              </div>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      loading: true,
      metrics: null,
      refreshingData: false,
    }
  },

  async beforeMount() {
    this.loading = true
    await this.fetchData()
  },

  computed: {
    moodleAccessCount() {
      if (this.metrics) {
        return this.metrics
          .filter(item => item.name.endsWith("moodle-access-count"))
          .reduce((accumulator, currentItem) => accumulator + currentItem.value, 0)
      }
      return 0
    },

    discordWebhookCount() {
      if (this.metrics) {
        return this.metrics
          .filter(item => item.name.endsWith("discord-webhook-sent-count"))
          .reduce((accumulator, currentItem) => accumulator + currentItem.value, 0)
      }
      return 0
    },

    mailSentCount() {
      if (this.metrics) {
        return this.metrics
          .filter(item => item.name.endsWith("mail-sent-count"))
          .reduce((accumulator, currentItem) => accumulator + currentItem.value, 0)
      }
      return 0
    },

    topicMetrics() {
      if (this.metrics) {
        const filteredMetrics = this.metrics.filter(item =>
          !item.name.endsWith("moodle-access-count") &&
          !item.name.endsWith("discord-webhook-sent-count") &&
          !item.name.endsWith("mail-sent-count")
        )
        const groupedData = {}

        filteredMetrics.forEach(item => {
          const prefix = item.name.split('--')[0]
          if (!groupedData[prefix]) {

            groupedData[prefix] = []
          }

          groupedData[prefix].push(item)
        })
        return groupedData
      }
      return null
    }
  },

  methods: {
    async fetchData() {
      try {
        const metrics = await $fetch('/api/metrics')
        this.metrics = metrics
      } catch (error) {
        console.error('Error fetching data:', error)
      } finally {
        this.loading = false
      }
    },

    async refreshData() {
      this.refreshingData = true
      await this.fetchData()
      this.refreshingData = false
    },

    extractPrefix(name) {
      const parts = name.split('--')
      return parts.length > 1 ? parts.slice(1).join('--') : name;
    }
  },
}
</script>