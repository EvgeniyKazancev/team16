import styles from './Reusable.module.css';

type Props = {
    text: string;
    onClick: () => void;
    width: string;
    height: string;
    disabled: boolean;
    marginTop: string;
    fontSize: string;
    lineHeight: string;
    uppercase: boolean;
};

const Button = (props: Props) => {
    return (
        <button
            className={styles.btn}
            onClick={props.onClick}
            style={{
                width: `${props.width}`,
                height: `${props.height}`,
                marginTop: `${props.marginTop}`,
                fontSize: `${props.fontSize}`,
                lineHeight: `${props.lineHeight}`,
                textTransform: props.uppercase ? 'uppercase' : 'none'
            }}
            disabled={props.disabled}
            type="button"
        >
            {props.text}
        </button>
    );
};

export default Button;
