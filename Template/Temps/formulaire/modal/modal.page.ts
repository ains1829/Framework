import { Component, OnInit , Input} from '@angular/core';
import { ModalController } from '@ionic/angular';
import { AnimationController } from '@ionic/angular';
import { OverlayEventDetail } from '@ionic/core/components';
import { MainService } from 'src/app/services/main.service';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.page.html',
  styleUrls: ['./modal.page.scss'],
})
export class ModalPage implements OnInit {
  // Props
  @Input() item: any;
  isModalOpen = false;
  isDeleteAlertOpen = false;

  constructor( private mainService: MainService,private modalController: ModalController,private animationCtrl: AnimationController) { }

  ngOnInit() {
  }

  // Fonction crud
  // exemple
  // async delete#NomTable(nomAttr)
  async deleteVoiture(idvehicule: number|undefined){ // delete
    await this.mainService.delete_voiture(idvehicule);
  }
  setDeleteOpen(isOpen: boolean){
    this.isDeleteAlertOpen = isOpen
  }
  // fin crud

// ---------------------------------------------------------------- 
// Les fonction manaraka eto sont identiques pour toutes les modals 
// ---------------------------------------------------------------- 

// Fonction utile pour l'affichage
  closePopup(){
    this.isModalOpen = false;
    this.modalController.dismiss();
  }
  cancel() {
    this.modalController.dismiss(null, 'cancel');
  }
  confirm() {
    this.modalController.dismiss(null, 'confirm');
  }

// Fonction animation
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

// Delete option
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
    this.isDeleteAlertOpen = false;
    console.log(this.item.idvehicule)
    console.log(`Dismissed with role: ${ev.detail.role}`);
    if( ev.detail.role == "confirm" ){
      this.deleteVoiture(this.item.idvehicule); // Except ::: si confirm donc appel fonction supprimer 
    }
  }


}
