import styles from './Filters.module.css';
import { Question, Options } from '../../../types/types';
import { useEffect, useState } from 'react';

type Props = {
    question: Question | undefined;
    onClick: (arg: (number | string)[]) => void;
};

const Check = (props: Props) => {
    // const today = new Date();
    const item = props.question;
    const questionId = item?.id;
    const [answerArray, setAnswerArray] = useState<(number | string)[]>([]);

    useEffect(() => {
        props.onClick(answerArray);
    }, [answerArray]);

    const addValue = (value: number | string) => {
        const repeatValue = answerArray.findIndex((el) => el == value);
        console.log(repeatValue);

        if (repeatValue < 0) {
            setAnswerArray([...answerArray, Number(value)]);
        } else {
            setAnswerArray(answerArray.filter((el) => answerArray.indexOf(el) !== repeatValue));
        }
    };

    return (
        <div className={styles.filter_container}>
            <span className={styles.filter_label}>{item?.text}</span>
            <hr className={styles.filter_line} />
            <div className={styles.filter_options_container}>
                {item?.options?.map((item: Options) => {
                    return (
                        <label className={styles.check_container} key={item.id}>
                            <input className={styles.check_input} type="checkbox" id={item.id} name={questionId} value={item.value} onChange={(e) => addValue(e.target.value)} />
                            <span className={styles.check_fake}></span>
                            <div className={styles.check_label}>{item.text}</div>
                        </label>
                    );
                })}
            </div>
        </div>
    );
};

export default Check;
