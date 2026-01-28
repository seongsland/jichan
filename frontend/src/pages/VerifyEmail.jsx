import {useEffect, useState} from 'react';
import {useSearchParams} from 'react-router-dom';
import api from '../utils/api';
import Message from '../components/Message';
import {useLoading} from '../context/LoadingContext';
import './Auth.css';

const VerifyEmail = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const { showLoading, hideLoading } = useLoading();
  const [message, setMessage] = useState({ type: '', text: '', navigateTo: null });

  useEffect(() => {
    const verifyEmail = async () => {
      showLoading();
      if (!token) {
        setMessage({
          type: 'error',
          text: '인증 토큰이 없습니다.',
        });
        hideLoading();
        return;
      }

      try {
        await api.get(`/auth/verify_email?token=${token}`);
        setMessage({
          type: 'success',
          text: '이메일 인증이 완료되었습니다.',
          navigateTo: '/login',
        });
      } catch (error) {
        setMessage({
          type: 'error',
          text: error.response?.data?.message || '이메일 인증에 실패했습니다.',
        });
      } finally {
        hideLoading();
      }
    };

    verifyEmail();
  }, [token, showLoading, hideLoading]);

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>이메밀 인증</h2>
        <Message
          type={message.type}
          message={message.text}
          onClose={() => setMessage({ type: '', text: '', navigateTo: null })}
          navigateTo={message.navigateTo}
        />
      </div>
    </div>
  );
};

export default VerifyEmail;
