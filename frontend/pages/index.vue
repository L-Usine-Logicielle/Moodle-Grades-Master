<template>
    <div class="hero bg-base-200 py-6">
        <div class="hero-content text-center">
            <div class="max-w-md">
                <h1 class="text-4xl font-bold">Moodle Grades Master</h1>
                <div class="text-desc">Par <span class="text-blue-600 mr-1">L'Usine Logicielle</span>
                    <i class="fa-solid fa-industry"></i>
                </div>
                <p class="py-6">
                    Une console web de gestion des instances Moodle Grades Master dockerisées
                </p>
                <a role="button" class="btn btn-secondary" href="https://github.com/L-Usine-Logicielle/Moodle-Grades-Master"
                    target="_blank" rel="noopener noreferrer">
                    <i class="fa-brands fa-github"></i>
                    Code source du projet
                </a>
            </div>
        </div>
    </div>
    <div class="flex flex-col py-3 w-full border-opacity-10 bg-primary space-y-3 px-6">
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
                <div class="stat-title">Stacks Mootse Runner</div>
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
                        <table class="table bg-neutral table-zebra w-full h-1/2">
                            <thead>
                                <tr>
                                    <th>
                                    </th>
                                    <th>Nom</th>
                                    <th>État</th>
                                    <th>Statut</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(container, index) in containers" :key="index">
                                    <template v-if="container.Names[0].startsWith('mootse-', 1)">
                                        <th>
                                            <button class="btn btn-secondary btn-square btn-md">
                                                <i class="fa-solid fa-pencil"></i>
                                            </button>
                                        </th>
                                    </template>
                                    <template v-else>
                                        <th></th>
                                    </template>
                                    <td>
                                        <div class="flex items-center space-x-3">
                                            <div>
                                                <div class="font-bold">
                                                    {{ container.Names[0] }}
                                                </div>
                                                <div class="text-sm opacity-50"> Test
                                                    <!-- {{ container.Labels['org.label-schema.description'] }} -->
                                                </div>
                                                <template v-if="container.Names[0].startsWith('mootse-', 1)">
                                                    <br />
                                                    <span class="badge badge-primary badge-sm">Mootse stack</span>
                                                </template>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        {{ container.State }}
                                        <br />
                                    </td>
                                    <td>{{ container.Status }}</td>
                                    <th>
                                        <button class="btn btn-secondary btn-xs"
                                            @click="showModal(container)">Détails</button>
                                    </th>
                                </tr>
                            </tbody>
                        </table>
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
                        <table class="table bg-neutral table-zebra w-full h-1/2">
                            <thead>
                                <tr>
                                    <th>
                                    </th>
                                    <th>Nom</th>
                                    <th>Réseau</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(mootseStack, index) in mootseStacks" :key="index">
                                    <th>
                                        <button class="btn btn-error btn-square btn-md"
                                            @click="deleteStackOpenModal(mootseStack)">
                                            <i class="fa-solid fa-trash"></i>
                                        </button>
                                    </th>
                                    <td>
                                        <div class="flex items-center space-x-3">
                                            <div>
                                                <div class="font-bold">
                                                    {{ mootseStack.name }}
                                                </div>
                                                <div class="text-sm opacity-50"> Test
                                                    <!-- {{ container.Labels['org.label-schema.description'] }} -->
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        {{ mootseStack.network }}
                                    </td>

                                </tr>
                            </tbody>
                        </table>
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
            successModal: false,
            errorModal: false,
            message: "",
            deleteModalConfirmation: false,
            stackToDelete: null
        }
    },
    methods: {

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
            console.log(this.stackToDelete)
            this.deleteModalConfirmation = false
            this.loadingModal = true

            try {
                await $fetch(`/api/stacks/${this.stackToDelete[0].Id}`, {
                    method: 'DELETE',
                    headers: {'Access-Control-Allow-Methods': '*'}
                })
                this.message = "Stack supprimée !"
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
        }
    },
    async beforeMount() {
        this.loading = true;

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
    computed: {
        containersLength() {
            return this.containers ? this.containers.length : 0
        },
        stacksLength() {
            return this.stacks ? this.stacks.length : 0
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