import { defineConfig } from 'vite';
import path from 'path';

export default defineConfig({
  root: './',
  build: {
    outDir: path.resolve(__dirname, '../src/main/resources/static'),
    emptyOutDir: false,
    rollupOptions: {
      input: './bundle.js',
      output: {
        entryFileNames: 'bundle.js',
        assetFileNames: 'bundle.css'
      }
    }
  }
});