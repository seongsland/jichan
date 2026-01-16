import {useEffect, useState} from 'react';
import {useNavigate, useSearchParams} from 'react-router-dom';
import api from '../utils/api';
import Message from '../components/Message';
import './Auth.css';

const VerifyEmail = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const [message, setMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const verifyEmail = async () => {
      if (!token) {
        setMessage({
          type: 'error',
          text: '인증 토큰이 없습니다.',
        });
        setLoading(false);
        return;
      }

      try {
        await api.get(`/auth/verify_email?token=${token}`);
        setMessage({
          type: 'success',
          text: '이메일 인증이 완료되었습니다.',
        });
        setTimeout(() => {
          navigate('/login');
        }, 2000);
      } catch (error) {
        setMessage({
          type: 'error',
          text: error.response?.data?.message || '이메일 인증에 실패했습니다.',
        });
      } finally {
        setLoading(false);
      }
    };

    verifyEmail();
  }, [token, navigate]);

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>이메일 인증</h2>
        {loading ? (
          <p>인증 중...</p>
        ) : (
          <Message
            type={message.type}
            message={message.text}
            onClose={() => setMessage({ type: '', text: '' })}
          />
        )}
      </div>
    </div>
  );
};

export default VerifyEmail;


