<template>
    <div class="navbar bg-base-100 px-6">
        <div class="flex-1">
            <a class="btn btn-ghost normal-case text-xl">
                <i class="fa-solid fa-graduation-cap px-3"></i>
                <div class="m-auto">
                    Moodle Grades Master
                    <p class="text-sm text-left">
                        Par <span class="text-blue-600 mr-1">L'Usine Logicielle</span>
                        <i class="fa-solid fa-industry"></i>
                    </p>
                </div>
            </a>

        </div>
        <div class="flex-none">
            <div class="dropdown dropdown-end">
                <ul class="menu menu-horizontal px-1">
                    <div class="dropdown dropdown-end">
                        <ul class="menu menu-horizontal px-1">
                            <li>
                                <NuxtLink to="/" class="rounded-full">
                                    <i class="fa-solid fa-house"></i>
                                    Accueil
                                </NuxtLink>
                            </li>
                            <li>
                                <NuxtLink to="/configuration" class="rounded-full">
                                    <i class="fa-solid fa-screwdriver-wrench"></i>
                                    Configuration
                                </NuxtLink>
                            </li>
                        </ul>
                    </div>
                </ul>
            </div>

        </div>
    </div>
    <div class="">
        <slot />
    </div>
    <div class="fixed inset-x-0 bottom-0">
        <footer class="footer items-center p-4 bg-base-200 text-neutral-content">
            <div class="items-center grid-flow-col">
                <i class="fa-solid fa-industry fa-2xl"></i>
                <p class="font-bold">Version 0.1</p>
            </div>
            <div class="grid-flow-col gap-4 md:place-self-center md:justify-self-end">
                <i class="fa-brands fa-github fa-2xl"></i>
                <i class="fa-solid fa-globe fa-xl"></i>
            </div>
        </footer>
    </div>
</template>

<script>
export default {
    data() {
        return {
            links: [
                {
                    label: 'Accueil',
                    icon: 'fa-solid fa-house',
                    active: true,
                },
                {
                    label: 'Configuration',
                    icon: 'fa-solid fa-screwdriver-wrench',
                    active: false,
                },
                {
                    label: 'Statistiques',
                    icon: 'fa-solid fa-chart-line',
                    active: false,
                },
            ],
        }
    },
}
</script>
<style>
.router-link-active {
    @apply bg-secondary text-white;
}
</style>