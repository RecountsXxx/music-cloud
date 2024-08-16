import {defineConfig} from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
            '@': path.resolve(__dirname, './src'), // Альяс для упрощенного импорта
        },
    },
    build: {
        outDir: path.resolve(__dirname, '../dist'), // Путь для продакшен сборки
        rollupOptions: {
            input: {
                main: path.resolve(__dirname, './main.js'), // Входной файл (путь к main.js в папке public)
            },
            output: {
                entryFileNames: '[name].[hash].js', // Имя файлов с хэшированием
            }
        }
    },
    server: {
        port: 8081, // Порт для разработки
        open: true, // Автоматически открывать браузер
    }
});
