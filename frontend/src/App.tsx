import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Authorization from './components/pages/authorization/Authorization';
import AppMain from './components/pages/AppMain';
import SearchPage from './components/pages/search/SearchPage';
import Account from './components/pages/Account/Account';

function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<AppMain />} />
                    <Route path="/authorization" element={<Authorization />} />
                    <Route path="/search" element={<SearchPage />} />
                    <Route path="/account" element={<Account />} />
                </Routes>
            </BrowserRouter>
        </>
    );
}

export default App;
