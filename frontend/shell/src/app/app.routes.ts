import { loadRemoteModule } from '@angular-architects/native-federation';
import { Routes } from '@angular/router';
import { HomePage } from './home-page/home-page';
import { UserView } from './users/user-view.comnponent';

export const routes: Routes = [
  {
    path: '',
    component: HomePage
  },
  {
    path: 'users',
    loadComponent: () => loadRemoteModule('users-app', './UserView').then(m => m.UserView),
  },
  {
    path: 'users2',
    component: UserView
  }
];
