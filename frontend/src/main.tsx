import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import './assets/reset.css';

import 'react-date-range/dist/styles.css'; // main style file
import 'react-date-range/dist/theme/default.css'; // theme css file
import './assets/GlobalStyles.css';
import { Provider } from 'react-redux';
import { store } from './store/store.tsx';

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <Provider store={store}>
            <App />
        </Provider>
    </React.StrictMode>
);
