import styles from './Header.module.css';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const Header = () => {
    const navigate = useNavigate();
    const logOut = () => {
        localStorage.getItem('uuid') ? localStorage.removeItem('uuid') : sessionStorage.getItem('uuid') ? sessionStorage.removeItem('uuid') : {};
        navigate('/');
    };

    return (
        <header className={styles.header}>
            <Link to="/search" className={styles.logo}>
                This is НОВОСТИ
            </Link>
            <nav className={styles.nav}>
                <ul className={styles.nav_list}>
                    <li className={styles.nav_list_item}>
                        <Link className={styles.nav_list_link} to="/account">
                            Личный кабинет
                        </Link>
                    </li>
                </ul>
                <div className={styles.nav_account_links}>
                    <button className={styles.nav_logout} onClick={logOut} />
                    <Link className={styles.nav_account} to="/account">
                        Е
                    </Link>
                    {/* <img src="public/imgs/pics/avatar.png" className={styles.nav_account} /> */}
                </div>
            </nav>
        </header>
    );
};

export default Header;
