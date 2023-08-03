<template>
    <div class="navbar bg-base-100 px-6">
        <div class="flex-1">
            <a class="btn btn-ghost normal-case text-xl"><i class="fa-solid fa-graduation-cap px-3"></i> Moodle Grades
                Master</a>
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
    <div>
        <slot />
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