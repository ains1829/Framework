import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SidebarAccPage } from './sidebar-acc.page';

const routes: Routes = [
  {
    path: '',
    component: SidebarAccPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SidebarAccPageRoutingModule {}
