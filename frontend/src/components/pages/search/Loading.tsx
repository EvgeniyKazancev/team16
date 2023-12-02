import styles from './SearchPage.module.css';
import { Info } from '../../types/types';

type Props = {
    content: Info | undefined;
    isLoading: boolean;
    error: boolean;
};

const Loading = (props: Props) => {
    return (!props.content || props.content.totalElements === 0) && !props.isLoading ? (
        <div className={styles.loading_container}>
            <h5 className={styles.loading_text}>Ещё ничего не найдено...</h5>
        </div>
    ) : props.isLoading ? (
        <div className={props.isLoading ? `${styles.loading_popup_container} ${styles.loading_active}` : `${styles.loading_popup_container}`}>
            <div
                className={
                    props.error
                        ? `${styles.loading_content} ${styles.loading_active} ${styles.loading_error}`
                        : props.isLoading
                        ? `${styles.loading_content} ${styles.loading_active}`
                        : `${styles.loading_content}`
                }
                onClick={(e) => e.stopPropagation()}
            >
                <h5 className={styles.loading_popup_text}>{!props.error ? 'Подождите, пожалуйста, идет загрузка новостей...' : 'Что-то пошло не так. Повторите попытку позже.'}</h5>
            </div>
        </div>
    ) : (
        <div></div>
    );
};

export default Loading;
