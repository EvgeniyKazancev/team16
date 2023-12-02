import styles from './SearchPage.module.css';
import Header from '../../reusable/header/Header';
import Filters from './Filters/Filters';
import NewsCards from './NewsCard/NewsCards';
import { useEffect, useState } from 'react';
import { Info, Item } from '../../types/types';
import { useNavigate } from 'react-router-dom';
import { newsRequest } from '../../../Services';
import { PaginatedItems } from './NewsCard/Pagination';
import Footer from '../../reusable/Footer/Footer';
import Loading from './Loading';
import NewsCardGrid from './NewsCard/NewsCardGrid';

const SearchPage = () => {
    const [startDate, setStartDate] = useState<string | number>(new Date(new Date(new Date().setFullYear(new Date().getFullYear() - 100))).toString());
    const [endDate, setEndDate] = useState<string | number>(new Date().toString());
    const [content, setContent] = useState<Info>();
    const [textSearch, setTextSearch] = useState('');
    const [size, setSize] = useState<number | string>(12);
    const [page, setPage] = useState(0);
    const [pagesAmount, setPagesAmount] = useState<number>();
    const [catIds, setCatIds] = useState<(number | string)[]>([]);
    const [sourceIds, setSourceIds] = useState<(number | string)[]>([]);
    const [displayStyle, setDisplayStyle] = useState('list');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(false);
    const navigate = useNavigate();
    const uuid = localStorage.getItem('uuid') || sessionStorage.getItem('uuid');

    const formatDate = (date: Date) => {
        // 2023-11-14T00:00:00
        const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().length === 1 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1}-${
            date.getDate().toString().length === 1 ? '0' + date.getDate() : date.getDate()
        }T${date.getHours().toString().length === 1 ? '0' + date.getHours() : date.getHours()}%3A00%3A${date.getMinutes().toString().length === 1 ? '0' + date.getMinutes() : date.getMinutes()}`;
        return formattedDate;
    };

    const handleError = () => {
        setError(true);
        setTimeout(() => {
            setError(false);
            setIsLoading(false);
        }, 3000);
    };

    const sendRequest = async () => {
        const newsArguments = {
            uuid: uuid,
            startDate: formatDate(new Date(startDate)),
            endDate: formatDate(new Date(endDate)),
            page: page,
            size: size,
            catId: catIds || undefined,
            sourceId: sourceIds || undefined,
            searchString: textSearch || undefined,
            favoritesOnly: false
        };
        if (uuid) {
            setIsLoading(true);
            const result = await newsRequest(newsArguments);
            result ? console.log(result) : {};
            if (result && result.content) {
                setPagesAmount(result.totalPages);
                setContent({
                    totalElements: result.totalElements,
                    totalPages: result.totalPages,
                    content: result.content.map((item: Item) => {
                        return {
                            id: item.id,
                            created: item.created,
                            removed: item.removed,
                            url: item.url,
                            source: item.source.url,
                            img: item.publicationsData,
                            text: item.publicationsText,
                            usersWhoHaveFavorited: item.usersWhoHaveFavorited
                        };
                    })
                });
                setIsLoading(false);
            } else if (result) handleError();
        }
    };

    useEffect(() => {
        uuid ? {} : navigate('/');
        sendRequest();
    }, []);

    const sendRequestWithFilters = () => {
        sendRequest();
    };

    useEffect(() => {
        sendRequest();
    }, [page]);

    return (
        <div className={styles.container}>
            <Header />
            <main className={styles.main} style={!content || content?.totalElements < 4 ? { height: 'calc(100vh - 74px)' } : {}}>
                <div className={styles.search_container}>
                    <input type="text" className={styles.search_input} placeholder="Давай найдем прикольные новости!" onChange={(e) => setTextSearch(e.target.value)} />
                    <button className={styles.search_btn} onClick={sendRequestWithFilters}></button>
                </div>
                <Filters setStartDate={setStartDate} setEndDate={setEndDate} setCatIds={setCatIds} setSourceIds={setSourceIds} setSize={setSize} />
                <div className={styles.heading_container}>
                    <h2 className={styles.heading}>Смотри, чё нашел!</h2>
                    <div className={styles.btns_container}>
                        <button className={displayStyle === 'list' ? `${styles.btn_list} ${styles.btn_list_active}` : `${styles.btn_list}`} onClick={() => setDisplayStyle('list')}></button>
                        <button className={displayStyle === 'grid' ? `${styles.btn_grid} ${styles.btn_grid_active}` : `${styles.btn_grid}`} onClick={() => setDisplayStyle('grid')}></button>
                    </div>
                </div>

                {content ? (
                    <ul className={displayStyle === 'list' ? `${styles.news_list}` : `${styles.news_list} ${styles.news_list_grid}`}>
                        {content.content.map((item) => {
                            return displayStyle === 'list' ? <NewsCards info={item} key={item.id} /> : <NewsCardGrid info={item} key={item.id} />;
                        })}
                        <Loading content={content} isLoading={isLoading} error={error} />
                    </ul>
                ) : (
                    <Loading content={content} isLoading={isLoading} error={error} />
                )}

                {content ? <PaginatedItems setPageCount={setPage} pagesAmount={pagesAmount} /> : <></>}
            </main>
            <Footer />
        </div>
    );
};

export default SearchPage;
