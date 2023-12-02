import { PayloadAction, createSlice } from '@reduxjs/toolkit';

export interface LoggedIn {
    isLogedIn: boolean;
}

const initialState: LoggedIn = {
    isLogedIn: false
};

export const LoggedInSlice = createSlice({
    name: 'LoggedIn',
    initialState,
    reducers: {
        logIn: (
            state,
            action: PayloadAction<{
                isLoggedIn: boolean;
            }>
        ) => {
            state.isLogedIn = action.payload.isLoggedIn;
        }
    }
});

export default LoggedInSlice.reducer;
export const { logIn } = LoggedInSlice.actions;
