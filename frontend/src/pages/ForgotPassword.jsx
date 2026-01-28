import {useState} from 'react';
import api from '../utils/api';
import Message from '../components/Message';
import {useLoading} from '../context/LoadingContext';
import './Auth.css';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState({type: '', text: ''});
    const {showLoading, hideLoading} = useLoading();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({type: '', text: ''});
        showLoading();

        try {
            await api.post('/auth/forgot_password', {email});
            setMessage({
                type: 'success', text: '비밀번호 재설정 링크가 포함된 이메일을 보냈습니다.',
            });
        } catch (error) {
            setMessage({
                type: 'error', text: error.response?.data?.message || '오류가 발생했습니다. 다시 시도해 주세요.',
            });
        } finally {
            hideLoading();
        }
    };

    return (<div className="auth-container">
            <div className="auth-card">
                <h2>비밀번호 찾기</h2>
                <Message
                    type={message.type}
                    message={message.text}
                    onClose={() => setMessage({type: '', text: ''})}
                />
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">이메일</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary btn-block">
                        재설정 링크 보내기
                    </button>
                </form>
            </div>
        </div>);
};

export default ForgotPassword;
