import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { HomePage } from './home.page';
import { SidebarPage } from './sidebar/sidebar.page';
import { HomePageRoutingModule } from './home-routing.module';
import { ModifierPage } from '../formulaire/modifier/modifier.page';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    HomePageRoutingModule
  ],
  declarations: [HomePage,SidebarPage,ModifierPage]
})
export class HomePageModule {}
