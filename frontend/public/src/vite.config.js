import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

import path from 'path'
// https://vitejs.dev/config/
export default defineConfig({
  base: "/",
  server : {
    port: 5173,
    host: true,
    watch: {
      usePolling: true,
    },
  },
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      // eslint-disable-next-line no-undef
      '~bootstrap': path.resolve(__dirname, 'node_modules/bootstrap')
    }
  }
})
