export const loginSuccess = (userName) =>
({
    type: 'Login_success',
    payload:{
        userName,
    }
})

export const logout = () =>
({
    type: 'Logout',
})