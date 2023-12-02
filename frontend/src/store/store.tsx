import { configureStore } from '@reduxjs/toolkit';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import { AuthorizationSlice } from './features/AuthorizationSlice';
import { LoggedInSlice } from './features/LoggedInSlice';

export const store = configureStore({
    reducer: {
        Authorization: AuthorizationSlice.reducer,
        LoggedIn: LoggedInSlice.reducer
    }
});

export const useAppDispatch: () => typeof store.dispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<ReturnType<typeof store.getState>> = useSelector;
