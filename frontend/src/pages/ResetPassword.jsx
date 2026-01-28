import {useState} from 'react';
import {useSearchParams} from 'react-router-dom';
import api from '../utils/api';
import {validatePassword} from '../utils/validation';
import Message from '../components/Message';
import {useLoading} from '../context/LoadingContext';
import './Auth.css';

const ResetPassword = () => {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const [formData, setFormData] = useState({
        password: '',
        passwordConfirm: '',
    });
    const [message, setMessage] = useState({type: '', text: '', navigateTo: null});
    const {showLoading, hideLoading} = useLoading();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({type: '', text: '', navigateTo: null});

        const passwordError = validatePassword(formData.password);
        if (passwordError) {
            setMessage({type: 'error', text: passwordError});
            return;
        }

        if (formData.password !== formData.passwordConfirm) {
            setMessage({type: 'error', text: '비밀번호가 일치하지 않습니다.'});
            return;
        }

        showLoading();

        try {
            await api.post('/auth/reset_password', {token, password: formData.password});
            setMessage({
                type: 'success',
                text: '비밀번호가 성공적으로 재설정되었습니다. 로그인 페이지로 이동합니다.',
                navigateTo: '/login',
            });
        } catch (error) {
            setMessage({
                type: 'error',
                text: error.response?.data?.message || '비밀번호 재설정에 실패했습니다.',
            });
        } finally {
            hideLoading();
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>비밀번호 재설정</h2>
                <Message
                    type={message.type}
                    message={message.text}
                    onClose={() => setMessage({type: '', text: '', navigateTo: null})}
                    navigateTo={message.navigateTo}
                />
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="password">새 비밀번호</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            minLength={8}
                            maxLength={32}
                        />
                        <div className="password-rules">
                            <p>비밀번호는 8~32자로 설정해 주세요.</p>
                            <p>영문 대/소문자, 숫자, 특수문자 중 3가지 이상을 조합해야 합니다.</p>
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="passwordConfirm">새 비밀번호 확인</label>
                        <input
                            type="password"
                            id="passwordConfirm"
                            name="passwordConfirm"
                            value={formData.passwordConfirm}
                            onChange={handleChange}
                            required
                            minLength={8}
                            maxLength={32}
                        />
                    </div>
                    <button type="submit" className="btn btn-primary btn-block">
                        비밀번호 재설정
                    </button>
                </form>
            </div>
        </div>
    );
};

export default ResetPassword;
