import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AcceuilPage } from './acceuil.page';

const routes: Routes = [
  {
    path: '',
    component: AcceuilPage
  },
  {
    path: 'sidebar-acc',
    loadChildren: () => import('./sidebar-acc/sidebar-acc.module').then( m => m.SidebarAccPageModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AcceuilPageRoutingModule {}
