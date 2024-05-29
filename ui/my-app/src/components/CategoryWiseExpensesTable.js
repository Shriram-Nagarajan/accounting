import { useEffect, useState } from "react";
import accountingApi from "../httputil/accountingApi";
import '../css/table.css';
import DatePicker from "./DatePicker";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';


function CategoryWiseExpensesTable(props) {
    


    const StyledTableCell = styled(TableCell)(({ theme }) => ({
      [`&.${tableCellClasses.head}`]: {
        backgroundColor: theme.palette.common.black,
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
    // const handleSubmit = (event) => {
    //     event.preventDefault();
    //     let data = {fromDate: props.fromDate, toDate: props.toDate};
    //     accountingApi.getCategoryWiseExpenses(data, (response) => {
    //         setExpenses(response.data);
    //     }, (error) => {
    //         console.error("Error fetching expenses", error);
    //     });
    // }

    let sumTotalExpenditure = props.expenses.map(exp => exp.totalExpenditure).reduce((acc, val) => {
        return acc + val;
    }, 0);
    
    return (
        <div >
            
            <TableContainer component={Paper} >
        <Table sx={{ minWidth: 500 }} size = "small" aria-label="customizedtable">
          <TableHead>
            <TableRow>
              <StyledTableCell>Category</StyledTableCell>
              <StyledTableCell align="center">Number of expenses</StyledTableCell>
              <StyledTableCell align="right">Amount&nbsp;(in Rs.)</StyledTableCell>
              {/* <TableCell align="right">Carbs&nbsp;(g)</TableCell>
              <TableCell align="right">Protein&nbsp;(g)</TableCell> */}
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
                {/* <TableCell align="right">{row.number}</TableCell> */}
                <StyledTableCell align="center">{row.numOfExpenses}</StyledTableCell>
                <StyledTableCell align="right">{row.totalExpenditure}</StyledTableCell>
                {/* <TableCell align="right">{row.protein}</TableCell> */}
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
            {/* <table className="data-table">
                <thead>
                <tr>
                    <th>Category</th>
                    <th>Number of expenses</th>
                    <th>Amount (in Rupees)</th>
                </tr>
                </thead>
                <tbody>
                {expenses.sort((e1, e2) => e2.totalExpenditure - e1.totalExpenditure).map(expense => (
                    <tr key={expense.categoryName}>
                    <td>{expense.categoryName}</td>
                    <td>{expense.numOfExpenses}</td>
                    <td>{expense.totalExpenditure}</td>
                    </tr>
                ))}
                </tbody>
            </table> */}
            <span>{'Total expenditure: Rs. ' + sumTotalExpenditure}</span>
        </div>
    );

}

export default CategoryWiseExpensesTable;