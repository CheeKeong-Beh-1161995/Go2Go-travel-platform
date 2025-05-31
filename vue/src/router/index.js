import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/front/home' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: 'System Home' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: 'Admin Information' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: 'System Announcements' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: 'Profile' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: 'Change Password' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'article', meta: { name: 'Travel Guide' }, component: () => import('@/views/manager/Article.vue'), },
        { path: 'routes', meta: { name: 'Travel Routes' }, component: () => import('@/views/manager/Routes.vue'), },
        { path: 'travels', meta: { name: 'Travel Diaries' }, component: () => import('@/views/manager/Travels.vue'), },
        { path: 'comments', meta: { name: 'Comments' }, component: () => import('@/views/manager/Comments.vue'), },
        { path: 'review', meta: { name: 'Review' }, component: () => import('@/views/manager/Review.vue'), },
        { path: 'user', meta: { name: 'User Information' }, component: () => import('@/views/manager/User.vue'), },
        { path: 'tourism', meta: { name: 'Tourism Products' }, component: () => import('@/views/manager/Tourism.vue'), },
        { path: 'car', meta: { name: 'Car Products' }, component: () => import('@/views/manager/Car.vue'), },
        { path: 'orders', meta: { name: 'Tourism Orders' }, component: () => import('@/views/manager/Orders.vue'), },
        { path: 'collect', meta: { name: 'Collect Information' }, component: () => import('@/views/manager/Collect.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'home_login', component: () => import('@/views/front/Home_login.vue'),  },
        { path: 'article', component: () => import('@/views/front/Article.vue'),  },
        { path: 'articleDetail', component: () => import('@/views/front/ArticleDetail.vue'),  },
        { path: 'addReview', component: () => import('@/views/front/addReview.vue'),  },
        { path: 'addTravels', component: () => import('@/views/front/addTravels.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'), },
        { path: 'password', component: () => import('@/views/front/Password.vue'), },
        { path: 'tourism', component: () => import('@/views/front/Tourism.vue'), },
        { path: 'tourismDetail', component: () => import('@/views/front/TourismDetail.vue'), },
        { path: 'orders', component: () => import('@/views/front/Orders.vue'), },
        { path: 'search', component: () => import('@/views/front/Search.vue'), },
        { path: 'about', component: () => import('@/views/front/About.vue'), },
        { path: 'guest', component: () => import('@/views/front/Guest.vue'), },
        { path: 'car', component: () => import('@/views/front/Car.vue'), },
        { path: 'carDetail', component: () => import('@/views/front/CarDetail.vue'), },
        { path: 'travelDetail', component: () => import('@/views/front/TravelDetail.vue'), },
        { path: 'collect', component: () => import('@/views/front/Collect.vue'), },
        { path: 'reviewDetail', component: () => import('@/views/front/ReviewDetail.vue'),  },
      ]
    },
    {
      path: '/loginNav',
      component: () => import('@/views/LoginNav.vue'),
      children: [
        { path: 'login', component: () => import('@/views/Login.vue') },
        { path: 'adminLogin', component: () => import('@/views/AdminLogin.vue') },
        { path: 'register', component: () => import('@/views/Register.vue') },
        { path: 'forgetPassword', component: () => import('@/views/ForgetPassword.vue') },
      ]
    },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

export default router