import { useEffect, useState } from 'react';
import styles from './PopUp.module.css';

type Props = {
    imgs: string[];
};

const ImgContainer = (props: Props) => {
    const [url, setUrl] = useState('');
    const imgs = props.imgs;

    useEffect(() => {
        switch (imgs.length) {
            case 0:
                setUrl('/imgs/pics/sorry_no_image.svg');
                break;
            case 1:
                setUrl(imgs[0]);
                break;
            case 2:
                setUrl(imgs[1]);
                break;
            default:
                setUrl(imgs[imgs.length - 1]);
        }
    }, []);

    /* eslint-disable @typescript-eslint/no-explicit-any */

    const onImageError = (e: any) => {
        e.src = '/imgs/pics/sorry_no_image.svg';
    };

    return (
        <div className={styles.img_container}>
            <img src={url} onError={(e) => onImageError(e)} className={styles.img} style={imgs.length === 0 ? { backgroundColor: 'var(--accent-color)', padding: '40px' } : {}} />
        </div>
    );
};

export default ImgContainer;
