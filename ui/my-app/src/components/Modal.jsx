// SliceModal.jsx
import React,{useEffect, useState} from 'react';
import { Modal, Box, Typography } from '@mui/material';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';


const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 900,
    maxHeight: 400,  // Set a fixed height for the modal content
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
    overflowY: 'auto', // Enable vertical scrolling
};
const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: 'rgb(25, 118, 210)',//backgroundColor: theme.palette.common.black,
      color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));
  
  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.action.hover,
    },
    // hide last border
    '&:last-child td, &:last-child th': {
      border: 0,
    },
  }));


function ModalPopup({ open, handleClose, data,from }) {
  const[isPreview,setIsPreview] = useState(false);
  const [sumTotalExpenditure, setSumTotalExpenditure] = useState(0);
  useEffect(() => {
    if (from === "einsights") {
      const totalExpenditure = data ? data.map(exp => exp.amount).reduce((acc, val) => acc + val, 0) : 0;
      setSumTotalExpenditure(totalExpenditure);
    } else if (from === "efileuploadforpreview") {
      setIsPreview(true);
    }
  }, [data, from]);
  const formatDate = (date) => {
    const d = new Date(date);
    return d.toLocaleDateString('en-GB'); // Adjust the format as needed
  };
    return (
        <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={style}>
            <div >
            {data?
            <TableContainer  component={Paper} sx={{ maxHeight: 450 }} >
        <Table stickyHeader sx={{ minWidth: 550 }} size = "small" aria-label="customizedtable">
          <TableHead>
            <TableRow>
              <StyledTableCell align="center">Date</StyledTableCell>
              <StyledTableCell align="center">Description</StyledTableCell>
              <StyledTableCell align="left">Amount&nbsp;(in ₹)</StyledTableCell>
              {isPreview ? <StyledTableCell align="left">Category</StyledTableCell> : null}
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row) => (
              <StyledTableRow
                key={row.date}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <StyledTableCell align="left" component="th" scope="row" sx={{width:"120px"}}>
                {formatDate(row.date)}
                </StyledTableCell>
                <StyledTableCell align="left">{row.description}</StyledTableCell>
                {isPreview ? <StyledTableCell align="left">{row.category}</StyledTableCell> : null}

                <StyledTableCell align="right" sx={{width:"120px"}}>{row.amount}</StyledTableCell>

              </StyledTableRow>
              
            ))}
            {!isPreview?(<StyledTableRow >
            <StyledTableCell align="left" colSpan={2} sx={{fontWeight : "bold"}}>Total expenditure</StyledTableCell>
            <StyledTableCell align="right" sx={{fontWeight : "bold"}}>₹{sumTotalExpenditure}</StyledTableCell>
          </StyledTableRow>) :null}
          </TableBody>
        </Table>
      </TableContainer>
      :null}
            
            {/* <span>{'Total expenditure: Rs. ' + sumTotalExpenditure}</span> */}
        </div>
                {/* <Typography id="modal-modal-title" variant="h6" component="h2">
                    Slice Details
                </Typography>
                <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                    {data ? (
                        <>
                            <p>Label: {data.label}</p>
                            <p>Value: {data.value}</p>
                        </>
                    ) : (
                        <p>No data available</p>
                    )}
                </Typography> */}
            </Box>
        </Modal>
    );
}

export default ModalPopup;