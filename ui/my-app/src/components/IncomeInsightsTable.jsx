//Table component for expense insights
import { Table } from '@mui/material';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';


function IncomeInsightsTable(props) {
    let index =0;

  //styling for table cells
  const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: 'rgb(25, 118, 210)',//theme.palette.common.black,
      color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));
  //styling for table rows
  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.action.hover,
    },
    // hide last border
    '&:last-child td, &:last-child th': {
      border: 0,
    },
  }));
  //calculation for total expenditure to be shown in table last row
  let sumTotalExpenditure = props.income.map(exp => exp.amount).reduce((acc, val) => {
    return acc + val;
  }, 0);

  return (
    <div >
      <TableContainer component={Paper} >
        <Table sx={{ minWidth: 700 }} size="small" aria-label="customizedtable">
          <TableHead>
            <TableRow>
              <StyledTableCell>Date</StyledTableCell>
              <StyledTableCell align="left">Description</StyledTableCell>
              <StyledTableCell align="right">Amount&nbsp;(in Rs.)</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {props.income.map((row) => (
              <StyledTableRow
                key={++index}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <StyledTableCell component="th" scope="row" sx={{width:'120px'}}>
                  {row.date}
                </StyledTableCell>
                <StyledTableCell align="left">{row.description}</StyledTableCell>
                <StyledTableCell align="right" sx={{width:'150px'}}>{row.amount.toFixed(2)}</StyledTableCell>
              </StyledTableRow>

            ))}
            <StyledTableRow >
              <StyledTableCell align="center" colSpan={2} sx={{ fontWeight: "bold", backgroundColor: 'rgb(25, 118, 210)', color: 'white' }}>Total income</StyledTableCell>
              <StyledTableCell align="right" sx={{ fontWeight: "bold", backgroundColor: 'rgb(25, 118, 210)', color: 'white' }}>₹{sumTotalExpenditure.toFixed(2)}</StyledTableCell>
            </StyledTableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default IncomeInsightsTable;