import { useEffect, useState } from "react";
import accountingApi from "../httputil/accountingApi";
import '../css/table.css';
import DatePicker from "./DatePicker";

function CategoryWiseExpensesTable() {
    
    const [expenses, setExpenses] = useState([]);
    const [fromDate, setFromDate] = useState("");
    const [toDate, setToDate] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();
        let data = {fromDate: fromDate, toDate: toDate};
        accountingApi.getCategoryWiseExpenses(data, (response) => {
            setExpenses(response.data);
        }, (error) => {
            console.error("Error fetching expenses", error);
        });
    }
    
    return (
        <div>
            <form onSubmit={handleSubmit}>
                <span>{'From Date: '}</span>
                <DatePicker name="from-date" onChange={setFromDate} /> <br/>
                <span>{'To Date: '}</span>
                <DatePicker name = "to-date" onChange={setToDate} /> <br/>
                <button type="submit">Submit</button>
            </form>
            <table className="data-table">
                <thead>
                <tr>
                    <th>Category</th>
                    <th>Number of expenses</th>
                    <th>Amount (in Rupees)</th>
                </tr>
                </thead>
                <tbody>
                {expenses.map(expense => (
                    <tr key={expense.categoryName}>
                    <td>{expense.categoryName}</td>
                    <td>{expense.numOfExpenses}</td>
                    <td>{expense.totalExpenditure}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );

}

export default CategoryWiseExpensesTable;