//Table component for expense insights
import { Table } from '@mui/material';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';


function CategoryWiseExpensesTable(props) {
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
  let sumTotalExpenditure = props.expenses.map(exp => exp.totalExpenditure).reduce((acc, val) => {
    return acc + val;
  }, 0);

  return (
    <div >
      <TableContainer component={Paper} >
        <Table sx={{ minWidth: 500 }} size="small" aria-label="customizedtable">
          <TableHead>
            <TableRow>
              <StyledTableCell>Category</StyledTableCell>
              <StyledTableCell align="center">Number of expenses</StyledTableCell>
              <StyledTableCell align="right">Amount&nbsp;(in Rs.)</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {props.expenses.map((row) => (
              <StyledTableRow
                key={row.categoryName}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <StyledTableCell component="th" scope="row">
                  {row.categoryName}
                </StyledTableCell>
                <StyledTableCell align="center">{row.numOfExpenses}</StyledTableCell>
                <StyledTableCell align="right">{row.totalExpenditure.toFixed(2)}</StyledTableCell>
              </StyledTableRow>

            ))}
            <StyledTableRow >
              <StyledTableCell align="center" colSpan={2} sx={{ fontWeight: "bold", backgroundColor: 'rgb(25, 118, 210)', color: 'white' }}>Total expenditure</StyledTableCell>
              <StyledTableCell align="right" sx={{ fontWeight: "bold", backgroundColor: 'rgb(25, 118, 210)', color: 'white' }}>₹{sumTotalExpenditure}</StyledTableCell>
            </StyledTableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default CategoryWiseExpensesTable;