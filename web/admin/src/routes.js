//
import Home from './views/Home.vue'
import Info from './views/Info.vue'
import Test from './views/Test.vue'

import Company from './views/Company.vue'
import Units from './views/Units.vue'

import Product from './views/Info.vue'
import Customer from './views/Info.vue'
import User from './views/Info.vue'

// { path: '/hidden', component: Info, name: 'Customer', hidden: true  },


let routes = [
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
    }
];

export default routes;
