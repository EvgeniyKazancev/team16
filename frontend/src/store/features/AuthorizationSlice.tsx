import { PayloadAction, createSlice } from '@reduxjs/toolkit';

export interface AuthorizationInfo {
    email: string;
    password: string;
}

const initialState: AuthorizationInfo = {
    email: '',
    password: ''
};

export const AuthorizationSlice = createSlice({
    name: 'Authorization',
    initialState,
    reducers: {
        addEmail: (
            state,
            action: PayloadAction<{
                email: string;
            }>
        ) => {
            state.email = action.payload.email;
        },
        addPassword: (
            state,
            action: PayloadAction<{
                password: string;
            }>
        ) => {
            state.password = action.payload.password;
        }
    }
});

export default AuthorizationSlice.reducer;
export const { addEmail } = AuthorizationSlice.actions;
export const { addPassword } = AuthorizationSlice.actions;
