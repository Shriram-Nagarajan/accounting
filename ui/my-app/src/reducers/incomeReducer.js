//reducers/incomeReducer.js

import { ADD_INCOME,RESET_INCOME } from "../actions/incomeActions";

const initialState = {
    income:[],
};

const incomeReducer = (state = initialState,action) =>
    {
        switch (action.type) {
            case ADD_INCOME:
                return{
                    ...state,
                    income: [...state.income,action.payload],
                }
            case RESET_INCOME:
                return{
                    ...state,
                    income: [],
                }    
            default:
                return state;
        }
    }
export default incomeReducer;