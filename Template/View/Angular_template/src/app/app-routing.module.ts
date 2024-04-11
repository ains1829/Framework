import { NgModule } from '@angular/core';
import axios from 'axios';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home/voiture',
    pathMatch: 'full'
  },
  // CRUD voiture
  {
    path: 'home/voiture',
    loadChildren: () => import('./voiture/home/home.module').then( m => m.HomePageModule)
  },
  {
    path: 'voiture/insert',
    loadChildren: () => import('./voiture/formulaire/insert/insert.module').then( m => m.InsertPageModule)
  },
  {
    path: 'voiture/modal',
    loadChildren: () => import('./voiture/formulaire/modal/modal.module').then( m => m.ModalPageModule)
  },
  // CRUD marque
  {
    path: 'formulaire',
    loadChildren: () => import('./marque/formulaire/formulaire.module').then( m => m.FormulairePageModule)
  },
  {
    path: 'home/marque',
    loadChildren: () => import('./marque/home/home.module').then( m => m.HomePageModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
