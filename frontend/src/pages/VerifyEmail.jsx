import {useEffect, useRef} from 'react';
import {useSearchParams} from 'react-router-dom';
import api from '../utils/api';
import {useMessage} from '../context/MessageContext';
import {useLoading} from '../context/LoadingContext';
import './Auth.css';

const VerifyEmail = () => {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const {showLoading, hideLoading} = useLoading();
    const {showMessage} = useMessage();
    const hasFetched = useRef(false);

    useEffect(() => {
        if (hasFetched.current) return;
        hasFetched.current = true;

        const verifyEmail = async () => {
            showLoading();
            if (!token) {
                showMessage(
                    'error',
                    '인증 토큰이 없습니다.',
                );
                hideLoading();
                return;
            }

            try {
                await api.get(`/auth/verify_email?token=${token}`);
                showMessage(
                    'success',
                    '이메일 인증이 완료되었습니다.',
                    '/login',
                );
            } catch (error) {
                showMessage(
                    'error',
                    error.response?.data?.message || '이메일 인증에 실패했습니다.',
                );
            } finally {
                hideLoading();
            }
        };

        verifyEmail();
    }, [token, showLoading, hideLoading, showMessage]);

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>이메일 인증</h2>
            </div>
        </div>
    );
};

export default VerifyEmail;
