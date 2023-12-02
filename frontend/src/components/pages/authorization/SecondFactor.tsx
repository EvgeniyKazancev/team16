import styles from './Authorization.module.css';
import { useEffect, useState } from 'react';
import { authRequest } from '../../../Services';
import { useAppSelector } from '../../../store/store';
import { useCountdown } from 'usehooks-ts';

type Props = {
    code: string;
    setCode: (arg: string) => void;
    setSecondFactor: (arg: boolean) => void;
    email: string;
    password: string;
};

const SecondFactor = (props: Props) => {
    const [formattedTime, setFormattedTime] = useState('');
    const [sendActive, setSendActive] = useState(false);
    const [count, { startCountdown, resetCountdown }] = useCountdown({
        countStart: 30,
        intervalMs: 1000
    });
    const email = useAppSelector((state) => state.Authorization.email);
    const password = useAppSelector((state) => state.Authorization.password);

    const formatTime = (seconds: number) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${minutes < 10 ? '0' : ''}${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    };

    useEffect(() => {
        setFormattedTime(formatTime(count));
        if (count === 0) activateSend();
    }, [count]);

    function activateSend() {
        sendActive ? setSendActive(false) : setSendActive(true);
    }

    const handleSend = async () => {
        await authRequest(email, password);
        resetCountdown();
        setSendActive(false);
    };

    useEffect(() => {
        if (!sendActive) {
            startCountdown();
        }
    }, [sendActive]);

    return (
        <div className={styles.form_content}>
            <p className={styles.description}>Вам был отправлен код на почту. Введите его в это поле:</p>
            <label htmlFor="" className={styles.input_label}>
                <input type="text" className={styles.input} required value={props.code} onChange={(e) => props.setCode(e.target.value)} maxLength={6} />
            </label>
            <div className={styles.timer_container}>
                <button disabled={!sendActive} className={styles.timer_label} onClick={handleSend} type="button">
                    {sendActive ? 'Отправить код заново' : 'Отправить код заново через:'}
                </button>
                <span className={styles.timer} style={sendActive ? { display: 'none' } : {}}>
                    {formattedTime}
                </span>
            </div>
            <button type="button" className={styles.btn_back} onClick={() => props.setSecondFactor(false)}>
                Назад
            </button>
        </div>
    );
};

export default SecondFactor;
