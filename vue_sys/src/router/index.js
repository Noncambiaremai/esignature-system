import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login';
import menu from '@/components/menu';
import home from '@/components/home';
import addsig from '@/components/addsig';
import mysig from '@/components/mysig';
import mydoc from '@/components/mydoc';
import record from '@/components/record';

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/menu',
      name: 'menu',
      component: menu
    },
    {
      path: '/home',
      name: 'home',
      component: home
    },
    {
      path: '/addsig',
      name: 'addsig',
      component: addsig
    },
    {
      path: '/mysig',
      name: 'mysig',
      component: mysig
    },
    {
      path: '/mydoc',
      name: 'mydoc',
      component: mydoc
    },
    {
      path: '/record',
      name: 'record',
      component: record
    },
  ]
})
