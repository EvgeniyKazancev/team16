import styles from './Reusable.module.css';

type Item = {
    text: string;
    value: string;
    textColor: string;
    borderColor: string;
    fontSize: string;
    disabled: boolean;
    changedValue: boolean;
};

type Props = {
    item: Item;
    onChange: (arg: boolean) => void;
};

const Checkbox = (props: Props) => {
    const item = props.item;
    return (
        <label className={styles.check_container}>
            <input
                className={styles.check_input}
                type="checkbox"
                id={item.value}
                name={item.value}
                value={item.value}
                disabled={item.disabled}
                onChange={() => {
                    item.changedValue ? props.onChange(false) : props.onChange(true);
                }}
            />
            <span className={styles.check_fake} style={{ border: `1px solid ${item.borderColor}` }}></span>
            <div className={styles.check_text} style={{ fontSize: `${item.fontSize}`, color: `${item.textColor}` }}>
                {item.text}
            </div>
        </label>
    );
};

export default Checkbox;
