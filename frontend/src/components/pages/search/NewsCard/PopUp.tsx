import styles from './PopUp.module.css';
import { Content } from '../../../types/types';
import ImgContainer from './ImgContainer';
import { useEffect, useState } from 'react';
import Button from '../../../reusable/Button';
import Comment from './Comment';

type Props = {
    active: boolean;
    setActive: (arg: boolean) => void;
    info: Content;
    timePublished: string;
    datePublished: string;
    heading: string | undefined;
};

const PopUp = (props: Props) => {
    const info = props.info;
    const imgUrls = info.img?.map((item) => {
        return item.content;
    });
    const [text, setText] = useState('');
    const [comment, setComment] = useState('');

    useEffect(() => {
        const textArray = info.text.map((item) => {
            return item.text;
        });
        const tagsRemoved = textArray.join(' ').replace(/(&apos;)/gm, `'`);
        // const tagsRemoved = found?.text.replace(/(&apos;)/gm, `'`);
        setText(tagsRemoved);
    }, [info]);

    const comments: string[] = [];

    return (
        <div className={props.active ? `${styles.container} ${styles.active}` : `${styles.container}`} onClick={() => props.setActive(false)}>
            <div className={props.active ? `${styles.content} ${styles.active}` : `${styles.content}`} onClick={(e) => e.stopPropagation()}>
                <header className={styles.header}>
                    <button className={styles.share}></button>
                    <div className={styles.btns_container}>
                        <select name="folders" id="folder-select" className={styles.select_folder}>
                            <option value="">Сохранить новость</option>
                            <option value="saved">избранное</option>
                            <option value="animals">животные</option>
                        </select>
                        <button className={styles.close_popup} onClick={() => props.setActive(false)}></button>
                    </div>
                </header>
                <ImgContainer imgs={imgUrls} />
                <div className={styles.source_container}>
                    <div className={styles.date_time}>
                        <span className={styles.time}>{props.timePublished}</span>
                        <span className={styles.date}>{props.datePublished}</span>
                    </div>
                    <a href={info.url} target="_blank" className={styles.link_btn}>
                        {info.source}
                    </a>
                </div>
                <div className={styles.article_container}>
                    <div className={styles.tag}>интересное</div>
                    <h2 className={styles.heading}>{props.heading}</h2>
                    <p className={styles.article_text}>{text}</p>
                </div>
                <label className={styles.comment_container}>
                    <div className={styles.comment_label}>Оставьте комментарий </div>
                    <textarea id="comment" name="story" rows={3} className={styles.comment_input} onChange={(e) => setComment(e.target.value)} />
                </label>
                <Button
                    text="Отправить комментарий"
                    onClick={() => {}}
                    width="217px"
                    height="45px"
                    disabled={comment === '' ? true : false}
                    marginTop="0px"
                    fontSize="14px"
                    lineHeight="14px"
                    uppercase={false}
                />
                <div className={styles.comment_heading_container}>
                    <h2 className={styles.comment_heading}>комментарии</h2>
                    <div className={styles.comment_line}></div>
                    <div className={styles.comment_count}>{comments.length}</div>
                </div>
                <div className={styles.other_comments_container}>
                    {comments.length > 0 ? (
                        comments.map((item) => {
                            return <Comment data={item} />;
                        })
                    ) : (
                        <div className={styles.no_comments}>Комментариев пока нет, будьте первым.</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default PopUp;
