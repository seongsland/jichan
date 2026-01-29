import React, {createContext, useContext, useRef, useState} from 'react';
import LoadingSpinner from '../components/LoadingSpinner';

const LoadingContext = createContext(null);

export const useLoading = () => {
    const context = useContext(LoadingContext);
    if (!context) {
        throw new Error('useLoading must be used within a LoadingProvider');
    }
    return context;
};

export const LoadingProvider = ({children}) => {
    const [loading, setLoading] = useState(false);
    const timerRef = useRef(null);

    const showLoading = () => {
        timerRef.current = setTimeout(() => {
            setLoading(true);
        }, 500);
    };

    const hideLoading = () => {
        clearTimeout(timerRef.current);
        setLoading(false);
    };

    return (<LoadingContext.Provider value={{loading, showLoading, hideLoading}}>
        {loading && <LoadingSpinner message="Loading..."/>}
        {children}
    </LoadingContext.Provider>);
};
