import { useEffect, useState } from 'react';
import styles from './Filters.module.css';
import Radio from './Radio';
import DateInput from './Date';
import Check from './Check';
import Button from '../../../reusable/Button';
import { sourcesRequest } from '../../../../Services';
// import { Options, Question } from '../../../types/types';

const paginationInfo = {
    type: 'radio',
    text: 'Новостей на странице',
    id: '1',
    options: [
        {
            text: '24',
            id: '1-1',
            value: 24
        },
        {
            text: '36',
            id: '1-2',
            value: 36
        },
        {
            text: '48',
            id: '1-3',
            value: 48
        },
        {
            text: '60',
            id: '1-4',
            value: 60
        }
    ]
};

const pubTimeInfo = {
    type: 'radio',
    text: 'Время публикации',
    id: '2',
    options: [
        {
            text: 'За сутки',
            id: '2-1',
            value: new Date(Date.now() - 24 * 3600 * 1000).toString()
        },
        {
            text: 'За неделю',
            id: '2-2',
            value: new Date(Date.now() - 24 * 3600 * 1000 * 7).toString()
        },
        {
            text: 'За месяц',
            id: '2-3',
            value: new Date(Date.now() - 24 * 3600 * 1000 * 30).toString()
        },
        {
            text: 'Не важно',
            id: '2-4',
            value: new Date(new Date(new Date().setFullYear(new Date().getFullYear() - 100))).toString()
        }
    ]
};

const dateInfo = {
    type: 'date',
    text: 'Укажите дату',
    id: '3',
    options: [
        {
            text: '',
            value: '',
            id: '3-1'
        }
    ]
};

const tagsInfo = {
    type: 'check',
    text: 'Теги',
    id: '5',
    options: [
        {
            text: 'юмор',
            id: '5-1',
            value: 1
        },
        {
            text: 'наука',
            id: '5-2',
            value: 2
        },
        {
            text: 'спорт',
            id: '5-3',
            value: 3
        },
        {
            text: 'кино',
            id: '5-4',
            value: 4
        },
        {
            text: 'музыка',
            id: '5-5',
            value: 5
        },
        {
            text: 'игры',
            id: '5-6',
            value: 6
        },
        {
            text: 'животные',
            id: '5-7',
            value: 7
        }
    ]
};

type Props = {
    setStartDate: (arg: number | string) => void;
    setEndDate: (arg: number | string) => void;
    setCatIds: (arg: (number | string)[]) => void;
    setSourceIds: (arg: (number | string)[]) => void;
    setSize: (arg: number | string) => void;
};

/* eslint-disable @typescript-eslint/no-explicit-any */

const Filters = (props: Props) => {
    const [filtersShown, setFiltersShown] = useState(false);
    const [sourcesInfo, setSourcesInfo] = useState<any>();
    const uuid = localStorage.getItem('uuid') || sessionStorage.getItem('uuid');

    const showFilter = () => {
        filtersShown ? setFiltersShown(false) : setFiltersShown(true);
    };

    const getSources = async () => {
        if (uuid) {
            const result = await sourcesRequest(uuid, '4');
            // console.log(result);

            return result ? setSourcesInfo(result) : {};
        }
    };

    useEffect(() => {
        getSources();
    }, []);

    return (
        <form action="" className={styles.container}>
            <div className={styles.heading_container} onClick={showFilter}>
                <h3 className={styles.heading}>Фильтр</h3>
                <button type="button" onClick={showFilter} className={styles.btn} style={filtersShown ? { transform: 'rotate(0.5turn)' } : {}}></button>
            </div>
            <div className={styles.filters_container} style={!filtersShown ? { display: 'none' } : {}}>
                <ul className={styles.filters_list}>
                    <Radio question={paginationInfo} onClick={props.setSize} />
                    <Radio question={pubTimeInfo} onClick={props.setStartDate} />
                    <DateInput question={dateInfo} setStartDate={props.setStartDate} setEndDate={props.setEndDate} />
                    <Check question={sourcesInfo} onClick={props.setSourceIds} />
                    <Check question={tagsInfo} onClick={props.setCatIds} />
                </ul>
                <div className={styles.reset_btn_container}>
                    <div className={styles.reset_btn_line}></div>
                    <Button text="Сбросить фильтр" onClick={() => {}} width="198px" height="36px" disabled={false} marginTop="0" fontSize="16px" lineHeight="16px" uppercase={true} />
                    <div className={styles.reset_btn_line}></div>
                </div>
            </div>
        </form>
    );
};

export default Filters;
