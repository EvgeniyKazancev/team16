type Props = {
    data: string;
};

const Comment = (props: Props) => {
    return <div>{props.data}</div>;
};

export default Comment;
