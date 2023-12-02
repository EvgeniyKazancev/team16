import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// import { useAppSelector } from '../../store/store';

const AppMain = () => {
    // const isLogedIn = useAppSelector((state) => state.LoggedIn.isLogedIn);
    const navigate = useNavigate();

    useEffect(() => {
        if (!localStorage.getItem('uuid')) navigate('/authorization');
        else navigate('/search');
    }, []);
    return <div>AppMain</div>;
};

export default AppMain;
