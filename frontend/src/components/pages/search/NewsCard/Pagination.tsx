import ReactPaginate from 'react-paginate';
import './pagination.css';

type Props = {
    setPageCount: (arg: number) => void;
    pagesAmount: number | undefined;
};

export function PaginatedItems(props: Props) {
    const handlePageClick = (event: { selected: number }) => {
        props.setPageCount(event.selected);
    };

    return (
        <>
            <div
                style={
                    !props.pagesAmount || props.pagesAmount <= 1
                        ? {
                              display: 'none'
                          }
                        : {
                              display: 'flex',
                              flexDirection: 'column',
                              justifyContent: 'center',
                              boxSizing: 'border-box',
                              marginTop: '20px',
                              width: '100%',
                              height: '100%'
                          }
                }
            >
                <ReactPaginate
                    activeClassName={'item active '}
                    breakClassName={'item break-me '}
                    containerClassName={'pagination'}
                    disabledClassName={'disabled-page'}
                    marginPagesDisplayed={2}
                    nextClassName={'item next '}
                    pageClassName={'item pagination-page '}
                    previousClassName={'item previous'}
                    breakLabel="..."
                    nextLabel="hello"
                    onPageChange={handlePageClick}
                    pageRangeDisplayed={1}
                    pageCount={typeof props.pagesAmount === 'number' ? props.pagesAmount : 0}
                    previousLabel="hello"
                    renderOnZeroPageCount={null}
                />
            </div>
        </>
    );
}
