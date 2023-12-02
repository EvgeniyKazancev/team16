import styles from './Filters.module.css';
import { Question, Options } from '../../../types/types';

type Props = {
    question: Question;
    onClick: (arg: number | string) => void;
};

const Radio = (props: Props) => {
    const item = props.question;
    const questionId = item.id;

    return (
        <div className={styles.filter_container}>
            <span className={styles.filter_label}>{item.text}</span>
            <hr className={styles.filter_line} />
            <div className={styles.filter_options_container}>
                {item.options?.map((item: Options) => {
                    return (
                        <label className={styles.radio_container} key={item.id}>
                            <input className={styles.radio_input} type="radio" id={item.id} name={questionId} value={item.value} onChange={(e) => props.onClick(e.target.value)} />
                            <span className={styles.radio_fake}></span>
                            <div className={styles.radio_label}>{item.text}</div>
                        </label>
                    );
                })}
            </div>
        </div>
    );
};

export default Radio;
