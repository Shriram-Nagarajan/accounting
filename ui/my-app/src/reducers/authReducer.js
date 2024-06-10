const initialState = {
    // isLoggedIn: false,
    userName:"",
};

const authReducer = (state = initialState, action) => {
    switch (action.type) {
        // case 'Login_success':
        //     return {
        //         ...state,
        //         isLoggedIn: true,
        //         userName:action.payload.userName,
        //     };
        // case 'Logout':
        //     return {
        //         ...state,
        //         isLoggedIn: false,
        //     };
        case 'save_username':
            return {
                ...state,
                userName:action.payload.userName,
            }
        default:
            return state;

    }
}

export default authReducer;