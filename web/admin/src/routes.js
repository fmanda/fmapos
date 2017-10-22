//
import Home from './views/Home.vue'
import NotFound from './views/404.vue'
import Info from './views/Info.vue'
import Test from './views/Test.vue'
import Login from './views/Login.vue'
import Company from './views/Company.vue'
import Units from './views/Units.vue'
import Customer from './views/Customer.vue'
import CustomerForm from './views/CustomerForm.vue'
import Product from './views/Product.vue'
import ProductForm from './views/ProductForm.vue'
import User from './views/Info.vue'
import Order from './views/Order.vue'
import OrderForm from './views/OrderForm.vue'


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
		name: 'Dashboard',
        iconCls: 'fa fa-bar-chart',
        children: [
            { path: '/Dashboard', component: User, name: 'Sales', iconCls:'fa fa-bar-chart'},
        ]
    },
	{
        path: '/',
        component: Home,
		name: 'Master',
        iconCls: 'fa fa-tags',
        children: [
            { path: '/company', component: Company, name: 'Company', iconCls:'fa fa-building'},
			{ path: '/units', component: Units, name: 'Units', iconCls:'fa fa-sitemap'},
			{ path: '/product', component: Product, name: 'Product', iconCls:'fa fa-tags'},
			{ path: '/customer', component: Customer, name: 'Customer', iconCls:'fa fa-user'},
			{ path: '/customer/:id', component: CustomerForm, name: 'CustomerForm' , hidden: true },
			{ path: '/product/:id', component: ProductForm, name: 'ProductForm' , hidden: true },
        ]
    },
	{
        path: '/',
        component: Home,
		name: 'Sales',
        iconCls: 'fa fa-credit-card-alt',
        children: [
            { path: '/order', component: Order, name: 'Order', iconCls:'fa fa-credit-card-alt'},
			{ path: '/order/:id', component: OrderForm, name: 'OrderForm' , hidden: true },
        ]
    },
	{
        path: '/',
        component: Home,
		name: 'Reports',
        iconCls: 'fa fa-file-text',
        children: [
            { path: '/reports', component: User, name: 'Sales Report', iconCls:'fa fa-file-text'},
        ]
    },
	{
        path: '/',
        component: Home,
		name: 'Setting',
        iconCls: 'fa fa-id-card-o',
        children: [
            { path: '/user', component: User, name: 'User', iconCls:'fa fa-users'},
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
