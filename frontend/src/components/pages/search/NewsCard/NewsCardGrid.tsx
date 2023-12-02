import styles from './NewsCardGrid.module.css';
import { useEffect, useState } from 'react';
import PopUp from './PopUp';
import { Content } from '../../../types/types';
import { PublicationsText } from '../../../types/types';

type Props = {
    info: Content;
    key: number;
};

const NewsCardGrid = (props: Props) => {
    const info = props.info;
    const text = info.text;
    const imgUrls = info.img?.map((item) => {
        return item.content;
    });

    const [timePublished, setTimePublished] = useState('');
    const [datePublished, setDatePublished] = useState('');
    const [heading, setHeading] = useState<string | undefined>('');
    const [popUpActive, setPopUpActive] = useState(false);
    const [url, setUrl] = useState('');
    const [source, setSource] = useState('');

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
            const result = info.source.replace(/(^\w+:|^)\/\/|\.com|www\./, '');
            setSource(result);
            getHeader(text);
        }
    }, []);

    const getHeader = (text: PublicationsText[]) => {
        console.log('bebebe');

        const found = text.find((element: PublicationsText) => element.header || text.indexOf(element) === 0);
        const tagsRemoved = found?.text.replace(/(&apos;)/gm, `'`);
        console.log('hello');
        setHeading(tagsRemoved);
    };

    // useEffect(() => {
    //     getHeader(text);
    // }, [text]);

    useEffect(() => {
        switch (imgUrls.length) {
            case 0:
                setUrl('/imgs/pics/sorry_no_image_2.svg');
                break;
            case 1:
                setUrl(imgUrls[0]);
                break;
            case 2:
                setUrl(imgUrls[1]);
                break;
            default:
                setUrl(imgUrls[imgUrls.length - 1]);
        }
    }, []);

    return (
        <li className={styles.container} key={info.id}>
            <div className={styles.img} style={{ backgroundImage: `url(${url})` }}></div>
            <div className={styles.card_info}>
                <span className={styles.time}>{timePublished}</span>
                <span className={styles.date}>{datePublished}</span>
                <a href={info.url} target="_blank" className={styles.source}>
                    {source}
                </a>
            </div>
            <span className={styles.heading}>{heading}</span>
            <div className={styles.btns_container}>
                <button className={styles.btn_like}></button>
                <button className={styles.btn_share}></button>
                <button className={styles.btn_delete}></button>
            </div>
            <PopUp heading={heading} active={popUpActive} setActive={setPopUpActive} info={info} timePublished={timePublished} datePublished={datePublished} />
        </li>
    );
};

export default NewsCardGrid;
