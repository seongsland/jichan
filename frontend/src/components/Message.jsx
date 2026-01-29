import {useCallback, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './Message.css';

const Message = ({type, message, onClose, navigateTo}) => {
    const navigate = useNavigate();

    const closeMessage = useCallback(() => {
        onClose();
        if (navigateTo) {
            navigate(navigateTo);
        }
    }, [onClose, navigateTo, navigate]);

    useEffect(() => {
        if (!message) {
            return;
        }

        const handleKeyDown = (e) => {
            if (e.key === 'Enter' || e.key === 'Escape') {
                e.preventDefault();
                closeMessage();
            }
        };

        document.addEventListener('keydown', handleKeyDown);

        return () => {
            document.removeEventListener('keydown', handleKeyDown);
        };
    }, [message, closeMessage]);

    if (!message) return null;

    return (
        <div className="message-overlay" onClick={closeMessage}>
            <div className={`message-box message-${type}`}>
                <span>{message}</span>
            </div>
        </div>
    );
};

export default Message;
