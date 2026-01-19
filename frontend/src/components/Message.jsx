import { useEffect } from 'react';
import './Message.css';

const Message = ({ type, message, onClose, timestamp }) => {
  useEffect(() => {
    if (message) {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }, [timestamp]);

  if (!message) return null;

  return (
    <div className={`message message-${type}`}>
      <span>{message}</span>
      {onClose && (
        <button className="message-close" onClick={onClose}>
          ×
        </button>
      )}
    </div>
  );
};

export default Message;
