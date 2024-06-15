//actions/incomeActions.js

export const ADD_INCOME = 'ADD_INCOME';
export const RESET_INCOME = 'RESET_INCOME';


export const addIncome = (income) =>({
    type: ADD_INCOME,
    payload :income,
})

export const resetIncome = () =>({
    type:RESET_INCOME
})