<template>
    <div class="drawer">
        <input id="my-drawer-3" type="checkbox" class="drawer-toggle" />
        <div class="drawer-content flex flex-col">
            <!-- Navbar -->
            <div class="w-full navbar bg-base-300">
                <div class="flex-none lg:hidden">
                    <label for="my-drawer-3" class="btn btn-square btn-ghost">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                            class="inline-block w-6 h-6 stroke-current">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M4 6h16M4 12h16M4 18h16"></path>
                        </svg>
                    </label>
                </div>
                <div class="flex-1 px-2 mx-2">
                    <i class="fa-solid fa-graduation-cap px-3"></i> Moodle Grades Master
                </div>
                <div class="flex-none hidden lg:block">
                    <ul class="menu menu-horizontal space-x-4">
                        <!-- Navbar menu content here -->
                        <li v-for="link in links" :key="link.label">
                            <a :class="{ 'btn-active': link.active, 'rounded-full': true }">
                                <i :class="link.icon"></i> {{ link.label }}
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <slot />
        </div>
        <div class="drawer-side">
            <label for="my-drawer-3" class="drawer-overlay"></label>
            <ul class="menu p-4 w-80 bg-base-100">
                <li v-for="link in links" :key="link.label">
                    <a :class="{ 'btn-active': link.active, 'rounded-full': true }">
                        <i :class="link.icon"></i> {{ link.label }}
                    </a>
                </li>
            </ul>
        </div>
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
        };
    },
};
</script>
