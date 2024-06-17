const initialState = {
    defaultCategories: [],
    categoriesLoaded: false // New flag to check if categories are loaded
};

const categoryReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'store_default_category':
            return {
                ...state,
                defaultCategories: action.payload.defaultCategories,
                categoriesLoaded: true // Set the flag to true when categories are loaded
            };
        
        default:
            return state;
    }
};

export default categoryReducer;
