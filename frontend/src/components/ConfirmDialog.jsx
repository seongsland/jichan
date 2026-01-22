import React from 'react';
import './ConfirmDialog.css';

const ConfirmDialog = ({ 
  isOpen, 
  message, 
  onConfirm, 
  onCancel, 
  confirmText = 'Yes',
  cancelText = 'No'
}) => {
  if (!isOpen) return null;

  return (
    <div className="confirm-overlay" onClick={onCancel}>
      <div className="confirm-box" onClick={(e) => e.stopPropagation()}>
        <p className="confirm-message">{message}</p>
        <div className="confirm-actions">
          <button 
            className="btn btn-primary" 
            onClick={onConfirm}
          >
            {confirmText}
          </button>
          <button 
            className="btn btn-outline" 
            onClick={onCancel}
          >
            {cancelText}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmDialog;
