import styles from './Footer.module.css';

const Footer = () => {
    return (
        <footer className={styles.footer}>
            <h4 className={styles.text}>
                Created by <span style={{ fontWeight: 600, textTransform: 'uppercase' }}>“team16”</span> | CONTENTED | SKILLFACTORY
            </h4>
        </footer>
    );
};

export default Footer;
