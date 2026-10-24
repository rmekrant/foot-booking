import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Le proxy redirige les appels /api vers le back-end Spring Boot.
// Le navigateur ne parle qu'au serveur Vite (même origine) : pas besoin de configurer CORS.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
});
