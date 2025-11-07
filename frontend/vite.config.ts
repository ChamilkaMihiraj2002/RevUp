import { defineConfig } from "vite"
import react from "@vitejs/plugin-react"
import { fileURLToPath, URL } from 'node:url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
        // use the browser-friendly process shim
       process: 'process/browser',
      "@": fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
    define: {
        // ensure references to process.env don't blow up
        'process.env': {},
        // Add global polyfill for sockjs-client
        'global': 'globalThis',
    },
    optimizeDeps: {
        include: ['process'],
    },
})
