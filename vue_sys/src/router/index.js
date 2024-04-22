import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login';
import menu from '@/components/menu';
import home from '@/components/home';
import addsig from '@/components/addsig';
import mysig from '@/components/mysig';
import adddoc from '@/components/adddoc';
import mydoc from '@/components/mydoc';
import record from '@/components/record';
import enroll from '@/components/enroll';
import mergePage from '@/components/mergePage';
import userManagement from '@/components/userManagement';
import sigManagement from '@/components/sigManagement';
import fileManagement from '@/components/fileManagement';
import recManagement from '@/components/recManagement';

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
      path: '/adddoc',
      name: 'adddoc',
      component: adddoc
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
    {
      path: '/enroll',
      name: 'enroll',
      component: enroll
    },
    {
      path: '/mergePage',
      name: 'mergePage',
      component: mergePage
    },
    {
      path: '/userManagement',
      name: 'userManagement',
      component: userManagement
    },
    {
      path: '/sigManagement',
      name: 'sigManagement',
      component: sigManagement
    },
    {
      path: '/fileManagement',
      name: 'fileManagement',
      component: fileManagement
    },
    {
      path: '/recManagement',
      name: 'recManagement',
      component: recManagement
    }
  ]
})
