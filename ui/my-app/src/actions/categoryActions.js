// action for add default category
export const addDefaultCategory = (defaultCategories) =>
    ({
        type: 'store_default_category',
        payload:{
            defaultCategories,
        }
    })
