import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { SidebarAccPageRoutingModule } from './sidebar-acc-routing.module';

import { SidebarAccPage } from './sidebar-acc.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SidebarAccPageRoutingModule
  ],
  declarations: []
})
export class SidebarAccPageModule {}
