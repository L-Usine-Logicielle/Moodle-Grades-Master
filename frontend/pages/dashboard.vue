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
                <div class="stat-title">Nombre de containers</div>
                <div class="stat-value">{{ containers.length }}</div>
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
                <div class="stat-title">TODO</div>
                <div class="stat-value">TODO</div>
                <div class="stat-desc">↗︎ TODO (TODO%)</div>
            </div>

            <div class="stat">
                <div class="stat-figure text-secondary">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                        class="inline-block w-8 h-8 stroke-current">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"></path>
                    </svg>
                </div>
                <div class="stat-title">TODO</div>
                <div class="stat-value">TODO</div>
                <div class="stat-desc">↗︎ TODO (TODO%)</div>
            </div>

        </div>
    </div>
    <div class="bg-blue-700 flex flex-col py-3 w-full border-opacity-50 space-y-1 px-6">
        <div class="overflow-x-auto content-right flex items-center justify-center">
            <table class="table table-zebra w-full h-full">
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
                        <th>
                            <label>
                                <input type="checkbox" class="checkbox" />
                            </label>
                        </th>
                        <td>
                            <div class="flex items-center space-x-3">
                                <div>
                                    <div class="font-bold">{{ container.Names[0] }}</div>
                                    <div class="text-sm opacity-50">{{ container.Labels['org.label-schema.description'] }}
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            {{ container.State }}
                            <br />
                        </td>
                        <td>{{ container.Status }}</td>
                        <th>
                            <button class="btn btn-secondary btn-xs">details</button>
                        </th>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>
</template>

<script>
export default {
    data() {
        return {
            containers: null,
        };
    },
    async beforeMount() {
        this.containers = await $fetch('/api/portainer/containers/2');
    },
    computed: {
        containersLog() {
            console.log(this.containers);
            return this.containers;
        },
        runningCount() {
            return this.containers ? this.containers.filter((obj) => obj.Status === 'running').length : 0;
        },
    },
};
</script>
