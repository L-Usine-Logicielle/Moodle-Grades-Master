<template>
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
                </template>
            </div>

        </div>
    </div>
    <div class="flex flex-col py-3 w-full border-opacity-50 space-y-1 px-6">
        <div class="overflow-x-auto content-right flex items-center justify-center">
            <template v-if="loading">
                <span class="loading loading-dots loading-lg"></span>
            </template>
            <template v-else>
                <table class="table table-zebra w-full h-1/2">
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
                                        <div class="text-sm opacity-50">
                                            {{ container.Labels['org.label-schema.description'] }}
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
                                <button class="btn btn-xs" @click="showModal(container)">Détails</button>
                            </th>
                        </tr>
                    </tbody>
                </table>
            </template>
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
</template>

<script>
export default {
    data() {
        return {
            containers: null,
            stacks: null,
            modalContent: "",
            loading: true,
        }
    },
    methods: {
        showModal(container) {
            const modal = document.getElementById('my-modal')
            this.modalContent = container.Names[0]
            modal.checked = true
        },
    },
    async beforeMount() {
        this.loading = true;

        try {
            const containersData = await $fetch('/api/portainer/containers/2')
            this.containers = containersData
        } catch (error) {
            console.error('Error fetching data:', error)
        }

        try {
            const stacksData = await $fetch('/api/portainer/stacks')
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
    },
};
</script>