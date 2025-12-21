import { defineConfig } from 'vite';
import path from 'path';

export default defineConfig({
  root: './',
  build: {
    outDir: path.resolve(__dirname, '../src/main/resources/static/assets'),
    emptyOutDir: false,
    rollupOptions: {
      input : {
        'home': 'home.js',
        'user-dashboard': 'user-dashboard.js',
      },
      output: {
        entryFileNames: '[name].js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name].[ext]'
      }
    }
  }
});