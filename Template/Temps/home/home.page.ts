import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { IonModal,ModalController } from '@ionic/angular';
import { OverlayEventDetail } from '@ionic/core/components';
import { AnimationController } from '@ionic/angular';
import { MainService } from '../../services/main.service';
import { Fiara } from '../../services/Voiture.interface';
import { ModalPage } from '../formulaire/modal/modal.page'

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  // Modal 
  @ViewChild(IonModal)
  modal!: IonModal;
  // Liste 
  voit: any[] = [];
  message = 'Test.';
  name: string | undefined;
  // Necessaire  pour les modal
  isModalOpen = false;
  isUpdateModalOpen = false;
  isDeleteAlertOpen = false;
  data = [];

  // Constructeur (attribut apesaina) 
  constructor(private router:Router,private animationCtrl: AnimationController, private mainService: MainService, private modalController:ModalController) {}
  
  // Fonction Init
  async ngOnInit() {
    const test = await this.mainService.get_all_voiture();
    this.setFiaraList(test);
  }
  
  // Setters ---
  setFiaraList(l: Fiara[]){
    this.voit = l;
  }
  setOpen(isOpen: boolean) {// DETAIL
    this.isModalOpen = isOpen;
  }
  setUpdateOpen(isOpen: boolean){// UPDATE
    this.isUpdateModalOpen = isOpen
  }
  setDeleteOpen(isOpen: boolean){// DELETE
    this.isDeleteAlertOpen = isOpen
  }
  // Fin setters ---

  // Fonction JS utile pour l'affichage  
  cancel() {
    this.modal.dismiss(null, 'cancel');
  }
  confirm() {
    this.modal.dismiss(this.name, 'confirm');
  }
  onWillDismissUpdate(event: Event) {
    const ev = event as CustomEvent<OverlayEventDetail<string>>;
    if (ev.detail.role === 'confirm') {
      this.message = `Hello, ${ev.detail.data}!`;
    }this.setUpdateOpen(false)
  }
  
  // Fonctions CRUD 
  go_to_create_voiture(){ // insertion 
    console.log("Create nouveau voiture !");
    this.router.navigate(['/voiture/insert']);
  }
  async openDetailVoiture(item: any){ // detail -> delete  
    console.log(item);
    const modal = await this.modalController.create({
      component: ModalPage,
      componentProps: {
        'item':item
      }
    });
    return await modal.present();
  }
  
  // Animation l'update
  enterAnimation = (baseEl: HTMLElement) => {
    const root = baseEl.shadowRoot;

    const backdropAnimation = this.animationCtrl
      .create()
      .addElement(root!.querySelector('ion-backdrop')!)
      .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

    const wrapperAnimation = this.animationCtrl
      .create()
      .addElement(root!.querySelector('.modal-wrapper')!)
      .keyframes([
        { offset: 0, opacity: '0', transform: 'scale(0)' },
        { offset: 1, opacity: '0.99', transform: 'scale(1)' },
      ]);

    return this.animationCtrl
      .create()
      .addElement(baseEl)
      .easing('ease-out')
      .duration(500)
      .addAnimation([backdropAnimation, wrapperAnimation]);
  };
  leaveAnimation = (baseEl: HTMLElement) => {
    return this.enterAnimation(baseEl).direction('reverse');
  };
  // fin Animation 

  // (Test affichage) pour confirmer un delete ou non
  public alertButtons = [
    {
      text: 'Cancel',
      role: 'cancel',
      handler: () => {
        console.log('Annuler');
      },
    },
    {
      text: 'OK',
      role: 'confirm',
      handler: () => {
        console.log('Confirmer');
      },
    },
  ];
  setResult(event: Event) {
    const ev = event as CustomEvent<OverlayEventDetail<string>>;
    console.log(`Dismissed with role: ${ev.detail.role}`);
  }


}
