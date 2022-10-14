
import StartPage from '../pages/start.f7';
import HomePage from '../pages/home.f7';
import codePage from '../pages/access.code.f7';

import NotFoundPage from '../pages/404.f7';
import iqaPage from '../pages/step1.iqa.f7'
import livenessPage from '../pages/step2.liveness.f7'
import comparePage from '../pages/step3.compare.f7'
import ResultPage from '../pages/result.f7'

var routes = [
  {
    path: '/',
    component: StartPage,
  },
  {
    path: '/validate/',
    component: codePage,
  },
  {
    path: '/home/',
    component: HomePage,
  },
  {
    path : '/iqa/',
    component : iqaPage
  },
  {
    path : '/liveness/',
    component : livenessPage
  },
  {
    path : '/compare/',
    component : comparePage
  },
  {
    path : '/compare/result/',
    component : ResultPage
  },
  {
    path: '(.*)',
    component: NotFoundPage,
  },
];

export default routes;