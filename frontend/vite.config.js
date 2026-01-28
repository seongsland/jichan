import {defineConfig, loadEnv} from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(({mode}) => {
    // 1. env 변수를 여기서 먼저 선언하고 값을 할당합니다.
    const env = loadEnv(mode, '.', '');

    return {
        plugins: [react()],
        server: {
            port: 3000,
            proxy: {
                '/api': {
                    target: env.VITE_API_URL || 'http://localhost:8080',
                    changeOrigin: true
                }
            }
        }
    }
})