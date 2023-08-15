<template>
    <div>
        <div class="navbar bg-base-100">
            <div class="navbar-start">
                <div class="dropdown">
                    <label tabindex="0" class="btn btn-ghost lg:hidden">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M4 6h16M4 12h8m-8 6h16" />
                        </svg>
                    </label>
                    <ul tabindex="0"
                        class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52">
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
            <div class="navbar-end hidden lg:flex">
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
        </div>
        <!-- <div class="navbar bg-base-100 px-6">
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
        </div> -->
        <div>
            <slot />
        </div>
    </div>
    <!-- <div class="fixed inset-x-0 bottom-0">
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
    </div> -->
</template>

<style>.router-link-active {
    @apply bg-blue-700 text-white;
}</style>