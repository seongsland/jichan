import {createContext, useCallback, useContext, useState} from 'react';
import api from '../utils/api';

const SpecialtyContext = createContext();

export const useSpecialty = () => useContext(SpecialtyContext);

export const SpecialtyProvider = ({children}) => {
    const [categories, setCategories] = useState([]);
    const [details, setDetails] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);

    const fetchSpecialties = useCallback(async () => {
        if (isLoaded) return;
        try {
            setIsLoaded(true);
            const [categoriesResponse, detailsResponse] = await Promise.all([
                api.get('/specialty/categories'),
                api.get('/specialty/details')
            ]);
            setCategories(categoriesResponse.data);
            setDetails(detailsResponse.data);
        } catch (error) {
            setIsLoaded(false);
            console.error('특기 정보를 불러오는데 실패했습니다.', error);
        }
    }, [isLoaded]);

    const value = {
        categories,
        details,
        isLoaded,
        fetchSpecialties
    };

    return (
        <SpecialtyContext.Provider value={value}>
            {children}
        </SpecialtyContext.Provider>
    );
};
