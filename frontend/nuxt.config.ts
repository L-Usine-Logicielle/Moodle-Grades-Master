// https://nuxt.com/docs/api/configuration/nuxt-config

const REWRITE_URL = process.env.REWRITE_URL || 'http://127.0.0.1:8090'

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
      '/api/**': { proxy: { to: REWRITE_URL + '/**' } }
    }
  }
})
