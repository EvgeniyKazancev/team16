import styles from './Filters.module.css';
import { Question, Options } from '../../../types/types';
type Props = {
    question: Question;
    setStartDate: (arg: string) => void;
    setEndDate: (arg: string) => void;
};

const DateInput = (props: Props) => {
    const item = props.question;

    return (
        <div className={styles.filter_container}>
            <span className={styles.filter_label}>{item.text}</span>
            <hr className={styles.filter_line} />
            <div className={styles.filter_options_container}>
                {item.options?.map((item: Options) => {
                    return (
                        <div className={styles.date_container} key={item.id}>
                            <label htmlFor="">
                                <span className={styles.date_label}>Начиная с</span>
                                <input type="date" className={styles.startDate} onChange={(e) => props.setStartDate(e.target.value)} />
                            </label>
                            <label htmlFor="">
                                <span className={styles.date_label}>До</span>
                                <input type="date" className={styles.endDate} onChange={(e) => props.setEndDate(e.target.value)} />
                            </label>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default DateInput;
