import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// 프록시 관련 코드를 모두 지운 기본 상태입니다.
export default defineConfig({
    plugins: [react()],
    server: {
        port: 3000
    }
})