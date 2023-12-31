<template>
    <div class="bg-base-200">
        <template v-if="refreshingData">
            <button class="btn btn-square hover:bg-base-250">
                <span class="loading loading-spinner"></span>
            </button>
        </template>
        <template v-else>
            <button class="btn btn-square hover:bg-base-250" @click="refreshData">
                <i class="fa-xl fa-solid fa-arrows-rotate"></i>
            </button>
        </template>
    </div>
    <div class="hero bg-base-200 py-6">
        <div class="hero-content text-center">
            <div class="max-w-md">
                <h1 class="text-4xl font-bold">Moodle Grades Master</h1>
                <div class="text-desc">Par <span class="text-blue-600 mr-1">L'Usine Logicielle</span>
                    <i class="fa-solid fa-industry"></i>
                </div>
                <p class="py-6">
                    Une console web de gestion des instances Moodle Grades Scraper dockerisées
                </p>
                <a role="button" class="btn bg-blue-700 text-white hover:bg-blue-900"
                    href="https://github.com/L-Usine-Logicielle/Moodle-Grades-Master" target="_blank"
                    rel="noopener noreferrer">
                    <i class="fa-brands fa-github"></i>
                    Code source du projet
                </a>
            </div>
        </div>
    </div>
    <div class="flex flex-col py-3 w-full border-opacity-10 space-y-3 px-6">
        <div class="stats shadow">
            <div class="stat">
                <div class="stat-figure text-secondary">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                        class="inline-block w-8 h-8 stroke-current">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
                <div class="stat-title">Containers</div>
                <template v-if="loading">
                    <span class="loading loading-dots loading-lg"></span>
                </template>
                <template v-else>
                    <div class="stat-value">{{ containersLength }}</div>
                </template>
            </div>

            <div class="stat">
                <div class="stat-figure text-secondary">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                        class="inline-block w-8 h-8 stroke-current">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4">
                        </path>
                    </svg>
                </div>

                <div class="stat-title">Containers en fonctionnement</div>
                <template v-if="loading">
                    <span class="loading loading-dots loading-lg"></span>
                </template>
                <template v-else>
                    <div class="stat-value">{{ runningCount }}</div>
                </template>
            </div>

            <div class="stat">
                <div class="stat-figure text-secondary">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                        class="inline-block w-8 h-8 stroke-current">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"></path>
                    </svg>
                </div>
                <div class="stat-title">Stacks Moodle Runner</div>
                <template v-if="loading">
                    <span class="loading loading-dots loading-lg"></span>
                </template>
                <template v-else>
                    <div class="stat-value">{{ stacksLength }}</div>
                    <template v-if="healthStatus == 0">
                        <div class="stat-desc">
                            <i class="fa-solid fa-check mr-1 text-success"></i>
                            {{ healthStatus }} stacks en erreur
                        </div>
                    </template>
                    <template v-else>
                        <div class="stat-desc">
                            <i class="fa-solid fa-triangle-exclamation mr-1 text-error"></i>
                            {{ healthStatus }} stacks en erreur
                        </div>
                    </template>
                </template>
            </div>

        </div>
    </div>
    <div class="flex flex-col py-3 w-full border-opacity-50 space-y-1 px-6 bg-base-300 py-4">
        <div class="collapse collapse-plus bg-base-200 mb-4">
            <input type="checkbox" class="peer" />
            <div class="collapse-title py-4">
                <i class="fa-brands fa-docker mr-1" style="color: #0db7ed;"></i>
                Afficher les containers
            </div>
            <div class="collapse-content">
                <div class="overflow-x-auto content-right flex items-center justify-center mb-2">
                    <template v-if="loading">
                        <span class="loading loading-dots loading-lg"></span>
                    </template>
                    <template v-else>
                        <template v-if="containers.length > 0">
                            <table class="table bg-neutral table-zebra w-full h-1/2">
                                <thead>
                                    <tr>
                                        <th>Nom</th>
                                        <th>État</th>
                                        <th>Statut</th>
                                        <th>Tags<i class="fa-solid fa-tags ml-2"></i></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(container, index) in containers" :key="index">
                                        <td>
                                            <div class="flex items-center space-x-3">
                                                <div>
                                                    <div class="font-bold">
                                                        {{ container.Names[0] }}
                                                    </div>
                                                    <!-- <template v-if="container.Names[0].startsWith('mootse-', 1)">
                                                        <br />
                                                        <span class="badge bg-blue-600 px-4 py-2 text-white badge-sm">Mootse
                                                            stack</span>
                                                    </template> -->
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            {{ container.State }}
                                            <br />
                                        </td>
                                        <td>{{ container.Status }}</td>
                                        <th>
                                            <button class="btn bg-blue-700 btn-xs"
                                                @click="showModal(container)">Détails</button>
                                        </th>
                                    </tr>
                                </tbody>
                            </table>
                        </template>
                        <template v-else>
                            Pas de données disponibles
                        </template>
                    </template>
                </div>
                <div class="py-2 my-2"></div>
            </div>
        </div>
        <div class="collapse collapse-plus bg-base-200">
            <input type="checkbox" class="peer" />
            <div class="collapse-title py-4">
                <i class="fa-solid fa-server mr-1" style="color: #0db7ed;"></i>
                Afficher les stacks
            </div>
            <div class="collapse-content">
                <div class="overflow-x-auto content-right flex items-center justify-center mb-2">
                    <template v-if="loading">
                        <span class="loading loading-dots loading-lg"></span>
                    </template>
                    <template v-else>
                        <template v-if="mootseStacks.length > 0">
                            <table class="table bg-neutral table-zebra w-full h-1/2">
                                <thead>
                                    <tr>
                                        <th>Actions</th>
                                        <th>Nom</th>
                                        <th>Réseau</th>
                                        <th>Intervalle de scan</th>
                                        <th>Export des métriques</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(mootseStack, index) in mootseStacks" :key="index">
                                        <th>
                                            <button class="btn btn-error btn-square btn-md mr-2"
                                                @click="deleteStackOpenModal(mootseStack)">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                            <button class="btn btn-info btn-square btn-md mr-2"
                                                @click="startStackOpenModal(mootseStack)">
                                                <i class="fa-solid fa-play"></i>
                                            </button>
                                            <button class="btn btn-warning btn-square btn-md"
                                                @click="stopStackOpenModal(mootseStack)">
                                                <i class="fa-solid fa-stop"></i>
                                            </button>
                                        </th>
                                        <td>
                                            <div class="flex items-center space-x-3">
                                                <div>
                                                    <div class="font-bold">
                                                        {{ mootseStack.name }}
                                                    </div>
                                                    <!-- <div class="text-sm opacity-50"> Test
                                                        {{ container.Labels['org.label-schema.description'] }}
                                                    </div> -->
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            {{ mootseStack.network }}
                                        </td>
                                        <td>
                                            {{ mootseStack.runner.scanInterval }} secondes
                                        </td>
                                        <td v-if="mootseStack.runner.mootseMasterUrl !== ''">
                                            Activé <i class="fa-solid fa-check" style="color: #58d07c;"></i>
                                        </td>
                                        <td v-else>
                                            Désactivé <i class="fa-solid fa-xmark" style="color: #ff0000;"></i>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </template>
                        <template v-else>
                            Pas de données disponibles
                        </template>
                    </template>
                </div>
                <div class="py-2 my-2"></div>
            </div>
        </div>

    </div>

    <input type="checkbox" id="my-modal" class="modal-toggle" />
    <div class="modal">
        <div class="modal-box">
            <h3 class="font-bold text-lg">Détails du container</h3>
            <p class="py-4">
                {{ modalContent }}
            </p>
            <div class="modal-action">
                <label for="my-modal" class="btn">Fermer</label>
            </div>
        </div>
    </div>

    <dialog v-if="deleteModalConfirmation" class="modal modal-open" @close="onCloseModalDeleteConfirmation">
        <form method="dialog" class="modal-box">
            <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                @click="onModalDialogDeleteConfirmation">✕</button>
            <h3 class="font-bold text-lg">Voulez vous vraiment supprimer cette stack ?</h3>
            <p class="py-4 text-white">L'opération n'est pas réversible et la stack '{{ stackToDelete[0].Name }}' sera
                définitivement supprimée</p>
            <div class="modal-action flex justify-between mt-4">
                <button class="btn" @click="onModalDialogDeleteConfirmation">Annuler</button>
                <button class="btn btn-error" @click="deleteStack">Supprimer</button>
            </div>
        </form>
    </dialog>

    <dialog v-if="startModalConfirmation" class="modal modal-open" @close="onCloseModalstartConfirmation">
        <form method="dialog" class="modal-box">
            <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                @click="onModalDialogStartConfirmation">✕</button>
            <h3 class="font-bold text-lg">Voulez vous vraiment démarrer cette stack ?</h3>
            <p class="py-4 text-white">La stack '{{ stackToStart[0].Name }}' sera
                démarrée après confirmation </p>
            <div class="modal-action flex justify-between mt-4">
                <button class="btn" @click="onModalDialogStartConfirmation">Annuler</button>
                <button class="btn btn-info" @click="startStack">Démarrer</button>
                <!-- <button class="btn btn-error" @click="pauseStack">Supprimer</button> -->
            </div>
        </form>
    </dialog>

    <dialog v-if="stopModalConfirmation" class="modal modal-open" @close="onCloseModalstopConfirmation">
        <form method="dialog" class="modal-box">
            <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                @click="onModalDialogStopConfirmation">✕</button>
            <h3 class="font-bold text-lg">Voulez vous vraiment stopper cette stack ?</h3>
            <p class="py-4 text-white">La stack '{{ stackToStop[0].Name }}' sera
                stoppée après confirmation </p>
            <div class="modal-action flex justify-between mt-4">
                <button class="btn" @click="onModalDialogStopConfirmation">Annuler</button>
                <button class="btn btn-warning" @click="stopStack">Stopper</button>
                <!-- <button class="btn btn-error" @click="pauseStack">Supprimer</button> -->
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
</template>

<script>
export default {
    data() {
        return {
            containers: null,
            stacks: null,
            mootseStacks: null,
            modalContent: "",
            loading: true,
            loadingModal: false,
            refreshingData: false,
            successModal: false,
            errorModal: false,
            message: "",
            deleteModalConfirmation: false,
            startModalConfirmation: false,
            stopModalConfirmation: false,
            stackToDelete: null,
            stackToStop: null,
            stackToStart: null
        }
    },
    methods: {

        async fetchData() {
            try {
                const containersData = await $fetch('/api/containers')
                this.containers = containersData
            } catch (error) {
                console.error('Error fetching data:', error)
            }

            try {
                const mootseStackData = await $fetch('/api/mootse')
                this.mootseStacks = mootseStackData
            } catch (error) {
                console.error('Error fetching data:', error)
            }

            try {
                const stacksData = await $fetch('/api/stacks')
                this.stacks = stacksData
            } catch (error) {
                console.error('Error fetching data:', error)
            } finally {
                this.loading = false;
            }
        },

        async refreshData() {
            this.refreshingData = true
            await this.fetchData()
            this.refreshingData = false
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

        showModal(container) {
            const modal = document.getElementById('my-modal')
            this.modalContent = container.Names[0]
            modal.checked = true
        },

        async deleteStack() {
            this.deleteModalConfirmation = false
            this.loadingModal = true

            try {
                await $fetch(`/api/stacks/${this.stackToDelete[0].Id}`, {
                    method: 'DELETE',
                    headers: { 'Access-Control-Allow-Methods': '*' }
                })
                this.message = "Stack supprimée !"
                this.fetchData()
                this.loadingModal = false
                this.successModal = true
            } catch (error) {
                console.error("Unable to delete stack:", error)
                this.message = `Impossible de supprimer la stack ${this.stackToDelete[0].Name}`
                this.loadingModal = false
                this.errorModal = true
            }

        },

        deleteStackOpenModal(stackToDelete) {
            const stackIdList = this.stacks.filter((obj) => obj.Name === stackToDelete.name)
            this.stackToDelete = stackIdList
            this.deleteModalConfirmation = true
        },

        onCloseModalDeleteConfirmation() {
            this.stackToDelete = false
            this.deleteModalConfirmation = false
        },

        onModalDialogDeleteConfirmation() {
            this.deleteModalConfirmation = false
        },

        onCloseModalStartConfirmation() {
            this.stackToDelete = false
            this.startModalConfirmation = false
        },

        startStackOpenModal(stackToStart) {
            const stackIdList = this.stacks.filter((obj) => obj.Name === stackToStart.name)
            this.stackToStart = stackIdList
            this.startModalConfirmation = true
        },

        onModalDialogStartConfirmation() {
            this.startModalConfirmation = false
        },

        async startStack() {
            this.startModalConfirmation = false
            this.loadingModal = true

            try {
                await $fetch(`/api/stacks/start/${this.stackToStart[0].Id}`, {
                    method: 'POST',
                })
                this.message = "Stack démarrée !"
                this.fetchData()
                this.loadingModal = false
                this.successModal = true
            } catch (error) {
                console.error("Unable to start stack:", error)
                this.message = `Impossible de démarrer la stack ${this.stackToStart[0].Name}`
                this.loadingModal = false
                this.errorModal = true
            }

        },

        onCloseModalStopConfirmation() {
            this.stackToDelete = false
            this.stopModalConfirmation = false
        },

        stopStackOpenModal(stackToStop) {
            const stackIdList = this.stacks.filter((obj) => obj.Name === stackToStop.name)
            this.stackToStop = stackIdList
            this.stopModalConfirmation = true
        },

        onModalDialogStopConfirmation() {
            this.stopModalConfirmation = false
        },

        async stopStack() {
            this.stopModalConfirmation = false
            this.loadingModal = true

            try {
                await $fetch(`/api/stacks/stop/${this.stackToStop[0].Id}`, {
                    method: 'POST',
                })
                this.message = "Stack stoppée !"
                this.fetchData()
                this.loadingModal = false
                this.successModal = true
            } catch (error) {
                console.error("Unable to stop stack:", error)
                this.message = `Impossible de stopper la stack ${this.stackToStart[0].Name}`
                this.loadingModal = false
                this.errorModal = true
            }
        }
    },
    async beforeMount() {
        this.loading = true;

        this.fetchData()
    },
    computed: {
        containersLength() {
            return this.containers ? this.containers.length : 0
        },
        stacksLength() {
            return this.stacks ? this.stacks.filter((obj) => obj.Name.startsWith('mootse')).length : 0
        },
        runningCount() {
            return this.containers
                ? this.containers.filter((obj) => obj.State === 'running').length
                : 0
        },
        healthStatus() {
            return this.containers.filter((obj) => obj.Names[0].startsWith('/mootse') && obj.State === 'running' && obj.Status.endsWith('(healthy)')).length == this.stacks.filter((obj) => obj.Name.startsWith('mootse')).length * 2
                ? 0
                : this.stacks.filter((obj) => obj.Name.startsWith('mootse')).length * 2 - this.containers.filter((obj) => obj.Names[0].startsWith('/mootse') && obj.State === 'running' && obj.Status.endsWith('(healthy)')).length
        }
    },
};
</script>