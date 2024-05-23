import { useEffect, useState } from "react";
import accountingApi from "../httputil/accountingApi";
import '../css/table.css';

function CategoryWiseExpensesTable() {
    
    const [expenses, setExpenses] = useState([]);

    useEffect(() => {
        accountingApi.getCategoryWiseExpenses((response) => {
            setExpenses(response.data);
        }, (error) => {
            console.error("Error fetching expenses", error);
        });
    }, []);
    
    return (
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
    );

}

export default CategoryWiseExpensesTable;