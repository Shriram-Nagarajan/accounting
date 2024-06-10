// // action for login success
// export const loginSuccess = (userName) =>
// ({
//     type: 'Login_success',
//     payload:{
//         userName,
//     }
// })
// //action for logout
// export const logout = () =>
// ({
//     type: 'Logout',
// })

export const saveUserName = (userName) =>
    ({
        type:'save_username',
        payload:{userName,}
    })