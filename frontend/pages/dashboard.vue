<template>
    <div class="flex flex-col py-3 w-full border-opacity-50 space-y-3 px-6">
        <div class="grid h-20 card bg-base-300 rounded-box place-items-center">content</div>
        <div class="divider">OR</div>
        <div class="grid h-20 card bg-base-300 rounded-box place-items-center">content</div>

        <div class="form-control">
            <div class="input-group">
                <select class="select select-bordered">
                    <option disabled selected>Pick category</option>
                    <option>T-shirts</option>
                    <option>Mugs</option>
                </select>
                <button class="btn">Go</button>
            </div>
        </div>
    </div>
</template>

<script setup>
// const { data, error, pending, refresh } = await useFetch('http://localhost:8080/portainer/containers/2')
// console.log(data)
</script>