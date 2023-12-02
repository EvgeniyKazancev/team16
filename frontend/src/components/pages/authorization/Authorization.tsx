import styles from './Authorization.module.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch } from '../../../store/store';
import { addEmail, addPassword } from '../../../store/features/AuthorizationSlice';
import Checkbox from '../../reusable/Checkbox';
import Button from '../../reusable/Button';
import SecondFactor from './SecondFactor';
import { authRequest, codeRequest } from '../../../Services';

const Authorization = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [disabled, setDisabled] = useState(true);
    const [passwordVisible, setPasswordVisible] = useState(false);
    const [keepLoggedIn, setKeepLoggedIn] = useState(false);
    const [secondFactor, setSecondFactor] = useState(false);
    const [uuid, setUuid] = useState('');
    const [code, setCode] = useState('');
    const [error, setError] = useState(false);
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    // const authorizationInfo = useAppSelector((state) => state.Authorization);

    useEffect(() => {
        localStorage.getItem('uuid') ? navigate('/search') : {};
    }, []);

    const checkInfo = {
        text: 'Запомнить меня',
        value: 'keepLoggedIn',
        textColor: '#545454',
        borderColor: '#D3D3D3',
        fontSize: '16px',
        disabled: false,
        changedValue: keepLoggedIn
    };

    const validateEmail = (input: string) => {
        const validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

        if (input.match(validRegex)) {
            return true;
        } else {
            return false;
        }
    };

    useEffect(() => {
        if (email) {
            const validated = validateEmail(email);
            if (validated && password) {
                setDisabled(false);
                dispatch(addEmail({ email: email }));
                dispatch(addPassword({ password: password }));
            } else {
                setDisabled(true);
            }
        }
    }, [email, password]);

    useEffect(() => {
        if (code.length === 6) {
            setDisabled(false);
        } else {
            setDisabled(true);
        }
    }, [code]);

    const handleSend = async () => {
        if (!secondFactor) {
            if (!disabled) {
                const result = await authRequest(email, password);
                if (result.status === 200) {
                    setSecondFactor(true);
                    setUuid(result.id);
                    if (error) setError(false);
                } else {
                    setError(true);
                }
            }
        } else if (code) {
            const result = await codeRequest(uuid, code);
            if (result) {
                if (result.status === 200) {
                    keepLoggedIn ? localStorage.setItem('uuid', result.uuid) : sessionStorage.setItem('uuid', result.uuid);
                    navigate('/search');
                    if (error) setError(false);
                } else {
                    setError(true);
                }
            }
        }
    };

    return (
        <div className={styles.form_container}>
            <form action="" className={styles.form}>
                <div className={styles.heading_container}>
                    <div className={styles.heading_line}></div>
                    <h1 className={styles.heading}>Авторизация</h1>
                    <div className={styles.heading_line}></div>
                </div>
                {!secondFactor ? (
                    <div className={styles.form_content}>
                        <label htmlFor="" className={styles.input_label} style={error ? { border: '1px solid #FF4E4E' } : {}}>
                            <span className={styles.input_text}>E-mail</span>
                            <input type="email" className={styles.input} required value={email} onChange={(e) => setEmail(e.target.value)} />
                        </label>
                        <label htmlFor="" className={styles.input_label} style={error ? { border: '1px solid #FF4E4E' } : {}}>
                            <span className={styles.input_text}>Пароль</span>
                            <input type={passwordVisible ? 'text' : 'password'} className={styles.input} required value={password} onChange={(e) => setPassword(e.target.value)} />
                            <div
                                className={!passwordVisible ? `${styles.password_icon}` : `${styles.password_icon} ${styles.password_icon_visible}`}
                                onClick={() => {
                                    passwordVisible ? setPasswordVisible(false) : setPasswordVisible(true);
                                }}
                            ></div>
                        </label>
                        <Checkbox item={checkInfo} onChange={setKeepLoggedIn} />
                    </div>
                ) : (
                    <SecondFactor email={email} password={password} code={code} setCode={setCode} setSecondFactor={setSecondFactor} />
                )}
                <p className={styles.error} style={error ? {} : { display: 'none' }}>
                    Ошибка авторизации!
                </p>
                <p className={styles.error_desc} style={error ? {} : { display: 'none' }}>
                    {secondFactor ? 'Введен неверный код' : 'Введен неправильный логин или пароль.'}
                </p>
                <Button text="Войти" onClick={handleSend} width="100%" height="64px" disabled={disabled} marginTop="24px" fontSize="20px" lineHeight="24px" uppercase={false} />
            </form>
        </div>
    );
};

export default Authorization;
