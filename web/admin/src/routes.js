//
import Home from './views/Home.vue'
import Info from './views/Info.vue'
import Test from './views/Test.vue'
import Login from './views/Login.vue'

import Company from './views/Company.vue'
import Units from './views/Units.vue'
import Customer from './views/Customer.vue'
import CustomerForm from './views/CustomerForm.vue'

import Product from './views/Info.vue'
import User from './views/Info.vue'
import NotFound from './views/404.vue'

// { path: '/hidden', component: Info, name: 'Customer', hidden: true  },


let routes = [
	{
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
	{
        path: '/',
        component: Home,
		name: 'Master',
        iconCls: 'fa fa-tags',
        children: [
            { path: '/company', component: Company, name: 'Company' },
			{ path: '/units', component: Units, name: 'Units' },
			{ path: '/product', component: Product, name: 'Product'  },
			{ path: '/customer', component: Customer, name: 'Customer'  },
			{ path: '/customer/:id', component: CustomerForm, name: 'CustomerForm' , hidden: true },
        ]
    },
	{
        path: '/',
        component: Home,
		name: 'Setting',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/user', component: User, name: 'User'  },
        ]
    },
	{
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;
