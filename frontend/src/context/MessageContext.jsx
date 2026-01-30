import React, {createContext, useContext, useState, useCallback} from 'react';
import Message from '../components/Message'; // Message 컴포넌트 임포트

const MessageContext = createContext(null);

export const useMessage = () => {
    return useContext(MessageContext);
};

export const MessageProvider = ({children}) => {
    const [messageState, setMessageState] = useState(null); // { type: 'success'|'error', text: '...' }

    const showMessage = useCallback((type, text, navigateTo = null) => {
        setMessageState({type, text, navigateTo});
    }, []);

    const hideMessage = useCallback(() => {
        setMessageState(null);
    }, []);

    return (
        <MessageContext.Provider value={{showMessage, hideMessage}}>
            {children}
            {messageState && (
                <Message
                    type={messageState.type}
                    message={messageState.text}
                    onClose={hideMessage}
                    navigateTo={messageState.navigateTo}
                />
            )}
        </MessageContext.Provider>
    );
};
