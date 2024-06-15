import { combineReducers } from "redux";
import authReducer from "./authReducer";
import categoryReducer from "./categoryReducer";
import expenseReducer from "./expenseReducer";
import incomeReducer from "./incomeReducer";


const rootReducer = combineReducers({
    auth :authReducer,
    category:categoryReducer,
    expenses:expenseReducer,
    income:incomeReducer,
})


export default rootReducer;