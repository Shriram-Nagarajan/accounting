// actions/expenseActions.js
export const ADD_EXPENSE = 'ADD_EXPENSE';
export const RESET_EXPENSES = 'RESET_EXPENSES';

export const addExpense = (expense) => ({
  type: ADD_EXPENSE,
  payload: expense,
});

export const resetExpenses = () => ({
  type: RESET_EXPENSES,
});
