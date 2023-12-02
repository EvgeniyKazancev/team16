import styles from './NewsCard.module.css';
import { Content, PublicationsText } from '../../../types/types';
import { useEffect, useState } from 'react';
import PopUp from './PopUp';

type Props = {
    info: Content;
    key: number;
};

const NewsCards = (props: Props) => {
    const info = props.info;
    const text = info.text;

    const [timePublished, setTimePublished] = useState('');
    const [datePublished, setDatePublished] = useState('');
    const [heading, setHeading] = useState<string | undefined>('');
    const [popUpActive, setPopUpActive] = useState(false);
    const [liked, setLiked] = useState(false);

    useEffect(() => {
        if (info) {
            const date = new Date(info.created);
            setTimePublished(
                `${date.getHours().toString().length === 2 ? date.getHours() : '0' + date.getHours()}:${date.getMinutes().toString().length === 2 ? date.getMinutes() : '0' + date.getMinutes()}`
            );
            setDatePublished(
                `${date.getDate().toString().length === 2 ? date.getDate() : '0' + date.getDate()}.${
                    date.getMonth().toString().length === 2 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
                }.${date.getFullYear()}`
            );
        }
        const likedItem = localStorage.getItem('likedItem');
        if (typeof likedItem === 'string') {
            const result = JSON.parse(likedItem);
            if (result.id === info.id) {
                setLiked(true);
            }
        }
    }, []);

    const getHeader = (text: PublicationsText[]) => {
        const found = text.find((element: PublicationsText) => element.header || text.indexOf(element) === 0);
        const tagsRemoved = found?.text.replace(/(&apos;)/gm, `'`);
        setHeading(tagsRemoved);
    };

    useEffect(() => {
        getHeader(text);
    }, [text]);

    const likeNewsItem = () => {
        if (!liked) {
            setLiked(true);
            localStorage.setItem('likedItem', JSON.stringify(info));
        } else {
            setLiked(false);
            localStorage.removeItem('likedItem');
        }
    };

    return (
        <li className={styles.container} key={info.id}>
            <div className={styles.card_info} onClick={() => setPopUpActive(true)}>
                <span className={styles.time}>{timePublished}</span>
                <span className={styles.date}>{datePublished}</span>
                <span className={styles.heading}>{heading}</span>
            </div>
            <div className={styles.btns_container}>
                <button className={!liked ? styles.btn_like : styles.btn_like + ' ' + styles.btn_like_active} onClick={likeNewsItem}></button>
                <button className={styles.btn_share}></button>
                <button className={styles.btn_delete}></button>
            </div>
            <PopUp heading={heading} active={popUpActive} setActive={setPopUpActive} info={info} timePublished={timePublished} datePublished={datePublished} />
        </li>
    );
};

export default NewsCards;
