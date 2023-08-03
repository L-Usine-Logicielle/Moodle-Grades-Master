<template>
  <div class="flex flex-col py-3 w-full border-opacity-10 space-y-3 px-6">

    <div class="flex justify-between">
      <div class="card w-1/4 bg-blue-700 shadow-xl">
        <div class="card-body">
          <h2 class="card-title">Création d'une instance Mootse Runner</h2>
          <p class="text-xs">Permet de déployer facilement une nouvelle instance Mootse Runner</p>
          <div class="card-actions justify-end pt-3">
            <button class="btn btn-primary" onclick="createModal.showModal()">
              <i class="fa-solid fa-plus"></i>
              Créer une instance
            </button>
          </div>
        </div>
      </div>
      <div class="card w-1/4 bg-blue-700 shadow-xl">
        <div class="card-body">
          <h2 class="card-title">Modification d'une instance Mootse Runner</h2>
          <p class="text-xs">Permet d'éditer la configuration d'une instance Mootse Runner</p>
          <div class="card-actions justify-end pt-3">
            <button class="btn btn-primary" onclick="editModal.showModal()">
              <i class="fa-solid fa-pen"></i>
              Modifier une instance
            </button>
          </div>
        </div>
      </div>
      <div class="card w-1/4 bg-blue-700 shadow-xl">
        <div class="card-body">
          <h2 class="card-title">Export d'une instance Mootse Runner</h2>
          <p class="text-xs">Permet d'exporter la configuration d'une instance Mootse Runner</p>
          <div class="card-actions justify-end pt-3">
            <button class="btn btn-primary" onclick="createModal.showModal()">
              <i class="fa-solid fa-download"></i>
              Exporter une instance
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="divider"></div>

    <dialog id="editModal" class="modal modal-bottom" @close="onEditModalClose">
      <form method="dialog" class="modal-box">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        <h3 class="font-bold text-lg">Modification d'une instance Mootse Runner</h3>

        <div class="form-control w-full content-center pt-2">
          <h5 class="text-primary-content mb-2">
            <i class="fa-solid fa-arrow-pointer mr-1"></i>
            Choix de la stack
          </h5>
          <select class="select select-bordered w-full" v-model="stackNameToEdit" @change="onStackSelected">
            <option disabled selected>Choisir une stack</option>
            <option v-for="option in stackOptions" :key="option">{{ option.Name }}</option>
          </select>
        </div>
        <template v-if="selectedEdit">
          <template v-if="loadingEdit">
            <div class="flex justify-center items-center h-full pt-4">
              <span class="loading loading-dots loading-lg"></span>
            </div>
          </template>
          <template v-else>
            <div class="divider"></div>
            <h5 class="text-primary-content">
              <i class="fa-solid fa-gear mr-1"></i>
              Configuration de la stack
            </h5>
            <div v-for="(value, key) in stackProperties" :key="key" class="form-control w-full pt-1">
              <label class="label">
                <span class="label-text">{{ key }}</span>
              </label>
              <input v-model="stackProperties[key]" type="text" class="input input-bordered w-full" />
            </div>
          </template>


        </template>
        <div class="modal-action">
          <button class="btn btn-success" :class="{ 'btn-disabled': isEditButtonDisabled}" @click="editStack">
            <i class="fa-solid fa-pen"></i>
            Modifier
          </button>
        </div>
      </form>
    </dialog>

    <dialog id="createModal" class="modal modal-bottom">
      <form method="dialog" class="modal-box">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        <h3 class="font-bold text-lg">Création d'une instance Mootse Runner</h3>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom de la stack</span>
            <span class="label-text-alt">Exemple : moodle-stack</span>
          </label>
          <input v-model="stackName" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom du runner</span>
            <span class="label-text-alt">Exemple : mootse-runner-exemple</span>
          </label>
          <input v-model="runnerName" type="text" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom de la base de données</span>
            <span class="label-text-alt">Exemple : mootse-mariadb-exemple</span>
          </label>
          <input v-model="dbName" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom du network Docker</span>
            <span class="label-text-alt">Exemple : mootse-network-exemple</span>
          </label>
          <input v-model="networkName" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom d'utilisateur Mootse</span>
          </label>
          <input v-model="mootseUsername" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Mot de passe Mootse</span>
          </label>
          <input v-model="mootsePassword" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="modal-action">
          <button class="btn btn-success" @click="createStack">Créer</button>
        </div>
      </form>
    </dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      stackName: "",
      runnerName: "",
      dbName: "",
      stackNameToEdit: 'Choisir une stack',
      networkName: "",
      mootseUsername: "",
      isDisabled: true,
      mootsePassword: "",
      loading: true,
      selectedEdit: false,
      loadingEdit: false,
      stackOptions: null,
      stackProperties: {
        networkName: "",
        mariaDbName: "",
        mariaDbContainerName: "",
        mariaDbImage: "",
        mariaDbRestartPolicy: "",
        mariaDbRootPassword: "",
        runnerName: "",
        runnerContainerName: "",
        runnerImage: "",
        runnerRestartPolicy: "",
        runnerScanInterval: "",
        runnerMootseUsername: "",
        runnerMootsePassword: "",
        runnerMailRecipients: "",
        runnerDiscordWebhookUrl: "",
        runnerDbHost: "",
        runnerDbUser: "",
        runnerDbPassword: "",
        runnerDbPort: "",
        runnerPromo: "",
      },
      stackPropertiesOriginal: {
        networkName: "",
        mariaDbName: "",
        mariaDbContainerName: "",
        mariaDbImage: "",
        mariaDbRestartPolicy: "",
        mariaDbRootPassword: "",
        runnerName: "",
        runnerContainerName: "",
        runnerImage: "",
        runnerRestartPolicy: "",
        runnerScanInterval: "",
        runnerMootseUsername: "",
        runnerMootsePassword: "",
        runnerMailRecipients: "",
        runnerDiscordWebhookUrl: "",
        runnerDbHost: "",
        runnerDbUser: "",
        runnerDbPassword: "",
        runnerDbPort: "",
        runnerPromo: "",
      },
    }
  },
  async beforeMount() {
    this.loading = true

    try {
      const stacksData = await $fetch('/api/portainer/stacks')
      this.stackOptions = stacksData
    } catch (error) {
      console.error('Error fetching data:', error)
    } finally {
      this.loading = false
    }

  },
  methods: {
    async editStack() {
      await $fetch('/api/stackupdate/update', {
        method: 'POST',
        query: {
          'stackName': this.stackNameToEdit,
          'networkName': this.stackProperties.networkName,
          'mariaDbName': this.stackProperties.mariaDbName,
          'mariaDbContainerName': this.stackProperties.mariaDbContainerName,
          'mariaDbImage': this.stackProperties.mariaDbImage,
          'mariaDbRestartPolicy': this.stackProperties.mariaDbRestartPolicy,
          'mariaDbRootPassword': this.stackProperties.mariaDbRootPassword,
          'runnerName': this.stackProperties.runnerName,
          'runnerContainerName': this.stackProperties.runnerContainerName,
          'runnerImage': this.stackProperties.runnerImage,
          'runnerRestartPolicy': this.stackProperties.runnerRestartPolicy,
          'runnerScanInterval': this.stackProperties.runnerScanInterval,
          'runnerMootseUsername': this.stackProperties.runnerMootseUsername,
          'runnerMootsePassword': this.stackProperties.runnerMootsePassword,
          'runnerMailRecipients': this.stackProperties.runnerMailRecipients,
          'runnerDiscordWebhookUrl': this.stackProperties.runnerDiscordWebhookUrl,
          'runnerDbHost': this.stackProperties.runnerDbHost,
          'runnerDbUser': this.stackProperties.runnerDbUser,
          'runnerDbPassword': this.stackProperties.runnerDbPassword,
          'runnerDbPort': this.stackProperties.runnerDbPort,
          'runnerPromo': this.stackProperties.runnerPromo,
        }
      }) 
    },
    async createStack() {
      await $fetch('/api/stack/create', {
        method: 'POST',
        query: {
          'stackName': this.stackName,
          'runnerName': this.runnerName,
          'dbName': this.dbName,
          'networkName': this.networkName,
          'mootseUsername': this.mootseUsername,
          'mootsePassword': this.mootsePassword,
        }
      })
    },
    onEditModalClose() {
      this.loadingEdit = false
      this.selectedEdit = false
      this.stackProperties = {
        networkName: "",
        mariaDbName: "",
        mariaDbContainerName: "",
        mariaDbImage: "",
        mariaDbRestartPolicy: "",
        mariaDbRootPassword: "",
        runnerName: "",
        runnerContainerName: "",
        runnerImage: "",
        runnerRestartPolicy: "",
        runnerScanInterval: "",
        runnerMootseUsername: "",
        runnerMootsePassword: "",
        runnerMailRecipients: "",
        runnerDiscordWebhookUrl: "",
        runnerDbHost: "",
        runnerDbUser: "",
        runnerDbPassword: "",
        runnerDbPort: "",
        runnerPromo: "",
      }
      this.stackPropertiesOriginal = {
        networkName: "",
        mariaDbName: "",
        mariaDbContainerName: "",
        mariaDbImage: "",
        mariaDbRestartPolicy: "",
        mariaDbRootPassword: "",
        runnerName: "",
        runnerContainerName: "",
        runnerImage: "",
        runnerRestartPolicy: "",
        runnerScanInterval: "",
        runnerMootseUsername: "",
        runnerMootsePassword: "",
        runnerMailRecipients: "",
        runnerDiscordWebhookUrl: "",
        runnerDbHost: "",
        runnerDbUser: "",
        runnerDbPassword: "",
        runnerDbPort: "",
        runnerPromo: "",
      }
      this.stackNameToEdit = 'Choisir une stack'
    },
    async onStackSelected() {
      this.selectedEdit = true
      this.loadingEdit = true

      const stackInformations = await $fetch(`/api/portainer/stacks/${this.stackNameToEdit}`)
      console.log(stackInformations)
      this.stackPropertiesOriginal.networkName = stackInformations.networkName
      this.stackPropertiesOriginal.mariaDbName = stackInformations.mariaDbName
      this.stackPropertiesOriginal.mariaDbContainerName = stackInformations.mariaDbContainerName
      this.stackPropertiesOriginal.mariaDbImage = stackInformations.mariaDbImage
      this.stackPropertiesOriginal.mariaDbRestartPolicy = stackInformations.mariaDbRestartPolicy
      this.stackPropertiesOriginal.mariaDbRootPassword = stackInformations.mariaDbRootPassword
      this.stackPropertiesOriginal.runnerName = stackInformations.runnerName
      this.stackPropertiesOriginal.runnerContainerName = stackInformations.runnerContainerName
      this.stackPropertiesOriginal.runnerImage = stackInformations.runnerImage
      this.stackPropertiesOriginal.runnerRestartPolicy = stackInformations.runnerRestartPolicy
      this.stackPropertiesOriginal.runnerScanInterval = stackInformations.runnerScanInterval
      this.stackPropertiesOriginal.runnerMootseUsername = stackInformations.runnerMootseUsername
      this.stackPropertiesOriginal.runnerMootsePassword = stackInformations.runnerMootsePassword
      this.stackPropertiesOriginal.runnerMailRecipients = stackInformations.runnerMailRecipients
      this.stackPropertiesOriginal.runnerDiscordWebhookUrl = stackInformations.runnerDiscordWebhookUrl
      this.stackPropertiesOriginal.runnerDbHost = stackInformations.runnerDbHost
      this.stackPropertiesOriginal.runnerDbUser = stackInformations.runnerDbUser
      this.stackPropertiesOriginal.runnerDbPassword = stackInformations.runnerDbPassword
      this.stackPropertiesOriginal.runnerDbPort = stackInformations.runnerDbPort
      this.stackPropertiesOriginal.runnerPromo = stackInformations.runnerPromo

      this.stackProperties.networkName = stackInformations.networkName
      this.stackProperties.mariaDbName = stackInformations.mariaDbName
      this.stackProperties.mariaDbContainerName = stackInformations.mariaDbContainerName
      this.stackProperties.mariaDbImage = stackInformations.mariaDbImage
      this.stackProperties.mariaDbRestartPolicy = stackInformations.mariaDbRestartPolicy
      this.stackProperties.mariaDbRootPassword = stackInformations.mariaDbRootPassword
      this.stackProperties.runnerName = stackInformations.runnerName
      this.stackProperties.runnerContainerName = stackInformations.runnerContainerName
      this.stackProperties.runnerImage = stackInformations.runnerImage
      this.stackProperties.runnerRestartPolicy = stackInformations.runnerRestartPolicy
      this.stackProperties.runnerScanInterval = stackInformations.runnerScanInterval
      this.stackProperties.runnerMootseUsername = stackInformations.runnerMootseUsername
      this.stackProperties.runnerMootsePassword = stackInformations.runnerMootsePassword
      this.stackProperties.runnerMailRecipients = stackInformations.runnerMailRecipients
      this.stackProperties.runnerDiscordWebhookUrl = stackInformations.runnerDiscordWebhookUrl
      this.stackProperties.runnerDbHost = stackInformations.runnerDbHost
      this.stackProperties.runnerDbUser = stackInformations.runnerDbUser
      this.stackProperties.runnerDbPassword = stackInformations.runnerDbPassword
      this.stackProperties.runnerDbPort = stackInformations.runnerDbPort
      this.stackProperties.runnerPromo = stackInformations.runnerPromo
      this.loadingEdit = false

    },
  },
  computed: {
  isEditButtonDisabled() {
    for (const key in this.stackProperties) {
      if (this.stackProperties[key] !== this.stackPropertiesOriginal[key]) {
        return false
      }
    }
    return true
  },
  }
}
</script>