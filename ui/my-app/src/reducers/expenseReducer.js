// reducers/expenseReducer.js
import { ADD_EXPENSE, RESET_EXPENSES } from '../actions/expenseActions';

const initialState = {
  expenses: [],
};

const expenseReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_EXPENSE:
      return {
        ...state,
        expenses: [...state.expenses, action.payload],
      };
    case RESET_EXPENSES:
      return {
        ...state,
        expenses: [],
      };
    default:
      return state;
  }
};

export default expenseReducer;
