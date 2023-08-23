<template>
  <div class="px-3">
    <div class="py-3">
      <div class="card rounded-box w-full mb-2 md:mb-0">
        <div class="card bg-base-300 shadow-xl">
          <div class="card-body">
            <h2 class="card-title">Création d'une instance Moodle Grades Scraper</h2>
            <p class="text-xs">Permet de déployer facilement une nouvelle instance Moodle Grades Scraper</p>
            <div class="card-actions justify-end pt-3">
              <button class="text-white btn bg-blue-700 hover:bg-blue-900" onclick="createModal.showModal()">
                <i class="fa-solid fa-plus"></i>
                Créer une instance
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="card rounded-box w-full mb-2 md:mb-0 mt-4">
        <div class="card bg-base-300 shadow-xl">
          <div class="card-body">
            <h2 class="card-title">Modification d'une instance Moodle Grades Scraper</h2>
            <p class="text-xs">Permet d'éditer la configuration d'une instance Moodle Grades Scraper</p>
            <div class="card-actions justify-end pt-3">
              <button class="text-white btn bg-blue-700 hover:bg-blue-900" onclick="editModal.showModal()">
                <i class="fa-solid fa-pen"></i>
                Modifier une instance
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>


    <dialog id="editModal" class="modal modal-bottom" @close="onEditModalClose">
      <form method="dialog" class="modal-box">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        <h3 class="font-bold text-lg">Modification d'une instance Moodle Grades Master</h3>

        <div class="form-control w-full content-center pt-2">
          <h5 class="text-primary-content mb-2">
            <i class="fa-solid fa-arrow-pointer mr-1"></i>
            Choix de la stack
          </h5>
          <select class="select select-bordered w-full" v-model="selectedStackName" @change="onStackSelected">
            <option disabled selected>Choisir une stack</option>
            <option v-for="stack in stacks" :key="stack">{{ stack.name }}</option>
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

            <div class="collapse collapse-plus bg-base-200 my-3">
              <input type="checkbox" class="peer" />
              <div class="collapse-title py-4">
                <i class="fa-solid fa-wifi"></i>
                Options réseaux
              </div>
              <div class="collapse-content">
                <div class="form-control w-full pt-1">
                  <label class="label">
                    <span class="label-text">Nom du réseau</span>
                  </label>
                  <input v-model="stackToEdit['network']" type="text" class="input input-bordered w-full" />
                </div>
              </div>
            </div>
            <div class="collapse collapse-plus bg-base-200 my-3">
              <input type="checkbox" class="peer" />
              <div class="collapse-title py-4">
                <i class="fa-solid fa-server"></i>
                Options du runner
              </div>
              <div class="collapse-content">
                <div v-for="(value, key) in stackToEdit.runner" :key="key" class="form-control w-full pt-1">
                  <label class="label">
                    <span class="label-text">{{ key }}</span>
                  </label>
                  <input v-model="stackToEdit.runner[key]" type="text" class="input input-bordered w-full" />
                </div>
              </div>
            </div>

            <div class="collapse collapse-plus bg-base-200 my-3">
              <input type="checkbox" class="peer" />
              <div class="collapse-title py-4">
                <i class="fa-solid fa-database"></i>
                Options de la base de données
              </div>
              <div class="collapse-content">
                <div v-for="(value, key) in stackToEdit.database" :key="key" class="form-control w-full pt-1">
                  <label class="label">
                    <span class="label-text">{{ key }}</span>
                  </label>
                  <input v-model="stackToEdit.database[key]" type="text" class="input input-bordered w-full" />
                </div>
              </div>
            </div>
          </template>

        </template>
        <div class="modal-action">
          <!-- <button class="btn btn-success btn-disabled" @click="editStack"> -->
          <button class="btn btn-success" :class="{ 'btn-disabled': editDisabled }" @click="editStack">
            <i class="fa-solid fa-pen"></i>
            Modifier
          </button>
        </div>
      </form>
    </dialog>

    <dialog id="createModal" class="modal modal-bottom" @close="onCreateModalClose">
      <form method="dialog" class="modal-box">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        <h3 class="font-bold text-lg">Création d'une instance Moodle Grades Master</h3>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom de la stack</span>
            <span class="label-text-alt">Exemple : stack-demo</span>
          </label>
          <div class="join">
            <span class="btn join-item rounded-l-full w-1/3">mootse-</span>
            <input v-model="stack.name" class="input input-bordered join-item w-2/3" type="text" />
          </div>
        </div>


        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom du runner</span>
            <span class="label-text-alt">Exemple : runner-demo</span>
          </label>
          <div class="join">
            <span class="btn join-item rounded-l-full w-1/3">mootse-</span>
            <input v-model="stack.runner.name" class="input input-bordered join-item w-2/3" type="text" />
          </div>
        </div>

        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom de la base de données</span>
            <span class="label-text-alt">Exemple : mariadb-demo</span>
          </label>
          <div class="join">
            <span class="btn join-item rounded-l-full w-1/3">mootse-</span>
            <input v-model="stack.database.name" class="input input-bordered join-item w-2/3" type="text" />
          </div>
        </div>

        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom du network Docker</span>
            <span class="label-text-alt">Exemple : network-demo</span>
          </label>
          <input v-model="stack.network" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Nom d'utilisateur Moodle</span>
          </label>
          <input v-model="stack.runner.username" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="form-control w-full content-center pt-2">
          <label class="label">
            <span class="label-text">Mot de passe Moodle</span>
          </label>
          <input v-model="stack.runner.password" type="text" placeholder="" class="input input-bordered w-full" />
        </div>
        <div class="modal-action">
          <button class="btn btn-success" @click="createStack">Créer</button>
        </div>
      </form>
    </dialog>

    <dialog v-if="successModal" class="modal modal-open" @close="onCloseModalDialog">
      <form method="dialog" class="modal-box">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" @click="onModalDialog">✕</button>
        <h3 class="font-bold text-lg text-success">Opération réussie</h3>
        <p class="py-4 text-white">{{ message }}</p>
      </form>
    </dialog>

    <dialog v-if="errorModal" class="modal modal-open" @close="onCloseModalDialog">
      <form method="dialog" class="modal-box">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" @click="onModalDialog">✕</button>
        <h3 class="font-bold text-lg text-error">Opération échouée</h3>
        <p class="py-4 text-white">{{ message }}</p>
      </form>
    </dialog>

    <dialog v-if="loadingModal" class="modal modal-open">
      <form method="dialog" class="modal-box">
        <h3 class="font-bold text-lg text-warning">Opération en cours...</h3>
        <span class="loading loading-dots loading-lg"></span>
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
      networkName: "",
      mootseUsername: "",
      successModal: false,
      errorModal: false,
      loadingModal: false,
      message: "",
      isDisabled: true,
      mootsePassword: "",
      loading: true,
      selectedEdit: false,
      selectedStackName: 'Choisir une stack',
      loadingEdit: false,
      stackOptions: null,
      allStacksNoFiltered: null,
      stacks: null,
      stackToEdit: null,
      stackToEditOriginal: null,
      editDisabled: true,
      stack: {
        name: "",
        network: "",
        runner: {
          name: "",
          containerImage: "ghcr.io/lucasperfeito/moodle-grades-scraper:0.0.2",
          url: "https://mootse.telecom-st-etienne.fr",
          username: "",
          password: "",
          recipients: "",
          discordWebhookUrl: "",
          scanInterval: 120,
        },
        database: {
          name: "",
          containerImage: "docker.io/library/mariadb:10.4",
          password: "mypass",
        },
      }
    }
  },

  async beforeMount() {
    this.loading = true
    await this.fetchData()
  },

  methods: {
    async fetchData() {
      try {
        const stacksDataNoFiltered = await $fetch('/api/stacks')
        this.allStacksNoFiltered = stacksDataNoFiltered
      } catch (error) {
        console.error('Error fetching data:', error)
      }

      try {
        const stacksData = await $fetch('/api/mootse')
        this.stacks = stacksData
      } catch (error) {
        console.error('Error fetching data:', error)
      } finally {
        this.loading = false
      }
    },

    async editStack() {
      this.loadingModal = true

      try {
        const stackIdList = this.allStacksNoFiltered.filter((obj) => obj.Name === this.stackToEditOriginal.name)
        const stackId = stackIdList[0].Id

        const response = await $fetch(`/api/mootse/${stackId}`, {
          method: 'PUT',
          body: this.stackToEdit
        })
        this.message = "Stack mise à jour !"
        this.fetchData()
        this.loadingModal = false
        this.successModal = true
      } catch (error) {
        console.error(`Unable to update stack :`, error)
        this.message = `Impossible de mettre à jour la stack (${error})`
        this.loadingModal = false
        this.errorModal = true
      }

    },

    async createStack() {
      this.loadingModal = true
      this.stack.name = "mootse-" + this.stack.name
      this.stack.runner.name = "mootse-" + this.stack.runner.name
      this.stack.database.name = "mootse-" + this.stack.database.name
      try {

        const reponse = await $fetch('/api/mootse', {
          method: 'POST',
          body: this.stack
        })
        this.message = "Stack créée !"
        this.fetchData()
        this.loadingModal = false
        this.successModal = true
      } catch (error) {
        console.error('Unable to create stack', error)
        this.message = `Impossible de créer la stack (${error})`
        this.loadingModal = false
        this.errorModal = true
      }
    },

    onCloseModalDialog() {
      this.message = ""
      this.errorModal = false
      this.successModal = false
    },

    onModalDialog() {
      this.errorModal = false
      this.successModal = false
    },

    onCreateModalClose() {
      this.stack = {
        name: "",
        network: "",
        runner: {
          name: "",
          containerImage: "ghcr.io/lucasperfeito/moodle-grades-scraper:0.0.2",
          url: "https://mootse.telecom-st-etienne.fr",
          username: "",
          password: "",
          recipients: "",
          discordWebhookUrl: "",
          scanInterval: 120,
        },
        database: {
          name: "",
          containerImage: "docker.io/library/mariadb:10.4",
          password: "mypass",
        },
      }
    },

    onEditModalClose() {
      this.selectedStackName = 'Choisir une stack'
      this.stackToEditOriginal = null
      this.stackToEdit = null
      this.editDisabled = true
      this.loadingEdit = false
      this.selectedEdit = false
    },

    async onStackSelected() {
      this.selectedEdit = true
      this.loadingEdit = true
      const stackIdList = this.allStacksNoFiltered.filter((obj) => obj.Name === this.selectedStackName)
      const stackInformations = await $fetch(`/api/mootse/${stackIdList[0].Id}`)
      this.stackToEdit = JSON.parse(JSON.stringify(stackInformations))
      this.stackToEditOriginal = JSON.parse(JSON.stringify(stackInformations))
      this.editDisabled = false
      this.loadingEdit = false
    },
  },
}
</script>