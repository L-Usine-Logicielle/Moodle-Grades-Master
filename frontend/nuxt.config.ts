// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  devtools: { enabled: true },
  pages: true,
  modules: ['@nuxtjs/tailwindcss'],
  css: [
    '@fortawesome/fontawesome-svg-core/styles.css',
    '@fortawesome/fontawesome-free/css/all.css',
  ],
  nitro: {
    routeRules: {
      '/api/**': { proxy: { to: 'http://127.0.0.1:8080/**' } }
    }
  }
})
