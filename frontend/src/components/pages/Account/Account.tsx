import styles from './Account.module.css';
import Header from '../../reusable/header/Header';
import Footer from '../../reusable/Footer/Footer';
import NewsCards from '../search/NewsCard/NewsCards';
import { useEffect, useState } from 'react';
import { Content } from '../../types/types';

const Account = () => {
    const [info, setInfo] = useState<Content>();

    useEffect(() => {
        const item = localStorage.getItem('likedItem');
        if (typeof item === 'string') {
            const result = JSON.parse(item);
            console.log(result);

            setInfo(result);
        }
    }, []);

    return (
        <>
            <Header />
            <main className={styles.main}>
                <ul className={styles.folders_list}>
                    <li className={styles.folder + ' ' + styles.favorite + ' ' + styles.active}>Избранное</li>
                    <li className={styles.folder + ' ' + styles.sources}>Источники</li>
                    <li className={styles.folder + ' ' + styles.black_list}>Черный список</li>
                </ul>
                <div className={styles.liked_news}>{info ? <NewsCards info={info} key={info?.id} /> : <div></div>}</div>
            </main>
            <Footer />
        </>
    );
};

export default Account;
